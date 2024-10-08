package net.lopymine.tp.utils;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.lopymine.tp.TexturizedParticles;
import net.lopymine.tp.modmenu.yacl.simple.SimpleContent;

import java.util.function.Function;

public class ModMenuUtils {

	private ModMenuUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getOptionKey(String optionId) {
		return String.format("%s.modmenu.option.%s", TexturizedParticles.MOD_ID, optionId);
	}

	public static String getCategoryKey(String categoryId) {
		return String.format("%s.modmenu.category.%s", TexturizedParticles.MOD_ID, categoryId);
	}

	public static String getGroupKey(String groupId) {
		return String.format("%s.modmenu.group.%s", TexturizedParticles.MOD_ID, groupId);
	}

	public static Text getName(String key) {
		return Text.translatable(key + ".name");
	}

	public static Text getDescription(String key) {
		return Text.translatable(key + ".description");
	}

	public static Identifier getContentId(SimpleContent content, String optionId) {
		return TexturizedParticles.id(String.format("textures/config/%s/%s.%s", content.getFolder(), optionId, content.getFileExtension()));
	}

	public static Text getModTitle() {
		return TexturizedParticles.text("modmenu.title");
	}

	public static Function<Boolean, Text> getEnabledOrDisabledFormatter() {
		return state -> TexturizedParticles.text("modmenu.formatter.enabled_or_disabled." + state);
	}

	public static Text getNoConfigScreenMessage() {
		return TexturizedParticles.text("modmenu.no_config_library_screen.message");
	}
}
