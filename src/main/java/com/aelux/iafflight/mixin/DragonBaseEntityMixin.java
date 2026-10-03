package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.data.DragonType;
import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.aelux.iafflight.dragon.DragonFlightConfig;
import com.aelux.iafflight.dragon.IDragonFlightData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonBaseEntity.class)
public abstract class DragonBaseEntityMixin extends LivingEntity implements IDragonFlightData {
    @Unique
    private static final EntityDataAccessor<Float> IAFFLIGHT$STAMINA =
            SynchedEntityData.defineId(DragonBaseEntity.class, EntityDataSerializers.FLOAT);

    @Unique
    private boolean iafflight$gliding;

    protected DragonBaseEntityMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void iafflight$avoidWaterPathfinding(
            EntityType<? extends DragonBaseEntity> t, Level world, DragonType type,
            double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth,
            double minimumSpeed, double maximumSpeed, CallbackInfo ci
    ) {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        self.setPathfindingMalus(PathType.WATER, 8.0F);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void iafflight$defineSynchedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IAFFLIGHT$STAMINA, DragonFlightConfig.STAMINA_PER_STAGE);
    }

    @Override
    public float iafflight$getMaxStamina() {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        return self.getDragonStage() * DragonFlightConfig.STAMINA_PER_STAGE;
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
                ? stamina < this.iafflight$getMaxStamina()
                : stamina <= 0F;
        this.iafflight$gliding = gliding;
        return gliding;
    }

    @Inject(method = "travel", at = @At("TAIL"))
    private void iafflight$staminaBookkeeping(Vec3 travelVector, CallbackInfo ci) {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if (self.onGround() || self.isInWater() || self.isInLava()) {
            return;
        }

        float vertical = (float) self.getDeltaMovement().y;
        float stamina = this.iafflight$getStamina();
        stamina -= DragonFlightConfig.STAMINA_DRAIN_IDLE_PER_TICK;
        if (vertical > 0F) {
            stamina -= vertical * DragonFlightConfig.STAMINA_DRAIN_PER_CLIMB_UNIT;
        } else if (vertical < 0F) {
            float refund = -vertical * DragonFlightConfig.STAMINA_REFUND_PER_FALL_UNIT;
            stamina += Math.min(refund, DragonFlightConfig.STAMINA_DRAIN_IDLE_PER_TICK);
        }
        stamina = Math.max(0F, Math.min(this.iafflight$getMaxStamina(), stamina));
        this.iafflight$updateGliding(stamina);
        this.iafflight$setStamina(stamina);
    }

    @Redirect(
            method = "travel",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/DragonBaseEntity;isGoingDown()Z")
    )
    private boolean iafflight$forceGoingDownInTravel(DragonBaseEntity self) {
        return this.iafflight$gliding || self.isGoingDown();
    }

    @Redirect(
            method = "doesWantToLand",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/DragonBaseEntity;isGoingDown()Z")
    )
    private boolean iafflight$forceGoingDownInDoesWantToLand(DragonBaseEntity self) {
        return this.iafflight$gliding || self.isGoingDown();
    }

    @Redirect(
            method = "updateRider",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/entity/DragonBaseEntity;isGoingDown()Z")
    )
    private boolean iafflight$forceGoingDownInUpdateRider(DragonBaseEntity self) {
        return this.iafflight$gliding || self.isGoingDown();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$forcedLandingWhileGliding(CallbackInfo ci) {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        if (this.iafflight$gliding && self.onGround()) {
            self.setFlying(false);
            self.setHovering(false);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void iafflight$groundedRegen(CallbackInfo ci) {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        if (!self.isControlledByLocalInstance()) {
            return;
        }
        if ((self.isFlying() || self.isHovering()) && !self.isInWater() && !self.isInLava()) {
            return;
        }
        float regened = Math.min(
                this.iafflight$getMaxStamina(),
                this.iafflight$getStamina() + DragonFlightConfig.STAMINA_REGEN_GROUNDED
        );
        this.iafflight$setStamina(regened);
        this.iafflight$updateGliding(regened);
    }

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    private void iafflight$blockBreakingRequiresMinStage(BlockPos position, CallbackInfo ci) {
        DragonBaseEntity self = (DragonBaseEntity) (Object) this;
        if (self.getDragonStage() <= DragonFlightConfig.MIN_STAGE_TO_BREAK_BLOCKS) {
            ci.cancel();
        }
    }
}
