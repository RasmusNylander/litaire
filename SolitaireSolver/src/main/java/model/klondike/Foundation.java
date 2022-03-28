package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.Stack;

class Foundation extends Stack<Integer> implements CardContainer {
	protected Foundation() {
		super();
	}

	@Override
	public Integer push(@NotNull Integer card) {
		if (!canAcceptCard(card))
			throw new IllegalArgumentException("Error: Cannot push card: [" + card + "] onto Foundation: " + this);
		super.push(card);
		return card;
	}

	@Override
	public boolean canAcceptCard(int card) {
		if (card == Card.Unknown) return false;
		if (isEmpty()) return (card & Card.RankMask) == Card.Ace;
		return card - peek() == 1;
	}

	@Contract(pure = true)
	public Optional<Integer> asDestination() {
		if (isEmpty())
			return Optional.empty();
		return Optional.of(peek());
	}

	@Override
	public void move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		if (!this.reachableCards().contains(card))
			throw new IllegalMoveException("Error: Cannot move card: [" + Card.asString(card) + "] from Foundation: " + this);
		destination.receive(this.pop());
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		for (int i = 0; i < cards.length; i++) {
			int card = cards[i];
			if (!canAcceptCard(card)) {
				for (int j = 0; j < i; j++) this.pop(); // Restore the original state
				throw new IllegalMoveException("Error: Cannot push card: [" + Card.asString(card) + "] onto Foundation: " + this);
			}
			push(card);
		}
	}

	@Override
	public @NotNull Set<Integer> reachableCards() {
		if (isEmpty()) return Set.of();
		return Set.of(this.peek());
	}

	@Override
	public int getNumberOfCards() {
		return size();
	}
}
