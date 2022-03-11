package model.klondike;

import model.Card;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

class Stock extends ArrayList<Integer> {
	protected Stock(int numberOfCards) {
		super(numberOfCards);
		for (int i = 0; i < numberOfCards; i++) {
			this.add(Card.Unknown);
		}
	}
}

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

public record Klondike(@NotNull Foundation[] foundations, @NotNull Column[] columns, @NotNull Stock stock) {

	public static Klondike newGame() {
		Column[] columns = new Column[7];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new Column(i);
		}

		Foundation[] foundations = new Foundation[4];
		for (int i = 0; i < foundations.length; i++) {
			foundations[i] = new Foundation();
		}

		return new Klondike(foundations, columns, new Stock(24));
	}
}
