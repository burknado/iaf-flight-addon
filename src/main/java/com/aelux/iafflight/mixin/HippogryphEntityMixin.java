package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.HippogryphEntity;
import com.aelux.iafflight.hippogryph.HippogryphFlightConfig;
import com.aelux.iafflight.hippogryph.IHippogryphFlightData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HippogryphEntity.class)
public abstract class HippogryphEntityMixin extends LivingEntity implements IHippogryphFlightData {
    @Unique
    private static final EntityDataAccessor<Float> IAFFLIGHT$STAMINA =
            SynchedEntityData.defineId(HippogryphEntity.class, EntityDataSerializers.FLOAT);

    @Unique
    private boolean iafflight$gliding;

    protected HippogryphEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void iafflight$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IAFFLIGHT$STAMINA, HippogryphFlightConfig.MAX_STAMINA);
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
                ? stamina < HippogryphFlightConfig.MAX_STAMINA
                : stamina <= 0F;
        this.iafflight$gliding = gliding;
        return gliding;
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void iafflight$staminaBookkeeping(Vec3 travelVector, CallbackInfo ci) {
        HippogryphEntity self = (HippogryphEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (self.onGround() || self.isInWater() || self.isInLava()) {
            return;
        }

        float vertical = (float) self.getDeltaMovement().y;
        float stamina = this.iafflight$getStamina();
        stamina -= HippogryphFlightConfig.STAMINA_DRAIN_IDLE_PER_TICK;
        if (vertical > 0F) {
            stamina -= vertical * HippogryphFlightConfig.STAMINA_DRAIN_PER_CLIMB_UNIT;
        } else if (vertical < 0F) {
            float refund = -vertical * HippogryphFlightConfig.STAMINA_REFUND_PER_FALL_UNIT;
            stamina += Math.min(refund, HippogryphFlightConfig.STAMINA_DRAIN_IDLE_PER_TICK);
        }
        stamina = Math.max(0F, Math.min(HippogryphFlightConfig.MAX_STAMINA, stamina));
        this.iafflight$updateGliding(stamina);
        this.iafflight$setStamina(stamina);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$forcedLandingWhileGliding(CallbackInfo ci) {
        HippogryphEntity self = (HippogryphEntity) (Object) this;

        if (self.onGround() && (this.iafflight$gliding || !self.isGoingUp())) {
            self.setFlying(false);
            self.setHovering(false);
        }
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void iafflight$normalizeMovementVector(Vec3 travelVector, CallbackInfo ci) {
        HippogryphEntity self = (HippogryphEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (!self.isFlying() && !self.isHovering()) {
            return;
        }
        Vec3 motion = self.getDeltaMovement();
        double horizontalMag = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        double verticalMag = Math.abs(motion.y);
        double targetMag = Math.max(horizontalMag, verticalMag);
        double actualMag = motion.length();
        if (actualMag > targetMag && actualMag > 1.0E-5) {
            self.setDeltaMovement(motion.scale(targetMag / actualMag));
        }
    }

    @Redirect(
            method = "tickRidden",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;")
    )
    private Vec3 iafflight$forceDescentWhenExhausted(Vec3 instance, double x, double y, double z) {
        HippogryphEntity self = (HippogryphEntity) (Object) this;
        if (this.iafflight$gliding && (self.isFlying() || self.isHovering())) {
            return instance.add(0D, -0.2D, 0D);
        }
        return instance.add(x, y, z);
    }

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/HippogryphEntity;isGoingDown()Z")
    )
    private boolean iafflight$alwaysAllowLandingCheck(HippogryphEntity self) {
        return true;
    }

    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I")
    )
    private int iafflight$disableRandomSkyLaunch(RandomSource instance, int bound) {
        return -1;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$groundedRegen(CallbackInfo ci) {
        HippogryphEntity self = (HippogryphEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (self.isFlying() || self.isHovering()) {
            return;
        }
        float regened = Math.min(
                HippogryphFlightConfig.MAX_STAMINA,
                this.iafflight$getStamina() + HippogryphFlightConfig.STAMINA_REGEN_GROUNDED
        );
        this.iafflight$setStamina(regened);
        this.iafflight$updateGliding(regened);
    }
}
