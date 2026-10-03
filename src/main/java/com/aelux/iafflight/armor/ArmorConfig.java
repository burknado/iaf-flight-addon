package com.aelux.iafflight.armor;

import com.aelux.iafflight.config.IafFlightConfigLoader;
import com.aelux.iafflight.config.IafFlightJsonConfig;

public final class ArmorConfig {
    private ArmorConfig() {
    }

    private static final IafFlightJsonConfig.Armor DATA = IafFlightConfigLoader.DATA.armor;

    public static final boolean DRAGON_SCALE_BREATH_REDUCTION_ENABLED = DATA.dragonScaleBreathReductionEnabled;
    public static final boolean DRAGON_STEEL_BREATH_REDUCTION_ENABLED = DATA.dragonSteelBreathReductionEnabled;
    public static final boolean TROLL_PROJECTILE_REDUCTION_ENABLED = DATA.trollProjectileReductionEnabled;
    public static final boolean TIDE_GUARDIAN_EFFECTS_ENABLED = DATA.tideGuardianEffectsEnabled;
    public static final boolean COSMETIC_ARMOR_UNBREAKABLE = DATA.cosmeticArmorUnbreakable;

    public static final int[] DRAGON_SCALE_PROTECTION = DATA.dragonScaleProtection;

    public static final int[] TROLL_PROTECTION = DATA.trollProtection;

    public static final int[] COPPER_PROTECTION = DATA.copperProtection;

    public static final int[] SILVER_PROTECTION = DATA.silverProtection;

    public static final int[] DEATHWORM_PROTECTION = DATA.deathwormProtection;

    public static final int[] TIDE_GUARDIAN_PROTECTION = DATA.tideGuardianProtection;

    public static final float DRAGON_SCALE_TOUGHNESS = DATA.dragonScaleToughness;
    public static final float TROLL_TOUGHNESS = DATA.trollToughness;
    public static final float DEATHWORM_TOUGHNESS = DATA.deathwormToughness;
    public static final float TIDE_GUARDIAN_TOUGHNESS = DATA.tideGuardianToughness;
}
