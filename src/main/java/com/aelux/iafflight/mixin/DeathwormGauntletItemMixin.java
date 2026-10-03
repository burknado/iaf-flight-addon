package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.registry.IafDataComponents;
import com.iafenvoy.iceandfire.registry.IafSounds;
import com.aelux.iafflight.legendary.LegendaryItemsConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.iafenvoy.iceandfire.item.DeathwormGauntletItem")
public abstract class DeathwormGauntletItemMixin {
    @Inject(method = "finishUsingItem", at = @At("HEAD"), cancellable = true)
    private void iafflight$configurableRangeAndHitboxTargeting(ItemStack stack, Level world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (user instanceof Player player) {
            Vec3 eyePos = player.getEyePosition(1.0F);
            Vec3 lookVec = player.getViewVector(1.0F);
            double range = LegendaryItemsConfig.DEATHWORM_GAUNTLET_RANGE;
            Vec3 rayEnd = eyePos.add(lookVec.scale(range));
            for (LivingEntity livingEntity : world.getEntitiesOfClass(
                    LivingEntity.class,
                    new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range),
                    entity -> entity != player
            )) {
                boolean hitByRay = livingEntity.getBoundingBox().clip(eyePos, rayEnd).isPresent();
                if (hitByRay && player.hasLineOfSight(livingEntity)) {
                    livingEntity.hurt(world.damageSources().playerAttack(player), 3F);
                    double distance = new Vec3(livingEntity.getX() - player.getX(), livingEntity.getY() - player.getY(), livingEntity.getZ() - player.getZ()).length();

                    double t = Math.min(1.0D, distance / range);
                    float knockbackStrength = (float) (LegendaryItemsConfig.DEATHWORM_GAUNTLET_KNOCKBACK_MIN
                            + (LegendaryItemsConfig.DEATHWORM_GAUNTLET_KNOCKBACK_MAX - LegendaryItemsConfig.DEATHWORM_GAUNTLET_KNOCKBACK_MIN) * t);
                    livingEntity.knockback(knockbackStrength, livingEntity.getX() - player.getX(), livingEntity.getZ() - player.getZ());
                }
            }
            player.getCooldowns().addCooldown(stack.getItem(), 20);
        }
        user.playSound(IafSounds.DEATHWORM_ATTACK.get(), 1F, 1F);
        stack.set(IafDataComponents.USER_ID.get(), -1);
        cir.setReturnValue(stack);
    }
}
