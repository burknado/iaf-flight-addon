package com.aelux.iafflight.mixin;

import com.aelux.iafflight.legendary.LegendaryItemsConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.iafenvoy.iceandfire.item.HydraHeartItem")
public abstract class HydraHeartItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"), cancellable = true)
    private void iafflight$nerfedRegenBrackets(ItemStack stack, Level world, Entity entity, int itemSlot, boolean isSelected, CallbackInfo ci) {
        ci.cancel();
        if (!(entity instanceof Player player) || itemSlot < 0 || itemSlot > 8) {
            return;
        }
        double healthPercentage = player.getHealth() / Math.max(1, player.getMaxHealth());
        if (healthPercentage >= 1.0D) {
            return;
        }
        int level;
        if (healthPercentage < 0.25D) level = 2;
        else if (healthPercentage < 0.5D) level = 1;
        else if (healthPercentage < 0.75D) level = 0;
        else return;

        if (!player.hasEffect(MobEffects.REGENERATION) || player.getEffect(MobEffects.REGENERATION).getAmplifier() < level) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, LegendaryItemsConfig.HYDRA_HEART_REGEN_DURATION_TICKS, level, true, false));
        }
    }
}
