package com.aelux.iafflight.siren;

import com.aelux.iafflight.IafFlightAddon;
import com.iafenvoy.iceandfire.entity.SirenEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = IafFlightAddon.MOD_ID)
public final class SirenMorningDespawn {
    private static final String NIGHT_SPAWN_TAG = "iafflight_night_siren";

    private SirenMorningDespawn() {
    }

    @SubscribeEvent
    public static void onFinalizeSpawn(FinalizeSpawnEvent event) {
        MobSpawnType type = event.getSpawnType();
        boolean worldSpawn = type == MobSpawnType.NATURAL || type == MobSpawnType.CHUNK_GENERATION;
        if (worldSpawn && event.getEntity() instanceof SirenEntity siren) {
            siren.addTag(NIGHT_SPAWN_TAG);
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!SirenConfig.DESPAWN_AT_MORNING) {
            return;
        }
        if (!(event.getEntity() instanceof SirenEntity siren) || siren.level().isClientSide || siren.tickCount % 20 != 0) {
            return;
        }
        if (siren.getTags().contains(NIGHT_SPAWN_TAG)
                && !siren.hasCustomName()
                && !siren.requiresCustomPersistence()
                && siren.level().isDay()) {
            siren.discard();
        }
    }
}
