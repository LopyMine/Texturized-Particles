package net.lopymine.pe.config;

import com.google.gson.*;
import lombok.*;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.pe.ParticleEffects;
import net.lopymine.pe.client.ParticleEffectsClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class ParticleEffectsConfig {

	public static final Codec<ParticleEffectsConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("mod_enabled").forGetter(ParticleEffectsConfig::isModEnabled),
			Codec.BOOL.optionalFieldOf("debug_log", false).forGetter(ParticleEffectsConfig::isDebugLogEnabled)
	).apply(instance, ParticleEffectsConfig::new));

	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(ParticleEffects.MOD_ID + ".json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEffects.MOD_NAME + "/Config");
	private boolean modEnabled;
	private boolean debugLogEnabled;

	public ParticleEffectsConfig() {
		this.modEnabled = true;
		this.debugLogEnabled = false;
	}

	public static ParticleEffectsConfig getInstance() {
		return ParticleEffectsConfig.read();
	}

	private static @NotNull ParticleEffectsConfig create() {
		ParticleEffectsConfig config = new ParticleEffectsConfig();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static ParticleEffectsConfig read() {
		if (!CONFIG_FILE.exists()) {
			return ParticleEffectsConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader))/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			LOGGER.error("Failed to read config", e);
		}
		return ParticleEffectsConfig.create();
	}

	public void save() {
		ParticleEffects.setConfig(this);
		CompletableFuture.runAsync(() -> {
			try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
				String json = GSON.toJson(CODEC.encode(this, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, LOGGER::error));*/
				writer.write(json);
			} catch (Exception e) {
				LOGGER.error("Failed to save config", e);
			}
		});
	}
}
