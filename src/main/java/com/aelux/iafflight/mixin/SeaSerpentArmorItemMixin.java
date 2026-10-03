package com.aelux.iafflight.mixin;

import com.aelux.iafflight.armor.ArmorConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Unbreakable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(targets = "com.iafenvoy.iceandfire.item.armor.SeaSerpentArmorItem")
public abstract class SeaSerpentArmorItemMixin {
    @Redirect(
            method = "inventoryTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z")
    )
    private boolean iafflight$conditionalEffects(Player instance, MobEffectInstance effect) {
        if (!ArmorConfig.TIDE_GUARDIAN_EFFECTS_ENABLED) {
            return false;
        }
        return instance.addEffect(effect);
    }

    @Redirect(
            method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1)
    )
    private boolean iafflight$conditionalDescription0(List<Component> tooltip, Object component) {
        if (!ArmorConfig.TIDE_GUARDIAN_EFFECTS_ENABLED) {
            return false;
        }
        return tooltip.add((Component) component);
    }

    @Redirect(
            method = "appendHoverText",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2)
    )
    private boolean iafflight$conditionalDescription1(List<Component> tooltip, Object component) {
        if (!ArmorConfig.TIDE_GUARDIAN_EFFECTS_ENABLED) {
            return false;
        }
        return tooltip.add((Component) component);
    }

    @Redirect(
            method = "<init>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;durability(I)Lnet/minecraft/world/item/Item$Properties;")
    )
    private static Item.Properties iafflight$conditionallyUnbreakable(Item.Properties instance, int durability) {
        Item.Properties properties = instance.durability(ArmorConfig.COSMETIC_ARMOR_UNBREAKABLE ? 1 : durability);
        if (ArmorConfig.COSMETIC_ARMOR_UNBREAKABLE) {
            properties = properties.component(DataComponents.UNBREAKABLE, new Unbreakable(false));
        }
        return properties;
    }
}
