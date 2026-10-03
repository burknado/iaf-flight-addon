package com.aelux.iafflight.client;

import com.iafenvoy.iceandfire.entity.AmphithereEntity;
import com.iafenvoy.iceandfire.entity.DragonBaseEntity;
import com.iafenvoy.iceandfire.entity.HippogryphEntity;
import com.aelux.iafflight.amphithere.AmphithereFlightConfig;
import com.aelux.iafflight.amphithere.IAmphithereFlightData;
import com.aelux.iafflight.dragon.IDragonFlightData;
import com.aelux.iafflight.hippogryph.HippogryphFlightConfig;
import com.aelux.iafflight.hippogryph.IHippogryphFlightData;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public final class StaminaOverlay implements LayeredDraw.Layer {
    private static final ResourceLocation JUMP_BAR_BACKGROUND = ResourceLocation.withDefaultNamespace("hud/jump_bar_background");
    private static final ResourceLocation JUMP_BAR_PROGRESS = ResourceLocation.withDefaultNamespace("hud/jump_bar_progress");

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        Entity vehicle = mc.player.getVehicle();

        float stamina;
        float maxStamina;

        if (vehicle instanceof AmphithereEntity amphithere && amphithere instanceof IAmphithereFlightData flightData) {
            stamina = flightData.iafflight$getStamina();
            maxStamina = AmphithereFlightConfig.MAX_STAMINA;
        } else if (vehicle instanceof HippogryphEntity hippogryph && hippogryph instanceof IHippogryphFlightData flightData) {
            stamina = flightData.iafflight$getStamina();
            maxStamina = HippogryphFlightConfig.MAX_STAMINA;
        } else if (vehicle instanceof DragonBaseEntity dragon && dragon instanceof IDragonFlightData flightData) {
            stamina = flightData.iafflight$getStamina();
            maxStamina = flightData.iafflight$getMaxStamina();
        } else {
            return;
        }

        float pct = Math.max(0F, Math.min(1F, stamina / maxStamina));

        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();
        int barWidth = 182;
        int barHeight = 5;

        int x = screenWidth / 2 - 91;
        int y = screenHeight - 25;

        graphics.blitSprite(JUMP_BAR_BACKGROUND, x, y, barWidth, barHeight);

        int fillWidth = Math.round(barWidth * pct);
        if (fillWidth > 0) {
            graphics.enableScissor(x, y, x + fillWidth, y + barHeight);
            graphics.blitSprite(JUMP_BAR_PROGRESS, x, y, barWidth, barHeight);
            graphics.disableScissor();
        }
    }
}
