package model.klondike;

import model.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

class MoveMetaInformation {
	public final CardContainer destination, source;

	public MoveMetaInformation(@NotNull CardContainer destination, @NotNull CardContainer source) {
		this.destination = destination;
		this.source = source;
	}
}

class StockMoveMetaInformation extends MoveMetaInformation {
	//TODO: This that is is properly returned by the stock
	public final int index;
	public final int waste;

	StockMoveMetaInformation(@NotNull CardContainer destination, @NotNull CardContainer source, int index, int waste) {
		super(destination, source);
		this.index = index;
		this.waste = waste;
	}
}

record MoveHistoryRecord(Move move, MoveMetaInformation info) {
}

public final class Klondike implements Solitaire {
	public final Foundation @NotNull [] foundations;
	public final Column @NotNull [] columns;
	public final @NotNull Stock stock;

	private final @NotNull Stack<MoveHistoryRecord> moveHistory;

	private Klondike(Foundation @NotNull [] foundations, Column @NotNull [] columns, @NotNull Stock stock, @NotNull Stack<MoveHistoryRecord> moveHistory) {
		this.foundations = foundations;
		this.columns = columns;
		this.stock = stock;
		this.moveHistory = moveHistory;
	}

	public Klondike(@NotNull Foundation @NotNull [] foundations,
	                @NotNull Column @NotNull [] columns,
	                @NotNull Stock stock) {
		this(foundations, columns, stock, new Stack<>());
	}

	public static Klondike newGame(@NotNull Stock stock) {
		Column[] columns = new Column[7];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = new Column(i);
		}

		Foundation[] foundations = new Foundation[4];
		for (int i = 0; i < foundations.length; i++) {
			foundations[i] = new Foundation();
		}

		return new Klondike(foundations, columns, stock);
	}

	@Override
	public void makeMove(@NotNull Move move) throws IllegalMoveException {
		CardContainer mover = findSource(move.movedCard());
		CardContainer destination = findDestination(move);
		MoveMetaInformation info = mover.move(move.movedCard(), destination);
		MoveHistoryRecord record = new MoveHistoryRecord(move, info);
		moveHistory.push(record);
	}

	@Override
	public void undoMove() {
		if (moveHistory.isEmpty())
			throw new EmptyHistoryException("Error: Move history is empty; there is no move to undo!");
		MoveHistoryRecord record = moveHistory.pop();
		CardContainer source = record.info().source;
		int card = record.move().movedCard();
		source.undo(card, record.info());
	}

	@NotNull
	private CardContainer findSource(int card) throws IllegalMoveException {
		for (Column column : columns)
			if (column.reachableCards().contains(card)) return column;
		for (Foundation foundation : foundations)
			if (foundation.reachableCards().contains(card)) return foundation;
		if (stock.reachableCards().contains(card)) return stock;

		throw new IllegalMoveException("Error: Cannot find card " + Card.asString(card) + " in any reachable card container.");
	}

	@NotNull
	private CardContainer findDestination(Move move) throws IllegalMoveException {
		if (move.destination().isPresent()) {
			for (Column column : columns)
				if (column.asDestination().equals(move.destination())) return column;
			for (Foundation foundation : foundations)
				if (foundation.asDestination().equals(move.destination())) return foundation;
			throw new IllegalMoveException("Error: Cannot find destination in move.\nMove: " + move);
		}

		if ((move.movedCard() & Card.RankMask) == Card.King) {
			for (Column column : columns)
				if (column.isEmpty()) return column;
		}

		if ((move.movedCard() & Card.RankMask) == Card.Ace) {
			for (Foundation foundation : foundations)
				if (foundation.isEmpty()) return foundation;
		}

		throw new IllegalMoveException("Error: Cannot find destination in move.\nMove: " + move);
	}

	@Override
	public boolean isLegalMove(@NotNull Move move) {
		return possibleMoves().contains(move);
	}

	@Override
	@NotNull
	public Set<Move> possibleMoves() {
		Set<Move> moves = new HashSet<>(24);
		// This will probably be too slow, but let's not optimise prematurely
		for (Column column : columns) {
			moves.addAll(columnMoves(column));
		}

		for (Foundation foundation : foundations) {
			moves.addAll(foundationMoves(foundation));
		}

		moves.addAll(stockMoves());
		return moves;
	}

	private Collection<Move> columnMoves(Column column) {
		Collection<Move> moves = new ArrayList<>();

		if (column.isEmpty())
			return moves;

		int topCard = column.lastCard();
		for (Foundation foundation : foundations) {
			if (!foundation.canAcceptCard(topCard))
				continue;
			moves.add(new Move(topCard, foundation.asDestination()));
			break;
		}

		Set<Integer> reachableCards = column.reachableCards();
		for (Integer card : reachableCards) {
			for (Column kloumn : this.columns) {
				if (kloumn == column) continue;
				if (!kloumn.canAcceptCard(card)) continue;
				moves.add(new Move(card, kloumn.asDestination()));
			}
		}

		return moves;
	}

	private Collection<Move> stockMoves() {
		Collection<Move> moves = new ArrayList<>();

		if (stock.isEmpty()) {
			return moves;
		}

		Set<Integer> stockCards = stock.reachableCards();
		for (Integer card : stockCards) {
			for (Foundation foundation : foundations) {
				if (foundation.canAcceptCard(card)) {
					moves.add(new Move(card, foundation.asDestination()));
					break;
				}
			}
			for (Column column : columns) {
				if (column.canAcceptCard(card)) {
					moves.add(new Move(card, column.asDestination()));
				}
			}
		}
		return moves;
	}

	private Collection<Move> foundationMoves(Foundation foundation) {
		Collection<Move> moves = new ArrayList<>();

		if (foundation.isEmpty()) {
			return moves;
		}

		Set<Integer> foundationCards = foundation.reachableCards();
		assert foundationCards.size() == 1;
		Integer card = foundationCards.iterator().next();
		for (Column column : columns) {
			if (column.canAcceptCard(card)) {
				moves.add(new Move(card, column.asDestination()));
			}
		}
		return moves;
	}

	/**
	 * Returns whether the <b>current<b/> state of the games are equal. I.e. calling undo() on both games
	 * will not necessarily result in the same state.
	 *
	 * @return true if the states are equal, false otherwise
	 */
	@Contract(value = "null -> false", pure = true)
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Klondike klondike = (Klondike) o;
		return Arrays.equals(foundations, klondike.foundations) && Arrays.equals(columns, klondike.columns) && stock.equals(klondike.stock);
	}

	@Override
	public int hashCode() {
		int result = stock.hashCode();
		result = 31 * result + Arrays.hashCode(foundations);
		result = 31 * result + Arrays.hashCode(columns);
		return result;
	}

	public Klondike deepCopy() {
		return new Klondike(foundations.clone(), columns.clone(), stock.copy());
	}
}
