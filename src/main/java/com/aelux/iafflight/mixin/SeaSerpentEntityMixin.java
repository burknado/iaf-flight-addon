package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.SeaSerpentEntity;
import com.aelux.iafflight.seaserpent.SeaSerpentConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SeaSerpentEntity.class)
public abstract class SeaSerpentEntityMixin {
    @Inject(method = "getAncientModifier", at = @At("HEAD"), cancellable = true)
    private void iafflight$configurableAncientModifier(CallbackInfoReturnable<Float> cir) {
        SeaSerpentEntity self = (SeaSerpentEntity) (Object) this;
        cir.setReturnValue(self.isAncient() ? SeaSerpentConfig.ANCIENT_MODIFIER : SeaSerpentConfig.NORMAL_MODIFIER);
    }
}
