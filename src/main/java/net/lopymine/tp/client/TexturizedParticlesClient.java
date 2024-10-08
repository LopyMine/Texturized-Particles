package net.lopymine.tp.client;

import lombok.*;
import org.slf4j.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.tp.TexturizedParticles;
import net.lopymine.tp.config.TexturizedParticlesConfig;
import net.lopymine.tp.manager.TexturizedParticleManager;


public class TexturizedParticlesClient implements ClientModInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(TexturizedParticles.MOD_NAME + "/Client");

	@Setter
	@Getter
	private static TexturizedParticlesConfig config;

	@Override
	public void onInitializeClient() {
		TexturizedParticlesClient.config = TexturizedParticlesConfig.getInstance();
		LOGGER.info("{} Client Initialized", TexturizedParticles.MOD_NAME);

		TexturizedParticleManager.onInitializeClient();
	}
}
