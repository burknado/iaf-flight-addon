package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.registry.IafArmorMaterials;
import com.aelux.iafflight.armor.ArmorConfig;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(targets = "com.iafenvoy.iceandfire.registry.IafArmorMaterials")
public abstract class IafArmorMaterialsMixin {
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/registry/IafArmorMaterials;register(Ljava/lang/String;[IILnet/minecraft/core/Holder;FLjava/util/function/Supplier;)Lnet/neoforged/neoforge/registries/DeferredHolder;")
    )
    private static DeferredHolder<ArmorMaterial, ArmorMaterial> iafflight$configurableProtectionValues(
            String name, int[] damageReduction, int enchantability, Holder<SoundEvent> sound, float toughness, Supplier<Ingredient> repairIngredients
    ) {
        int[] protection = switch (name) {
            case "copper" -> ArmorConfig.COPPER_PROTECTION;
            case "silver" -> ArmorConfig.SILVER_PROTECTION;
            case "deathworm_yellow", "deathworm_white", "deathworm_red" -> ArmorConfig.DEATHWORM_PROTECTION;
            default -> name.startsWith("troll_") ? ArmorConfig.TROLL_PROTECTION : damageReduction;
        };
        float configuredToughness = switch (name) {
            case "deathworm_yellow", "deathworm_white", "deathworm_red" -> ArmorConfig.DEATHWORM_TOUGHNESS;
            default -> name.startsWith("troll_") ? ArmorConfig.TROLL_TOUGHNESS : toughness;
        };
        return IafArmorMaterials.register(name, protection, enchantability, sound, configuredToughness, repairIngredients);
    }
}
