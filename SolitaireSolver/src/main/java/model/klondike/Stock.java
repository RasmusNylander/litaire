package model.klondike;

import model.Card;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class Stock {
	private final int[] cards;
	private int size;
	private int waste;

	public static final Stock Empty = new Stock();

	public Stock(int... cards) {
		if (cards == null)
			throw new IllegalArgumentException("Error: Cards must not be null");
		for (Integer card : cards) {
			if (card == null)
				throw new IllegalArgumentException("Error: Card must not be null"); // Might not be reachable anymore
			if ((card & Card.RankMask) == Card.Unknown)
				throw new IllegalArgumentException("Error: Stock can not be updated, thus card may not be unknown");

			// This is slow, but it need only be done once, so it's fine
			int duplicates = -1;
			for (Integer otherCard : cards) {
				if (otherCard.equals(card))
					duplicates++;
			}
			if (duplicates > 0)
				throw new IllegalArgumentException("Error: Stock may not contain duplicate cards!");
		}
		this.cards = cards;
		size = cards.length;
		waste = size() - 1;
	}

	public Stock(@NotNull Collection<Integer> cards) {
		this(intArrayFromIntegerCollection(cards));
	}

	private static int[] intArrayFromIntegerCollection(Collection<Integer> collection) {
		Integer[] integerArray = collection.toArray(new Integer[0]);
		int[] intArray = new int[integerArray.length];
		for (int i = 0; i < intArray.length; i++) {
			if (integerArray[i] == null)
				throw new IllegalArgumentException("Error: Cannot convert null to primitive type int");
			intArray[i] = integerArray[i];
		}
		return intArray;
	}

	@Contract(pure = true)
	public boolean isEmpty() {
		return size() == 0;
	}

	@Contract(pure = true)
	public int size() {
		return size;
	}

	@Contract(pure = true)
	private int waste() {
		return waste;
	}

	@NotNull
	@Contract(pure = true)
	public Set<Integer> reachableCards() {
		if (isEmpty()) return Set.of();

		Set<Integer> reachableCards = new HashSet<>();

		// This could probably be done a lot nicer and faster
		// Add all cards continuing to draw from current position
		for (int i = waste(); i < size(); i++) {
			reachableCards.add(cards[i]);
		}
		// Add all cards reachable after going through the entire stock
		for (int i = 2; i < size(); i += 3) {
			reachableCards.add(cards[i]);
		}

		return reachableCards;
	}

	private int removeCard(int card) {
		int index = 0;
		while (index < size() && card != cards[index]) index++;
		if (index == size)
			throw new IllegalArgumentException("Error: Cannot remove " + card + " as it is not contained herein.");

		size--;
		if (size() - index >= 0)
			System.arraycopy(cards, index + 1, cards, index, size() - index);

		return index;
	}

	@NotNull
	public Integer take(int card) {
		// This can be sped up using a lookup table
		if (!reachableCards().contains(card)) {
			throw new IllegalArgumentException("Error: cannot take " + card + " from Stock as it is not reachable.\nReachable cards: " + reachableCards());
		}
		waste = removeCard(card) - 1;

		return card;
	}

}
