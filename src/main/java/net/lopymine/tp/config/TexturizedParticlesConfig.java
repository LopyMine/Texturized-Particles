package net.lopymine.tp.config;

import com.google.gson.*;
import lombok.*;
import org.slf4j.*;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.tp.TexturizedParticles;
import net.lopymine.tp.client.TexturizedParticlesClient;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class TexturizedParticlesConfig {

	public static final Codec<TexturizedParticlesConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.BOOL.fieldOf("mod_enabled").forGetter(TexturizedParticlesConfig::isModEnabled)
	).apply(instance, TexturizedParticlesConfig::new));

	private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(TexturizedParticles.MOD_ID + ".json5").toFile();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(TexturizedParticles.MOD_NAME + "/Config");
	private boolean modEnabled;

	public TexturizedParticlesConfig() {
		this.modEnabled = true;
	}

	public TexturizedParticlesConfig(boolean modEnabled) {
		this.modEnabled = modEnabled;
	}

	public static TexturizedParticlesConfig getInstance() {
		return TexturizedParticlesConfig.read();
	}

	private static @NotNull TexturizedParticlesConfig create() {
		TexturizedParticlesConfig config = new TexturizedParticlesConfig();
		try (FileWriter writer = new FileWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
			String json = GSON.toJson(CODEC.encode(config, JsonOps.INSTANCE, JsonOps.INSTANCE.empty())/*? if >=1.20.5 {*/.getOrThrow());/*?} else*//*.getOrThrow(false, LOGGER::error));*/
			writer.write(json);
		} catch (Exception e) {
			LOGGER.error("Failed to create config", e);
		}
		return config;
	}

	private static TexturizedParticlesConfig read() {
		if (!CONFIG_FILE.exists()) {
			return TexturizedParticlesConfig.create();
		}

		try (FileReader reader = new FileReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
			return CODEC.decode(JsonOps.INSTANCE, JsonParser.parseReader(reader))/*? if >=1.20.5 {*/.getOrThrow()/*?} else {*//*.getOrThrow(false, LOGGER::error)*//*?}*/.getFirst();
		} catch (Exception e) {
			LOGGER.error("Failed to read config", e);
		}
		return TexturizedParticlesConfig.create();
	}

	public void save() {
		TexturizedParticlesClient.setConfig(this);
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
