package utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class ArrayUtils {

	public static boolean containsDuplicates(int @NotNull [] array) {
		return containsDuplicates(array, 0, array.length);
	}

	public static boolean containsDuplicates(int @NotNull [] array, int start, int end) {
		Set<Integer> set = new HashSet<>();
		while (start < end) {
			if (!set.add(array[start++]))
				return true;
		}
		return false;
	}
}
