package net.lopymine.tp.utils;

public class ArgbUtils {

	public static int getColorWithoutAlpha(int original) {
		return getArgb(255, getRed(original), getGreen(original), getBlue(original));
	}

	public static int getArgb(int alpha, int red, int green, int blue) {
		return alpha << 24 | red << 16 | green << 8 | blue;
	}

	public static int getAlpha(int color) {
		return color >>> 24;
	}

	public static int getRed(int color) {
		return color >> 16 & 255;
	}

	public static int getGreen(int color) {
		return color >> 8 & 255;
	}

	public static int getBlue(int color) {
		return color & 255;
	}

}
