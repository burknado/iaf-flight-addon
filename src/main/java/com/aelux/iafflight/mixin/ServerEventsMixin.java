package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.item.armor.DragonScaleArmorItem;
import com.iafenvoy.iceandfire.item.armor.DragonSteelArmorItem;
import com.iafenvoy.iceandfire.item.armor.TrollArmorItem;
import com.iafenvoy.iceandfire.registry.IafDamageTypes;
import com.aelux.iafflight.armor.ArmorConfig;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.iafenvoy.iceandfire.event.handler.ServerEvents")
public abstract class ServerEventsMixin {
    @Inject(method = "onEntityDamage", at = @At("HEAD"), cancellable = true)
    private static void iafflight$configurableArmorSetEffects(LivingDamageEvent.Pre event, CallbackInfo ci) {
        ci.cancel();
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getNewDamage();

        if (source.is(DamageTypeTags.IS_PROJECTILE) && ArmorConfig.TROLL_PROJECTILE_REDUCTION_ENABLED) {
            float multi = 1;
            if (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof TrollArmorItem)
                multi -= 0.1f;
            if (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof TrollArmorItem)
                multi -= 0.3f;
            if (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof TrollArmorItem)
                multi -= 0.2f;
            if (entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof TrollArmorItem)
                multi -= 0.1f;
            amount *= multi;
        }
        if (source.is(IafDamageTypes.DRAGON_FIRE_TYPE) || source.is(IafDamageTypes.DRAGON_ICE_TYPE) || source.is(IafDamageTypes.DRAGON_LIGHTNING_TYPE)) {
            float multi = 1;
            if ((entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof DragonScaleArmorItem && ArmorConfig.DRAGON_SCALE_BREATH_REDUCTION_ENABLED) ||
                    (entity.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof DragonSteelArmorItem && ArmorConfig.DRAGON_STEEL_BREATH_REDUCTION_ENABLED))
                multi -= 0.1f;
            if ((entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonScaleArmorItem && ArmorConfig.DRAGON_SCALE_BREATH_REDUCTION_ENABLED) ||
                    (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof DragonSteelArmorItem && ArmorConfig.DRAGON_STEEL_BREATH_REDUCTION_ENABLED))
                multi -= 0.3f;
            if ((entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof DragonScaleArmorItem && ArmorConfig.DRAGON_SCALE_BREATH_REDUCTION_ENABLED) ||
                    (entity.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof DragonSteelArmorItem && ArmorConfig.DRAGON_STEEL_BREATH_REDUCTION_ENABLED))
                multi -= 0.2f;
            if ((entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof DragonScaleArmorItem && ArmorConfig.DRAGON_SCALE_BREATH_REDUCTION_ENABLED) ||
                    (entity.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof DragonSteelArmorItem && ArmorConfig.DRAGON_STEEL_BREATH_REDUCTION_ENABLED))
                multi -= 0.1f;
            amount *= multi;
        }
        event.setNewDamage(amount);
    }
}
