package com.aelux.iafflight.seaserpent;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class SeaSerpentConfig {
    private SeaSerpentConfig() {
    }

    private static final IafFlightJsonConfig.SeaSerpent DATA = IafFlightConfigLoader.DATA.seaSerpent;

    public static final float NORMAL_MODIFIER = DATA.normalModifier;

    public static final float ANCIENT_MODIFIER = DATA.ancientModifier;
}
