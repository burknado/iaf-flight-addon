package com.aelux.iafflight.client;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import com.aelux.iafflight.IafFlightAddon;
import com.aelux.iafflight.render.AmphithereRiderFeatureRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = IafFlightAddon.MOD_ID, value = Dist.CLIENT)
public final class IafFlightClient {
    private IafFlightClient() {
    }

    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(
                VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(IafFlightAddon.MOD_ID, "amphithere_stamina"),
                new StaminaOverlay()
        );
    }

    @SubscribeEvent
    public static void disablePlayerRenderWhenNeed(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (player.getVehicle() instanceof AmphithereEntity && player instanceof LocalPlayer
                && (Minecraft.getInstance().options.getCameraType().isFirstPerson()
                || !AmphithereRiderFeatureRenderer.RENDERING_RIDERS.contains(player))) {
            event.setCanceled(true);
        }
        if (player instanceof RemotePlayer && player.getVehicle() instanceof AmphithereEntity
                && !AmphithereRiderFeatureRenderer.RENDERING_RIDERS.contains(player)) {
            event.setCanceled(true);
        }
    }
}
