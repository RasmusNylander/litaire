package model.klondike;

import model.Card;
import model.Move;
import model.Solitaire;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class Foundation extends Stack<Integer> {
	protected Foundation() {
		super();
	}

	@Override
	public Integer push(@NotNull Integer card) {
		if (!canAcceptCard(card))
			throw new IllegalArgumentException("Error: Cannot push card: [" + card + "] onto Foundation: " + this);
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
	public Set<Move> possibleMoves() {
		Set<Move> moves = new HashSet<>(24);
		// This will probably be too slow, but let's not optimise prematurely
		for (Column column : columns) {
			moves.addAll(columnMoves(column));
		}
		return moves;
	}

	private Collection<Move> columnMoves(Column column) {
		Collection<Move> moves = new ArrayList<>();

		if (column.isEmpty())
			return moves;

		Integer topCard = column.lastElement();
		for (Foundation foundation : foundations) {
			if (foundation.canAcceptCard(topCard)) {
				moves.add(new Move(topCard, foundation.asDestination()));
				break;
			}
		}


		for (Integer card : column) {
			if ((card & Card.RankMask) == Card.Unknown)
				continue;
			for (Column kloumn : this.columns) {
				if (kloumn == column) continue;
				if (!kloumn.canAcceptCard(card)) continue;
				moves.add(new Move(card, kloumn.asDestination()));
			}
		}

		return moves;
	}
}
