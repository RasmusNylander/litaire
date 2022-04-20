package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static model.Card.isUnknown;

class Foundation implements CardContainer {
	int[] cards;
	int size;

	private Foundation() {
		this.cards = new int[13];
		size = 0;
	}

	@SuppressWarnings("CopyConstructorMissesField")
	protected Foundation(Foundation foundation) {
		this();
		System.arraycopy(foundation.cards, 0, this.cards, 0, foundation.cards.length);
		this.size = foundation.size;
	}

	protected Foundation(int lastCard) {
		this();

		Card.validateCard(lastCard);
		if (isUnknown(lastCard))
			throw new IllegalArgumentException("Error: Cannot create Foundation with Unknown card.");

		int nextCard = Card.Ace | (lastCard & Card.SuitMask);
		do {
			cards[size++] = nextCard;
		} while (nextCard++ != lastCard);

	}

	protected Foundation(int @NotNull ... cards) {
		this();
		if (cards.length == 0) return;

		if (!canAcceptCard(cards[0]))
			throw new IllegalArgumentException("Error: Cannot push card: [" + Card.asString(cards[0]) + "] onto Foundation: " + this);

		if (!acceptableSequence(cards))
			throw new IllegalArgumentException("Error: Cannot create Foundation: " + Arrays.toString(Card.asString(cards)));

		System.arraycopy(cards, 0, this.cards, 0, cards.length);
		size = cards.length;
	}

	private boolean acceptableSequence(int @NotNull ... cards) {
		if (cards.length == 0) return true;
		if (isUnknown(cards[0])) return false;
		for (int i = 1; i < cards.length; i++) {
			if (isUnknown(cards[i])) return false;
			if (cards[i] - cards[i - 1] != 1) return false;
		}
		return true;
	}

	@Override
	public boolean canAcceptCard(int card) {
		if (isEmpty()) return (card & Card.RankMask) == Card.Ace;
		return acceptableSequence(cards[size - 1], card);
	}

	@Contract(pure = true)
	public Optional<Integer> asDestination() {
		if (isEmpty())
			return Optional.empty();
		return Optional.of(cards[size - 1]);
	}

	@Override
	public MoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		if (!this.reachableCards().contains(card))
			throw new IllegalMoveException("Error: Cannot move card: [" + Card.asString(card) + "] from Foundation: " + this);
		destination.receive(card);
		size--;
		return new MoveMetaInformation(destination, this);
	}

	@Override
	public void undo(int card, @NotNull MoveMetaInformation moveMetaInformation) {
		moveMetaInformation.destination.move(card, this);
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		if (cards.length == 0) return;

		if (!canAcceptCard(cards[0]))
			throw new IllegalMoveException("Error: Cannot push card: [" + Card.asString(cards[0]) + "] onto Foundation: " + this);
		if (!acceptableSequence(cards))
			throw new IllegalMoveException("Error: " + Arrays.toString(cards) + " is not a valid sequence for a Foundation.");
		System.arraycopy(cards, 0, this.cards, this.size, cards.length);
		size += cards.length;
	}

	@Override
	public @NotNull Set<Integer> reachableCards() {
		if (isEmpty()) return Set.of();
		return Set.of(cards[size - 1]);
	}

	@Override
	public boolean isEmpty() {
		return size < 1;
	}

	@Override
	public int getNumberOfCards() {
		return this.size;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Foundation that = (Foundation) o;
		return size == that.size && Arrays.equals(cards, 0, size, that.cards, 0, size);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(size);
		for (int i = 0; i < size; i++) {
			result = 31 * result + cards[i];
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (!isEmpty())
			sb.append(Card.asString(cards[0]));
		for (int i = 1; i < size; i++) {
			sb.append(", ");
			sb.append(Card.asString(cards[i]));
		}
		sb.append("]");
		return sb.toString();
	}
}
