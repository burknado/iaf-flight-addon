package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.registry.IafArmorMaterials;
import com.aelux.iafflight.armor.ArmorConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(targets = "com.iafenvoy.iceandfire.registry.IafItems")
public abstract class IafItemsMixin {
    @ModifyArgs(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ArmorItem;<init>(Lnet/minecraft/core/Holder;Lnet/minecraft/world/item/ArmorItem$Type;Lnet/minecraft/world/item/Item$Properties;)V")
    )
    private static void iafflight$conditionallyUnbreakable(Args args) {
        Holder<ArmorMaterial> material = args.get(0);
        boolean tracked = material == IafArmorMaterials.COPPER
                || material == IafArmorMaterials.SILVER
                || material == IafArmorMaterials.DEATHWORM_YELLOW
                || material == IafArmorMaterials.DEATHWORM_WHITE
                || material == IafArmorMaterials.DEATHWORM_RED;
        if (tracked && ArmorConfig.COSMETIC_ARMOR_UNBREAKABLE) {
            Item.Properties properties = args.get(2);

            args.set(2, properties.durability(1).component(DataComponents.UNBREAKABLE, new Unbreakable(false)));
        }
    }
}
