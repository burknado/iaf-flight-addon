package com.aelux.iafflight.hippogryph;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class HippogryphFlightConfig {
    private HippogryphFlightConfig() {
    }

    private static final IafFlightJsonConfig.Hippogryph DATA = IafFlightConfigLoader.DATA.hippogryph;

    public static final float MAX_STAMINA = DATA.maxStamina;

    public static final float STAMINA_DRAIN_IDLE_PER_TICK = DATA.staminaDrainIdlePerTick;

    public static final float STAMINA_DRAIN_PER_CLIMB_UNIT = DATA.staminaDrainPerClimbUnit;

    public static final float STAMINA_REFUND_PER_FALL_UNIT = DATA.staminaRefundPerFallUnit;

    public static final float STAMINA_REGEN_GROUNDED = DATA.staminaRegenGrounded;
}
