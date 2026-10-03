package com.aelux.iafflight.legendary;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class LegendaryItemsConfig {
    private LegendaryItemsConfig() {
    }

    private static final IafFlightJsonConfig.LegendaryItems DATA = IafFlightConfigLoader.DATA.legendaryItems;

    public static final int PIXIE_WAND_COOLDOWN_TICKS = DATA.pixieWandCooldownTicks;
    public static final int PIXIE_CHARGE_EFFECT_DURATION_TICKS = DATA.pixieChargeEffectDurationTicks;

    public static final int HYDRA_HEART_REGEN_DURATION_TICKS = DATA.hydraHeartRegenDurationTicks;

    public static final double DEATHWORM_GAUNTLET_RANGE = DATA.deathwormGauntletRange;

    public static final float DEATHWORM_GAUNTLET_KNOCKBACK_MIN = DATA.deathwormGauntletKnockbackMin;
    public static final float DEATHWORM_GAUNTLET_KNOCKBACK_MAX = DATA.deathwormGauntletKnockbackMax;

    public static final float FEATHER_BUNDLE_DAMAGE = DATA.featherBundleDamage;
}
