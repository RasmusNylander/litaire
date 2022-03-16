package model.klondike;

import model.Card;
import model.Move;
import model.Solitaire;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class Foundation extends Stack<Integer> {
	protected Foundation() {
		super();
	}

}

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
}

public record Klondike(@NotNull Foundation[] foundations, @NotNull Column[] columns,
                       @NotNull Stock stock) implements Solitaire {

	public static Klondike newGame() {
		Column[] columns = new Column[7];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new Column(i);
		}

		Foundation[] foundations = new Foundation[4];
		for (int i = 0; i < foundations.length; i++) {
			foundations[i] = new Foundation();
		}

		Collection<Integer> stockCards = Arrays.asList(Card.fullDeck()).subList(0, 24);
		Stock stock = new Stock(stockCards);

		return new Klondike(foundations, columns, stock);
	}

	@Override
	public List<Move> possibleMoves() {
		List<Move> moves = new ArrayList<>(24);
		// This will probably be too slow, but let's not optimise prematurely
		for (Column column : this.columns) { // Todo: Refactor, should probably be extracted to method
			if (column.isEmpty()) continue;
			Integer topCard = column.lastElement();
			for (Foundation foundation : foundations) {
				if (foundation.isEmpty()) {
					if ((topCard & Card.RankMask) == Card.Ace)
						moves.add(new Move(topCard, Optional.empty()));
					break;
				}
				Integer foundationTop = foundation.peek();
				if (topCard - 1 == foundationTop) {
					moves.add(new Move(topCard, Optional.of(foundationTop)));
					break;
				}
			}
		}
		return moves;
	}
}