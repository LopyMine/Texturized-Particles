package net.lopymine.pe.client;

import org.slf4j.*;

import net.fabricmc.api.ClientModInitializer;

import net.lopymine.pe.ParticleEffects;
import net.lopymine.pe.manager.ParticleEffectsManager;

public class ParticleEffectsClient implements ClientModInitializer {

	public static Logger LOGGER = LoggerFactory.getLogger(ParticleEffects.MOD_NAME + "/Client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("{} Client Initialized", ParticleEffects.MOD_NAME);
		ParticleEffectsManager.onInitializeClient();
	}

}
