package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

import static model.Card.isUnknown;

public class Stock implements CardContainer {
	private final int[] cards;
	private int size;
	private int waste;

	public static final Stock Empty = new Stock();

	public Stock(int... cards) {
		if (cards == null)
			throw new IllegalArgumentException("Error: Cards must not be null");
		for (int card : cards) {
			if (isUnknown(card))
				throw new IllegalArgumentException("Error: Stock can not be updated, thus card may not be unknown");
			if (!Card.isValidCard(card))
				throw new IllegalArgumentException("Error: Card is not a valid card. Card: " + card);

			// This is slow, but it need only be done once, so it's fine
			int duplicates = -1;
			for (int otherCard : cards) {
				if (card == otherCard)
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

	@SuppressWarnings("CopyConstructorMissesField")
	public Stock(Stock stock) {
		this.cards = stock.cards;
		this.size = stock.size;
		this.waste = stock.waste;
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

	@Override
	@Contract(pure = true)
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int getNumberOfCards() {
		return size();
	}

	@Contract(pure = true)
	@Deprecated
	private int size() {
		return size;
	}

	@Contract(pure = true)
	private int waste() {
		return waste;
	}

	private boolean undoing = false;

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		if (!undoing) throw new IllegalMoveException("Error: Stock can not receive cards");
		if (cards == null) throw new IllegalArgumentException("Error: Cards must not be null");
		if (cards.length != 1)
			throw new IllegalArgumentException("Error: Stock can only receive one card at a time as only one card can be removed at a time.");
		addCard(cards[0], waste() + 1); //Undoing should be of the last move, and thus waste = index - 1
	}

	@Override
	public boolean canAcceptCard(int card) {
		return false;
	}

	@Override
	public StockMoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		int premoveWaste = waste();
		int kard = take(card); // This will ensure that the card is reachable, and only if it is, will it be removed
		try {
			destination.receive(kard);
		} catch (IllegalMoveException e) {
			addCard(card, waste() + 1);
			waste = premoveWaste;
			throw e;
		}
		return new StockMoveMetaInformation(destination, this, waste() + 1, premoveWaste);
	}

	@Override
	public void undo(int card, @NotNull MoveMetaInformation moveMetaInformation) {
		if (!(moveMetaInformation instanceof StockMoveMetaInformation stockMoveMetaInformation))
			throw new IllegalArgumentException("Error: Move meta information is not of type StockMoveMetaInformation!");
		undoing = true;
		stockMoveMetaInformation.destination.move(card, this);
		undoing = false;
		waste = stockMoveMetaInformation.waste;
	}

	@Contract(pure = true)
	public @NotNull Set<Integer> reachableCards() {
		if (isEmpty()) return Set.of();

		Set<Integer> reachableCards = new HashSet<>();

		// This could probably be done a lot nicer and faster. Should probably be cached
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

	private void addCard(int card, int index) {
		// This could be done better; cards would have to not be an array
		if (index > size())
			throw new IllegalArgumentException("Error: Cannot add card at index " + index + " to stock, as this would result in one or more empty spaces.\nNote: Index most at most be equal to current size of stock");
		if (index < size()) // Shuffle all the cards along
			System.arraycopy(cards, index, cards, index + 1, size() - index);
		cards[index] = card;
		size++;
	}

	private int removeCard(int card) {
		int index = 0;
		while (index < size() && card != cards[index]) index++;
		if (index == size)
			throw new IllegalArgumentException("Error: Cannot remove " + card + " as it is not contained herein.");

		size--;
		if (size() - index >= 0) // Shuffle cards back (shuffle as in move, not randomise)
			System.arraycopy(cards, index + 1, cards, index, size() - index);

		return index;
	}

	int take(int card) {
		// This can be sped up using a lookup table
		if (!reachableCards().contains(card)) {
			throw new IllegalArgumentException("Error: cannot take " + card + " from Stock as it is not reachable.\nReachable cards: " + reachableCards());
		}
		waste = removeCard(card) - 1;

		return card;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Stock stock = (Stock) o;
		if (size != stock.size || waste != stock.waste) return false;
		return Arrays.equals(cards, 0, size(), stock.cards, 0, size());
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(size, waste);
		for (int i = 0; i < size(); i++) {
			result = 31 * result + cards[i]; // Arrays.hashCode(cards) but ignoring cards that are not in the stock
		}
		return result;
	}

	@Override
	public String toString() {
		Function<Integer, String> cardToColouredString = card -> {
			final String ANSI_RED = "\u001B[31m";
			final String ANSI_GREEN = "\u001B[32m";
			final String ANSI_RESET = "\u001B[0m";
			if (cards[waste()] == card) return ANSI_RED + Card.asString(card) + ANSI_RESET;
			if (reachableCards().contains(card)) return ANSI_GREEN + Card.asString(card) + ANSI_RESET;
			return Card.asString(card);
		};

		StringBuilder sb = new StringBuilder("[");
		if (!isEmpty())
			sb.append(cardToColouredString.apply(cards[0]));
		for (int i = 1; i < size(); i++) {
			sb.append(", ").append(cardToColouredString.apply(cards[i]));
		}
		return sb.append("]").toString();
	}

}
