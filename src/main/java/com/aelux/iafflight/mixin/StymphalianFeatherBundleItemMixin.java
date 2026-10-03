package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.StymphalianFeatherEntity;
import com.aelux.iafflight.legendary.LegendaryItemsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.iafenvoy.iceandfire.item.StymphalianFeatherBundleItem")
public abstract class StymphalianFeatherBundleItemMixin {
    @Redirect(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
    )
    private boolean iafflight$configurableFeatherDamage(net.minecraft.world.level.Level level, net.minecraft.world.entity.Entity entity) {
        if (entity instanceof StymphalianFeatherEntity feather) {
            feather.setBaseDamage(LegendaryItemsConfig.FEATHER_BUNDLE_DAMAGE);
        }
        return level.addFreshEntity(entity);
    }
}
