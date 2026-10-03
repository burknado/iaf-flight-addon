package com.aelux.iafflight.siren;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class SirenConfig {
    private SirenConfig() {
    }

    private static final IafFlightJsonConfig.Siren DATA = IafFlightConfigLoader.DATA.siren;

    public static final boolean BEACH_SPAWNS_ENABLED = DATA.beachSpawnsEnabled;
    public static final int SPAWN_WEIGHT = DATA.spawnWeight;
    public static final int MIN_GROUP_SIZE = DATA.minGroupSize;
    public static final int MAX_GROUP_SIZE = DATA.maxGroupSize;
    public static final double MIN_PLAYER_DISTANCE = DATA.minPlayerDistance;
    public static final boolean DESPAWN_AT_MORNING = DATA.despawnAtMorning;
}
