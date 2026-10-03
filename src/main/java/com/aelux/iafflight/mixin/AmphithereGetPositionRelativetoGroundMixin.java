package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AmphithereEntity.class)
public abstract class AmphithereGetPositionRelativetoGroundMixin {
    @Inject(method = "getPositionRelativetoGround", at = @At("RETURN"), cancellable = true)
    private static void iafflight$avoidWaterSurface(
            Entity entity, Level world, int x, int z, RandomSource rand, CallbackInfoReturnable<BlockPos> cir
    ) {
        if (entity.isInWater() || entity.isInLava()) {
            cir.setReturnValue(cir.getReturnValue().above(10));
        }
    }
}
