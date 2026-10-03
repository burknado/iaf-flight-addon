package com.aelux.iafflight.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.aelux.iafflight.IafFlightAddon;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class IafFlightConfigLoader {
    private IafFlightConfigLoader() {
    }

    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve(IafFlightAddon.MOD_ID + ".json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final IafFlightJsonConfig DATA = load();

    private static IafFlightJsonConfig load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                IafFlightJsonConfig loaded = GSON.fromJson(reader, IafFlightJsonConfig.class);
                if (loaded != null) {
                    return loaded;
                }
                IafFlightAddon.LOGGER.warn("{} parsed to null (empty or malformed?), using defaults instead", CONFIG_PATH);
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                IafFlightAddon.LOGGER.warn("Failed to read {}, using defaults instead", CONFIG_PATH, e);
            }
        }
        IafFlightJsonConfig defaults = new IafFlightJsonConfig();
        write(defaults);
        return defaults;
    }

    private static void write(IafFlightJsonConfig data) {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH, StandardCharsets.UTF_8)) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            IafFlightAddon.LOGGER.warn("Failed to write default config to {}", CONFIG_PATH, e);
        }
    }
}
