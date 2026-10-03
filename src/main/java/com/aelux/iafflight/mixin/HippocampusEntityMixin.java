package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.HippocampusEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HippocampusEntity.class)
public abstract class HippocampusEntityMixin {
    @Inject(method = "onInsideBubbleColumn", at = @At("HEAD"), cancellable = true)
    private void iafflight$immuneToBubbleColumns(boolean downwards, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onAboveBubbleCol", at = @At("HEAD"), cancellable = true)
    private void iafflight$immuneToBubbleColumnsAtSurface(boolean downwards, CallbackInfo ci) {
        ci.cancel();
    }
}
