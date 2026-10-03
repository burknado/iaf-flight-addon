package com.aelux.iafflight.siren;

import com.aelux.iafflight.IafFlightAddon;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class IafFlightBiomeModifiers {
    private IafFlightBiomeModifiers() {
    }

    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> REGISTRY =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, IafFlightAddon.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<SirenSpawnBiomeModifier>> SIREN_SPAWNS =
            REGISTRY.register("siren_spawns", () -> SirenSpawnBiomeModifier.CODEC);
}
