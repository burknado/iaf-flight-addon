package com.aelux.iafflight.siren;

import com.iafenvoy.iceandfire.registry.IafEntities;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

public record SirenSpawnBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {
    public static final MapCodec<SirenSpawnBiomeModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(SirenSpawnBiomeModifier::biomes)
    ).apply(instance, SirenSpawnBiomeModifier::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !this.biomes.contains(biome)) {
            return;
        }
        if (!SirenConfig.BEACH_SPAWNS_ENABLED || SirenConfig.SPAWN_WEIGHT <= 0) {
            return;
        }
        int min = Math.max(1, SirenConfig.MIN_GROUP_SIZE);
        int max = Math.max(min, SirenConfig.MAX_GROUP_SIZE);
        var type = IafEntities.SIREN.get();
        builder.getMobSpawnSettings().addSpawn(type.getCategory(), new MobSpawnSettings.SpawnerData(type, SirenConfig.SPAWN_WEIGHT, min, max));
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return IafFlightBiomeModifiers.SIREN_SPAWNS.get();
    }
}
