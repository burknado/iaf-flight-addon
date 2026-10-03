package com.aelux.iafflight;

import com.aelux.iafflight.siren.IafFlightBiomeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(IafFlightAddon.MOD_ID)
public class IafFlightAddon {
    public static final String MOD_ID = "iafflight";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public IafFlightAddon(IEventBus modEventBus) {
        IafFlightBiomeModifiers.REGISTRY.register(modEventBus);
    }
}
