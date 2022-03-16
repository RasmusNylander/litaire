package model.klondike;

import model.Card;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

class Stock {
	private final int[] cards;
	private int size;

	public static final Stock Empty = new Stock();

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

	public Stock(@NotNull Collection<Integer> cards) {
		this(intArrayFromIntegerCollection(cards));
	}

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
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public int size() {
		return size;
	}

	@NotNull
	public Set<Integer> reachableCards() {
		// Todo: Will fail when cards can be taken from the stock.
		Set<Integer> reachableCards = new HashSet<>();
		int i = 2;
		for (; i < size(); i += 3) {
			reachableCards.add(cards[i]);
		}
		if (size() % 3 != 0)
			reachableCards.add(cards[size() - 1]);
		return reachableCards;
	}

}
