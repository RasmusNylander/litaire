package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static model.Card.RankMask;

class Column implements CardContainer {
	/**
	 * In a game of klondike, a column can start with at most 7 cards. If the last
	 * card is a king, then 12 more cards can be moved on top of it.
	 */
	private final static int MAX_NUM_CARDS_IN_COLUMN = 7 + 12;
	private final int[] cards;
	private int numCards;

	protected Column(Column column) {
		this.cards = column.cards.clone();
		this.numCards = column.numCards;
	}

	protected Column(int numberOfUnknownCards) {
		this(numberOfUnknownCards, new int[0]);
	}

	protected Column(int numberOfUnknownCards, int... knownCards) {
		if (numberOfUnknownCards < 0)
			throw new IllegalArgumentException("Error: Cannot have a negative amount of cards!");

		numCards = numberOfUnknownCards + knownCards.length;
		if (numCards > MAX_NUM_CARDS_IN_COLUMN)
			throw new IllegalArgumentException("Error: Invalid number of cards!" +
					" Number of cards must be in range [0; " + MAX_NUM_CARDS_IN_COLUMN + "]");

		validateCardSequence(knownCards);

		cards = new int[MAX_NUM_CARDS_IN_COLUMN];
		for (int i = 0; i < numberOfUnknownCards; i++) // Unlike System.arraycopy, Arrays.fill is not a native method
			cards[i] = Card.Unknown;
		System.arraycopy(knownCards, 0, cards, numberOfUnknownCards, knownCards.length);
	}

	private void validateCardSequence(int... cards) {
		if (cards.length < 2) return;
		for (int i = 1; i < cards.length; i++) {
			if ((cards[i - 1] & Card.Colour) == (cards[i] & Card.Colour))
				throw new IllegalArgumentException("Error: Invalid card sequence! In a column " + Card.asString(cards[i - 1])
						+ " cannot be followed by " + Card.asString(cards[i]));
			if ((cards[i - 1] & RankMask) - (cards[i] & RankMask) != 1)
				throw new IllegalArgumentException("Error: Invalid card sequence! In a column " + Card.asString(cards[i - 1])
						+ " cannot be followed by " + Card.asString(cards[i]));
		}
	}

	public void reveal(int card, int index) {
		if (cards[index] != Card.Unknown)
			throw new IllegalArgumentException("Error: Card at index " + index + " is already revealed!");
		cards[index] = card;
	}

	@Contract(pure = true)
	public Optional<Integer> asDestination() {
		if (isEmpty())
			return Optional.empty();
		int card = cards[getNumberOfCards() - 1];
		if (card == Card.Unknown)
			throw new IllegalStateException("Error: Undefined! Unknown card cannot be a destination.");
		return Optional.of(card);
	}

	@Override
	public MoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		if (!reachableCards().contains(card))
			throw new IllegalMoveException("Error: Card " + Card.asString(card) + " cannot be moved from Column: " + this);
		int numMovingCards = this.size() - this.indexOf(card);
		int[] movingCards = new int[numMovingCards];
		for (int i = numMovingCards - 1; i >= 0; i--) {
			movingCards[i] = this.lastCard();
			numCards--;
		}
		try {
			destination.receive(movingCards);
		} catch (IllegalMoveException e) {
			// Undo operation - restore original state of the column
			for (int mcard : movingCards) {
				numCards++;
			}
			throw e;
		}
		return new MoveMetaInformation(destination, this);
	}

	private int indexOf(int card) {
		for (int i = 0; i < size(); i++) {
			if (cards[i] == card) return i;
		}
		throw new NoSuchElementException("Error: Card " + Card.asString(card) + " not found in Column: " + this);
	}

	private boolean undoing = false;

	@Override
	public void undo(int card, @NotNull MoveMetaInformation moveMetaInformation) {
		//This is really not very elegant, but it will have to do for now.
		undoing = true;
		try {
			moveMetaInformation.destination.move(card, this);
		} finally {
			undoing = false;
		}
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		if (cards.length == 0) return;
		if (!undoing) {
			if (!canAcceptCard(cards[0]))
				throw new IllegalMoveException("Error: Card " + Card.asString(cards[0]) + " cannot be moved to Column: " + this);
			try {
				validateCardSequence(cards);
			} catch (IllegalArgumentException e) {
				throw new IllegalMoveException(e.getMessage());
			}
		}

		System.arraycopy(cards, 0, this.cards, numCards, cards.length);
		numCards += cards.length;
	}

	@Override
	public @NotNull Set<Integer> reachableCards() {
		Set<Integer> reachableCards = new HashSet<>(size());
		for (int i = size() - 1; i >= 0; i--) {
			if (cards[i] == Card.Unknown) break;
			reachableCards.add(cards[i]);
		}
		return reachableCards;
	}

	@Override
	public boolean isEmpty() {
		return getNumberOfCards() < 1;
	}

	@Override
	public boolean canAcceptCard(int card) {
		int cardRank = card & RankMask;
		if (isEmpty()) return cardRank == Card.King;
		if ((card & Card.Colour) == (lastCard() & Card.Colour)) return false;
		return (lastCard() & RankMask) - cardRank == 1;
	}

	@Override
	public int getNumberOfCards() {
		return size();
	}

	private int size() {
		return numCards;
	}

	public int lastCard() {
		if (isEmpty()) throw new NoSuchElementException("Error: Cannot return last card as column is empty!");
		return cards[size() - 1];
	}

	public boolean contains(int card) {
		for (int i = 0; i < size(); i++)
			if (cards[i] == card) return true;
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Column column = (Column) o;
		if (getNumberOfCards() != column.getNumberOfCards()) return false;
		return Arrays.equals(
				this.cards, 0, getNumberOfCards(),
				column.cards, 0, getNumberOfCards()
		);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(numCards);
		result = 31 * result + Arrays.hashCode(cards);
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (!isEmpty())
			sb.append(Card.asString(cards[0]));
		for (int i = 1; i < size(); i++) {
			sb.append(", ");
			sb.append(Card.asString(cards[i]));
		}
		sb.append("]");
		return sb.toString();
	}
}
