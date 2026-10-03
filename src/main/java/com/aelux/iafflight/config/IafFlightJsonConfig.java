package com.aelux.iafflight.config;

public class IafFlightJsonConfig {
    public Amphithere amphithere = new Amphithere();
    public Hippogryph hippogryph = new Hippogryph();
    public Dragon dragon = new Dragon();
    public LegendaryItems legendaryItems = new LegendaryItems();
    public Armor armor = new Armor();
    public SeaSerpent seaSerpent = new SeaSerpent();
    public Siren siren = new Siren();

    public static class Amphithere {
        public float constantFlightSpeed = 0.8F;
        public float maxStamina = 200.0F;
        public float staminaDrainIdlePerTick = 0.25F;
        public float staminaDrainPerClimbUnit = 2.0F;

        public float staminaRegenGrounded = 4.0F;
        public float glideEnterStamina = 0.0F;
        public float forcedGlideAngleDegrees = 30.0F;
        public float forcedClimbAngleDegrees = 45.0F;
        public float wanderMinDistance = 15.0F;
        public float wanderMaxDistance = 30.0F;
        public int wanderMinAltitude = 8;
        public int wanderMaxAltitude = 20;
        public float ignoreTakeoffInputPitchDegrees = 20.0F;
    }

    public static class Hippogryph {
        public float maxStamina = 200.0F;
        public float staminaDrainIdlePerTick = 0.25F;
        public float staminaDrainPerClimbUnit = 2.0F;
        public float staminaRefundPerFallUnit = 1.0F;
        public float staminaRegenGrounded = 4.0F;
    }

    public static class Dragon {
        public float staminaPerStage = 100.0F;
        public float staminaDrainIdlePerTick = 0.25F;
        public float staminaDrainPerClimbUnit = 2.0F;
        public float staminaRefundPerFallUnit = 1.0F;
        public float staminaRegenGrounded = 4.0F;
        public int minStageToBreakBlocks = 3;

        public int caveYOffset = 0;
    }

    public static class LegendaryItems {
        public int pixieWandCooldownTicks = 600;
        public int pixieChargeEffectDurationTicks = 60;
        public int hydraHeartRegenDurationTicks = 100;
        public double deathwormGauntletRange = 24.0D;
        public float deathwormGauntletKnockbackMin = 1.0F;
        public float deathwormGauntletKnockbackMax = 3.0F;
        public float featherBundleDamage = 1.0F;
    }

    public static class Armor {
        public boolean dragonScaleBreathReductionEnabled = true;
        public boolean dragonSteelBreathReductionEnabled = true;
        public boolean trollProjectileReductionEnabled = true;
        public boolean tideGuardianEffectsEnabled = true;

        public boolean cosmeticArmorUnbreakable = false;

        public int[] dragonScaleProtection = {5, 7, 9, 5};
        public int[] trollProtection = {2, 5, 7, 3};
        public int[] copperProtection = {1, 3, 4, 2};
        public int[] silverProtection = {1, 4, 5, 2};
        public int[] deathwormProtection = {2, 5, 7, 3};

        public int[] tideGuardianProtection = {4, 7, 8, 4};

        public float dragonScaleToughness = 2.0F;
        public float trollToughness = 1.0F;
        public float deathwormToughness = 1.5F;
        public float tideGuardianToughness = 2.5F;
    }

    public static class SeaSerpent {
        public float normalModifier = 1.25F;
        public float ancientModifier = 1.5F;
    }

    public static class Siren {
        public boolean beachSpawnsEnabled = true;
        public int spawnWeight = 10;
        public int minGroupSize = 1;
        public int maxGroupSize = 1;
        public double minPlayerDistance = 48.0D;
        public boolean despawnAtMorning = true;
    }
}
