package com.aelux.iafflight.amphithere;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public final class WanderTargetUtil {
    private WanderTargetUtil() {
    }

    public static int[] randomOffset(RandomSource random) {
        float angle = random.nextFloat() * ((float) Math.PI * 2F);
        float distance = AmphithereFlightConfig.WANDER_MIN_DISTANCE
                + random.nextFloat() * (AmphithereFlightConfig.WANDER_MAX_DISTANCE - AmphithereFlightConfig.WANDER_MIN_DISTANCE);
        int dx = Math.round(Mth.sin(angle) * distance);
        int dz = Math.round(Mth.cos(angle) * distance);
        return new int[]{dx, dz};
    }
}
