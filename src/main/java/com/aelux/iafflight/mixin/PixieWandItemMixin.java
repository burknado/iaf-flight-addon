package com.aelux.iafflight.mixin;

import com.aelux.iafflight.legendary.LegendaryItemsConfig;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.iafenvoy.iceandfire.item.PixieWandItem")
public abstract class PixieWandItemMixin {
    @Redirect(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemCooldowns;addCooldown(Lnet/minecraft/world/item/Item;I)V")
    )
    private void iafflight$configurableCooldown(ItemCooldowns instance, Item item, int ticks) {
        instance.addCooldown(item, LegendaryItemsConfig.PIXIE_WAND_COOLDOWN_TICKS);
    }
}
