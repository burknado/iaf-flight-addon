package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import com.aelux.iafflight.amphithere.AmphithereFlightConfig;
import com.aelux.iafflight.amphithere.IAmphithereFlightData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AmphithereEntity.class)
public abstract class AmphithereEntityMixin extends LivingEntity implements IAmphithereFlightData {
    @Unique
    private static final EntityDataAccessor<Float> IAFFLIGHT$STAMINA =
            SynchedEntityData.defineId(AmphithereEntity.class, EntityDataSerializers.FLOAT);

    @Unique
    private boolean iafflight$gliding;

    protected AmphithereEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void iafflight$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IAFFLIGHT$STAMINA, AmphithereFlightConfig.MAX_STAMINA);
    }

    @Override
    public float iafflight$getStamina() {
        return this.entityData.get(IAFFLIGHT$STAMINA);
    }

    @Override
    public void iafflight$setStamina(float stamina) {
        this.entityData.set(IAFFLIGHT$STAMINA, stamina);
    }

    @Override
    public boolean iafflight$isGliding() {
        return this.iafflight$gliding;
    }

    @Override
    public void iafflight$setGliding(boolean gliding) {
        this.iafflight$gliding = gliding;
    }

    @Unique
    private boolean iafflight$updateGliding(float stamina) {
        boolean gliding = this.iafflight$gliding
                ? stamina < AmphithereFlightConfig.MAX_STAMINA

                : stamina <= AmphithereFlightConfig.GLIDE_ENTER_STAMINA;
        this.iafflight$gliding = gliding;
        return gliding;
    }

    @Unique
    private boolean iafflight$isTouchingFluid(AmphithereEntity self) {
        return self.isInWater() || self.isInLava() || !self.level().getFluidState(self.blockPosition()).isEmpty();
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void iafflight$flightVectorFilter(Vec3 travelVector, CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;

        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (this.iafflight$isTouchingFluid(self)) {
            if (self.isFlying()) {
                self.setFlying(false);

                self.setDeltaMovement(self.getDeltaMovement().scale(0.2D));
            }
            return;
        }
        if (!self.isFlying()) {
            return;
        }

        if (this.iafflight$gliding && self.onGround()) {
            self.setFlying(false);
            return;
        }

        Vec3 raw = self.getDeltaMovement();

        float vertical = (float) raw.y;
        float stamina = this.iafflight$getStamina();
        stamina -= AmphithereFlightConfig.STAMINA_DRAIN_IDLE_PER_TICK;
        if (vertical > 0F) {
            stamina -= vertical * AmphithereFlightConfig.STAMINA_DRAIN_PER_CLIMB_UNIT;
        } else if (vertical < 0F) {
            stamina += -vertical * AmphithereFlightConfig.STAMINA_REFUND_PER_FALL_UNIT;
        }
        stamina = Math.max(0F, Math.min(AmphithereFlightConfig.MAX_STAMINA, stamina));

        boolean gliding = this.iafflight$updateGliding(stamina);
        this.iafflight$setStamina(stamina);

        Vec3 direction;
        if (gliding || self.isGoingDown()) {
            direction = Vec3.directionFromRotation(AmphithereFlightConfig.FORCED_GLIDE_ANGLE_DEGREES, self.getYRot());
        } else if (self.isGoingUp()) {
            direction = Vec3.directionFromRotation(-AmphithereFlightConfig.FORCED_CLIMB_ANGLE_DEGREES, self.getYRot());
        } else {
            direction = Vec3.directionFromRotation(self.getXRot(), self.getYRot());
        }

        Vec3 result = direction.normalize().scale(AmphithereFlightConfig.CONSTANT_FLIGHT_SPEED);
        self.setDeltaMovement(result);

        double horizontalMag = Math.sqrt(result.x * result.x + result.z * result.z);
        float actualPitch = (float) -Math.toDegrees(Math.atan2(result.y, horizontalMag));
        self.setXRot(actualPitch);
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/AmphithereEntity;isGoingUp()Z")
    )
    private boolean iafflight$gateTakeoffInput(AmphithereEntity self) {
        if (this.iafflight$isTouchingFluid(self)) {
            return false;
        }
        if (this.iafflight$getStamina() < AmphithereFlightConfig.MAX_STAMINA) {
            return false;
        }
        if (self.getXRot() > AmphithereFlightConfig.IGNORE_TAKEOFF_INPUT_PITCH_DEGREES) {
            return false;
        }
        return self.isGoingUp();
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    )
    private int iafflight$requireFullStaminaForWildTakeoff(RandomSource instance, int bound) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.level().isClientSide) {
            return -1;
        }
        if (this.iafflight$getStamina() < AmphithereFlightConfig.MAX_STAMINA) {
            return -1;
        }
        return instance.nextInt(bound);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void iafflight$resumeFlightWhenEligible(CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.level().isClientSide) {
            return;
        }
        if (self.isFlying() || self.isBaby()) {
            return;
        }
        if (!self.getPassengers().isEmpty() || self.isNoAi() || !self.canMove()) {
            return;
        }
        if (this.iafflight$getStamina() < AmphithereFlightConfig.MAX_STAMINA) {
            return;
        }
        if (this.random.nextInt(200) != 0) {
            return;
        }

        double burst = this.iafflight$isTouchingFluid(self) ? 1.2D : 0.5D;
        self.setDeltaMovement(self.getDeltaMovement().x, self.getDeltaMovement().y + burst, self.getDeltaMovement().z);
        self.setFlying(true);
    }

    @Redirect(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/AmphithereEntity;isOverAir()Z")
    )
    private boolean iafflight$fixOverAirNearEdges(AmphithereEntity self) {
        if (self.onGround()) {
            return false;
        }
        return self.level().isEmptyBlock(self.blockPosition().below());
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void iafflight$autoTakeoffWhenMountedByNonOwner(CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.level().isClientSide) {
            return;
        }
        if (self.isFlying() || self.isBaby()) {
            return;
        }
        if (self.isTame() || self.getUntamedRider() == null) {
            return;
        }
        if (this.iafflight$isTouchingFluid(self)) {
            return;
        }
        if (this.iafflight$getStamina() < AmphithereFlightConfig.MAX_STAMINA) {
            return;
        }
        self.setDeltaMovement(self.getDeltaMovement().add(0, 0.5, 0));
        self.setFlying(true);
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void iafflight$neutralizeRiddenInput(Player player, Vec3 travelVector, CallbackInfoReturnable<Vec3> cir) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.isFlying()) {
            cir.setReturnValue(Vec3.ZERO);
        }
    }

    @Redirect(
            method = "tickRidden",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 iafflight$neutralizeRiddenVerticalThrottle(Vec3 instance, double x, double y, double z) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.isFlying() || this.iafflight$isTouchingFluid(self)) {
            return instance;
        }
        return instance.add(x, y, z);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$ejectRiderOnWaterContact(CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (self.level().isClientSide) {
            return;
        }
        if (!this.iafflight$isTouchingFluid(self)) {
            return;
        }
        if (!self.getPassengers().isEmpty()) {
            self.ejectPassengers();
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$groundedRegen(CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (self.isFlying() && !this.iafflight$isTouchingFluid(self)) {
            return;
        }
        float regened = Math.min(
                AmphithereFlightConfig.MAX_STAMINA,
                this.iafflight$getStamina() + AmphithereFlightConfig.STAMINA_REGEN_GROUNDED
        );
        this.iafflight$setStamina(regened);
        this.iafflight$updateGliding(regened);
    }

    @Inject(method = "positionRider", at = @At("TAIL"))
    private void iafflight$fixFlyingSeatPosition(Entity passenger, Entity.MoveFunction callback, CallbackInfo ci) {
        AmphithereEntity self = (AmphithereEntity) (Object) this;
        if (!self.hasPassenger(passenger) || !self.isFlying()) {
            return;
        }
        float pitchForward = (self.getXRot() / 45F) * 0.45F;
        float scaledGround = self.groundProgress * 0.1F;
        float radius = (self.isTame() ? 0.5F : 0.3F) - scaledGround * 0.5F + pitchForward;
        float angle = 0.01745329251F * self.yBodyRot;
        double extraX = radius * Mth.sin((float) (Math.PI + angle));
        double extraZ = radius * Mth.cos(angle);
        passenger.setPos(
                self.getX() + extraX,
                self.getY() + 0.7F - scaledGround * 0.14F + pitchForward,
                self.getZ() + extraZ
        );
    }
}
