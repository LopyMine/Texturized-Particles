package net.lopymine.pe.utils;

import net.minecraft.util.math.random.Random;

import java.util.List;
import org.jetbrains.annotations.Nullable;

public class ListUtils {

	@Nullable
	public static <T> T getRandomElement(List<T> list, Random random) {
		if (list.isEmpty()) {
			return null;
		}
		return list.get(random.nextBetween(0, list.size() - 1));
	}
}
