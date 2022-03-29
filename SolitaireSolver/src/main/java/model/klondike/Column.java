package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

class Column extends Vector<Integer> implements CardContainer {
	protected Column(int numberOfCards) {
		super(numberOfCards);
		for (int i = 0; i < numberOfCards; i++) {
			this.add(Card.Unknown);
		}
	}

	public void reveal(@NotNull Integer card, int index) {
		if (elementAt(index) != Card.Unknown)
			throw new IllegalArgumentException("Error: Card at index " + index + " is already revealed!");
		this.setElementAt(card, index);
	}

	@Contract(pure = true)
	public Optional<Integer> asDestination() {
		if (isEmpty())
			return Optional.empty();
		if (lastElement() == Card.Unknown)
			throw new IllegalStateException("Error: Undefined! Unknown card cannot be a destination.");
		return Optional.of(lastElement());
	}

	@Override
	public MoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		if (!reachableCards().contains(card))
			throw new IllegalMoveException("Error: Card " + Card.asString(card) + " cannot be moved from Column: " + this);
		int numMovingCards = this.size() - this.indexOf(card);
		int[] movingCards = new int[numMovingCards];
		for (int i = numMovingCards - 1; i >= 0; i--) {
			movingCards[i] = this.lastElement();
			this.remove(this.size() - 1); // pop
		}
		try {
			destination.receive(movingCards);
		} catch (IllegalMoveException e) {
			// Undo operation - restore original state of the column
			for (int mcard : movingCards) {
				this.add(mcard);
			}
			throw e;
		}
		return MoveMetaInformation.Empty;
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		if (!canAcceptCard(cards[0]))
			throw new IllegalMoveException("Error: Card " + Card.asString(cards[0]) + " cannot be moved to the column.");
		for (int i = 0; i < cards.length; i++) {
			if (!canAcceptCard(cards[i])) {
				this.removeRange(size() - i, size());
				throw new IllegalMoveException("Error: Card " + Card.asString(cards[0]) + " cannot be moved to Column: " + this);
			}
			add(cards[i]);
		}
	}

	@Override
	public @NotNull Set<Integer> reachableCards() {
		Set<Integer> reachableCards = new HashSet<>(size());
		for (int i = size() - 1; i >= 0; i--) {
			if (elementAt(i) == Card.Unknown) return reachableCards;
			reachableCards.add(elementAt(i));
		}
		return reachableCards;
	}

	@Override
	public boolean canAcceptCard(int card) {
		if (isEmpty()) return card == Card.King;
		if ((card & Card.Colour) == (lastElement() & Card.Colour)) return false;
		return (lastElement() & Card.RankMask) - (card & Card.RankMask) == 1;
	}

	@Override
	public int getNumberOfCards() {
		return size();
	}
}
