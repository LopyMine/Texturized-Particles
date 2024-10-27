package net.lopymine.tp;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.*;

import net.fabricmc.api.ModInitializer;

import net.lopymine.tp.manager.TexturizedParticleManager;

public class TexturizedParticles implements ModInitializer {

	public static final String MOD_NAME = /*$ mod_name*/ "Texturized Particles";
	public static final String MOD_ID = /*$ mod_id*/ "texturized-particles";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static Text text(String path, Object... args) {
		return Text.translatable(String.format("%s.%s", MOD_ID, path), args);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("{} Initialized", MOD_NAME);
		TexturizedParticleManager.onInitialize();
	}
}