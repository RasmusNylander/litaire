package model.klondike;

import model.*;
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

public record Klondike(@NotNull Foundation[] foundations, @NotNull Column[] columns,
                       @NotNull Stock stock) implements Solitaire {

		moveHistory = new Stack<>();
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
		mover.move(move.movedCard(), destination);
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
			throw new IllegalMoveException("Error: Cannot find destination in move.");
		}

		if ((move.movedCard() & Card.RankMask) == Card.King) {
			for (Column column : columns)
				if (column.isEmpty()) return column;
		}

		if ((move.movedCard() & Card.RankMask) == Card.Ace) {
			for (Foundation foundation : foundations)
				if (foundation.isEmpty()) return foundation;
		}

		throw new IllegalMoveException("Error: Cannot find destination in move.");
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

		Integer topcard = foundation.peek();
		for (Column column : columns) {
			if (column.canAcceptCard(topcard)) {
				moves.add(new Move(topcard, column.asDestination()));
			}
		}
		return moves;
	}

}
