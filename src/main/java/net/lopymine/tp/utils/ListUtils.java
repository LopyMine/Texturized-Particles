package net.lopymine.tp.utils;

import net.minecraft.util.math.random.Random;

import java.util.List;

public class ListUtils {

	public static <T> T getRandomElement(List<T> list, Random random) {
		return list.get(random.nextBetween(0, list.size() - 1));
	}
}
