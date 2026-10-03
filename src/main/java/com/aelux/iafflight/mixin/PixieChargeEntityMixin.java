package com.aelux.iafflight.mixin;

import com.aelux.iafflight.legendary.LegendaryItemsConfig;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.iafenvoy.iceandfire.entity.PixieChargeEntity")
public abstract class PixieChargeEntityMixin {
    @Redirect(
            method = "onHit",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z")
    )
    private boolean iafflight$configurableEffectDuration(LivingEntity instance, MobEffectInstance original) {
        MobEffectInstance withConfigDuration = new MobEffectInstance(
                original.getEffect(),
                LegendaryItemsConfig.PIXIE_CHARGE_EFFECT_DURATION_TICKS,
                original.getAmplifier(),
                original.isAmbient(),
                original.isVisible()
        );
        return instance.addEffect(withConfigDuration);
    }
}
