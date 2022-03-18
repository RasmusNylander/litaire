package model.klondike;

import model.Card;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Vector;

class Column extends Vector<Integer> {
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

	@Contract(pure = true)
	public boolean canAcceptCard(@NotNull Integer card) {
		if (isEmpty()) return card == Card.King;
		if ((card & Card.Colour) == (lastElement() & Card.Colour)) return false;
		return (lastElement() & Card.RankMask) - (card & Card.RankMask) == 1;
	}
}
