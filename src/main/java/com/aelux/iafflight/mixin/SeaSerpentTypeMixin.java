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

@Mixin(targets = "com.iafenvoy.iceandfire.data.SeaSerpentType")
public abstract class SeaSerpentTypeMixin {
    @Redirect(
            method = "initArmors",
            at = @At(value = "INVOKE", target = "Lcom/iafenvoy/iceandfire/registry/IafArmorMaterials;register(Ljava/lang/String;[IILnet/minecraft/core/Holder;FLjava/util/function/Supplier;)Lnet/neoforged/neoforge/registries/DeferredHolder;")
    )
    private static DeferredHolder<ArmorMaterial, ArmorMaterial> iafflight$configurableTideGuardianProtection(
            String name, int[] damageReduction, int enchantability, Holder<SoundEvent> sound, float toughness, Supplier<Ingredient> repairIngredients
    ) {
        return IafArmorMaterials.register(name, ArmorConfig.TIDE_GUARDIAN_PROTECTION, enchantability, sound, ArmorConfig.TIDE_GUARDIAN_TOUGHNESS, repairIngredients);
    }
}
