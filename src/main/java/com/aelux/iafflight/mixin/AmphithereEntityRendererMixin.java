package com.aelux.iafflight.mixin;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import com.iafenvoy.iceandfire.render.entity.AmphithereEntityRenderer;
import com.iafenvoy.iceandfire.render.model.AmphithereModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.aelux.iafflight.render.AmphithereRiderFeatureRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AmphithereEntityRenderer.class)
public abstract class AmphithereEntityRendererMixin extends MobRenderer<AmphithereEntity, AmphithereModel> {
    protected AmphithereEntityRendererMixin(EntityRendererProvider.Context context, AmphithereModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void iafflight$registerRiderRenderer(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new AmphithereRiderFeatureRenderer(this));
    }

    @Inject(method = "scale", at = @At("HEAD"))
    private void iafflight$tiltWhileFlying(AmphithereEntity entity, PoseStack matrixStackIn, float partialTickTime, CallbackInfo ci) {
        if (entity.isFlying() && !entity.isInWater() && !entity.isInLava()) {
            float pitch = Mth.lerp(partialTickTime, entity.xRotO, entity.getXRot());
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(pitch));
        }
    }
}
