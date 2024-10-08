package net.lopymine.tp.utils;

import net.minecraft.util.math.ColorHelper.Argb;

public class ArgbUtils {

	public static int getColorWithoutAlpha(int original) {
		return Argb.getArgb(255, Argb.getRed(original), Argb.getGreen(original), Argb.getBlue(original));
	}

}
