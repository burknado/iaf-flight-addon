package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import com.aelux.iafflight.amphithere.AmphithereFlightConfig;
import com.aelux.iafflight.amphithere.WanderTargetUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.iafenvoy.iceandfire.entity.AmphithereEntity$AIFlyWander")
public abstract class AmphithereAIFlyWanderMixin {
    @Redirect(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/iafenvoy/iceandfire/entity/AmphithereEntity;getPositionRelativetoGround(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;IILnet/minecraft/util/RandomSource;)Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos iafflight$widerWanderTargetOnUse(Entity entity, Level world, int x, int z, RandomSource random) {
        return iafflight$pickAerialTarget(entity, world, random);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/iafenvoy/iceandfire/entity/AmphithereEntity;getPositionRelativetoGround(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;IILnet/minecraft/util/RandomSource;)Lnet/minecraft/core/BlockPos;"
            )
    )
    private BlockPos iafflight$widerWanderTargetOnTick(Entity entity, Level world, int x, int z, RandomSource random) {
        return iafflight$pickAerialTarget(entity, world, random);
    }

    private static BlockPos iafflight$pickAerialTarget(Entity entity, Level world, RandomSource random) {
        int[] offset = WanderTargetUtil.randomOffset(random);
        BlockPos ground = AmphithereEntity.getPositionRelativetoGround(
                entity, world, entity.getBlockX() + offset[0], entity.getBlockZ() + offset[1], random);
        int altitude = AmphithereFlightConfig.WANDER_MIN_ALTITUDE
                + random.nextInt(AmphithereFlightConfig.WANDER_MAX_ALTITUDE - AmphithereFlightConfig.WANDER_MIN_ALTITUDE + 1);
        return ground.above(altitude);
    }
}
