package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArrayUtilsTest {
	@Test
	void contains_duplicates_should_return_false_given_empty_array() {
		int[] array = {};
		assertFalse(ArrayUtils.containsDuplicates(array));
	}

	@Test
	void contains_duplicates_should_return_false_given_array_containing_numbers() {
		int[] array = {1, 2, 3, 4, 5};
		assertFalse(ArrayUtils.containsDuplicates(array));
	}

	@Test
	void contains_duplicates_should_return_true_given_array_containing_duplicates() {
		int[] array = {1, 2, 3, 4, 4};
		assertTrue(ArrayUtils.containsDuplicates(array));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void contains_duplicates_should_throw_an_exception_given_null_array() {
		assertThrows(IllegalArgumentException.class, () -> ArrayUtils.containsDuplicates(null));
	}

	@Test
	void contains_duplicates_should_not_find_duplicates_outside_specified_range() {
		int[] array = {0, 2, 2, 3, 4, 4};
		assertFalse(ArrayUtils.containsDuplicates(array, 2, 5));
	}

	@Test
	void contains_duplicates_end_should_be_exclusive() {
		int[] array = {0, 1, 2, 3, 4, 4};
		assertFalse(ArrayUtils.containsDuplicates(array, 4, 5));
		assertTrue(ArrayUtils.containsDuplicates(array, 2, 6));
	}


}