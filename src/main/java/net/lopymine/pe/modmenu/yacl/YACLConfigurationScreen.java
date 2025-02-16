package net.lopymine.pe.modmenu.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.pe.ParticleEffects;
import net.lopymine.pe.client.ParticleEffectsClient;
import net.lopymine.pe.config.ParticleEffectsConfig;
import net.lopymine.pe.modmenu.yacl.simple.*;
import net.lopymine.pe.utils.ModMenuUtils;

import java.util.function.Function;

public class YACLConfigurationScreen {

	private static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = ModMenuUtils.getEnabledOrDisabledFormatter();

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		ParticleEffectsConfig defConfig = new ParticleEffectsConfig();
		ParticleEffectsConfig config = ParticleEffects.getConfig();

		return SimpleYACLScreenBuilder.startBuilder(parent, config::save)
				.categories(getGeneralCategory(defConfig, config))
				.build();
	}

	private static ConfigCategory getGeneralCategory(ParticleEffectsConfig defConfig, ParticleEffectsConfig config) {
		return SimpleCategoryBuilder.startBuilder("general")
				.options(
						SimpleOptionBuilder.getBooleanOption(
								"mod_enabled",
								defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled,
								ENABLED_OR_DISABLE_FORMATTER::apply, SimpleContent.NONE
						).build(),
						SimpleOptionBuilder.getBooleanOption(
								"debug_log_enabled",
								defConfig.isDebugLogEnabled(), config::isDebugLogEnabled, config::setDebugLogEnabled,
								ENABLED_OR_DISABLE_FORMATTER::apply, SimpleContent.NONE
						).build()
						)
				.build();
	}

}


