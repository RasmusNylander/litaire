package model.klondike;

import model.Card;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Stack;

class Foundation extends Stack<Integer> {
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

	@Contract(pure = true)
	public boolean canAcceptCard(@NotNull Integer card) {
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
}
