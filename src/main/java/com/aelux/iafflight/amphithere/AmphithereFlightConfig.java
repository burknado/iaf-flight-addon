package com.aelux.iafflight.amphithere;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class AmphithereFlightConfig {
    private AmphithereFlightConfig() {
    }

    private static final IafFlightJsonConfig.Amphithere DATA = IafFlightConfigLoader.DATA.amphithere;

    public static final float CONSTANT_FLIGHT_SPEED = DATA.constantFlightSpeed;

    public static final float MAX_STAMINA = DATA.maxStamina;

    public static final float STAMINA_DRAIN_IDLE_PER_TICK = DATA.staminaDrainIdlePerTick;

    public static final float STAMINA_DRAIN_PER_CLIMB_UNIT = DATA.staminaDrainPerClimbUnit;

    public static final float STAMINA_REFUND_PER_FALL_UNIT = STAMINA_DRAIN_IDLE_PER_TICK / CONSTANT_FLIGHT_SPEED;

    public static final float STAMINA_REGEN_GROUNDED = DATA.staminaRegenGrounded;

    public static final float GLIDE_ENTER_STAMINA = DATA.glideEnterStamina;

    public static final float FORCED_GLIDE_ANGLE_DEGREES = DATA.forcedGlideAngleDegrees;

    public static final float FORCED_CLIMB_ANGLE_DEGREES = DATA.forcedClimbAngleDegrees;

    public static final float WANDER_MIN_DISTANCE = DATA.wanderMinDistance;
    public static final float WANDER_MAX_DISTANCE = DATA.wanderMaxDistance;

    public static final int WANDER_MIN_ALTITUDE = DATA.wanderMinAltitude;
    public static final int WANDER_MAX_ALTITUDE = DATA.wanderMaxAltitude;

    public static final float IGNORE_TAKEOFF_INPUT_PITCH_DEGREES = DATA.ignoreTakeoffInputPitchDegrees;
}
