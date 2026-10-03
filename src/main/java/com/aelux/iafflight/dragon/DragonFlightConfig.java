package com.aelux.iafflight.dragon;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class DragonFlightConfig {
    private DragonFlightConfig() {
    }

    private static final IafFlightJsonConfig.Dragon DATA = IafFlightConfigLoader.DATA.dragon;

    public static final float STAMINA_PER_STAGE = DATA.staminaPerStage;
    public static final float STAMINA_DRAIN_IDLE_PER_TICK = DATA.staminaDrainIdlePerTick;
    public static final float STAMINA_DRAIN_PER_CLIMB_UNIT = DATA.staminaDrainPerClimbUnit;

    public static final float STAMINA_REFUND_PER_FALL_UNIT = DATA.staminaRefundPerFallUnit;
    public static final float STAMINA_REGEN_GROUNDED = DATA.staminaRegenGrounded;

    public static final int MIN_STAGE_TO_BREAK_BLOCKS = DATA.minStageToBreakBlocks;

    public static final int CAVE_Y_OFFSET = DATA.caveYOffset;
}
