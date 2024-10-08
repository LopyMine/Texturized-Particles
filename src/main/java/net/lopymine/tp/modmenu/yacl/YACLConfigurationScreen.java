package net.lopymine.tp.modmenu.yacl;

import dev.isxander.yacl3.api.ConfigCategory;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.tp.client.TexturizedParticlesClient;
import net.lopymine.tp.config.TexturizedParticlesConfig;
import net.lopymine.tp.modmenu.yacl.simple.*;
import net.lopymine.tp.utils.ModMenuUtils;

import java.util.function.Function;

public class YACLConfigurationScreen {

	private static final Function<Boolean, Text> ENABLED_OR_DISABLE_FORMATTER = ModMenuUtils.getEnabledOrDisabledFormatter();

	private YACLConfigurationScreen() {
		throw new IllegalStateException("Screen class");
	}

	public static Screen createScreen(Screen parent) {
		TexturizedParticlesConfig defConfig = new TexturizedParticlesConfig();
		TexturizedParticlesConfig config = TexturizedParticlesClient.getConfig();

		return SimpleYACLScreenBuilder.startBuilder(parent, config::save)
				.categories(getGeneralCategory(defConfig, config))
				.build();
	}

	private static ConfigCategory getGeneralCategory(TexturizedParticlesConfig defConfig, TexturizedParticlesConfig config) {
		return SimpleCategoryBuilder.startBuilder("general")
				.options(
						SimpleOptionBuilder.getBooleanOption(
								"mod_enabled",
								defConfig.isModEnabled(), config::isModEnabled, config::setModEnabled,
								ENABLED_OR_DISABLE_FORMATTER::apply, SimpleContent.NONE
						).build())
				.build();
	}

}


