package model.klondike;

import model.Card;
import model.IllegalMoveException;
import model.Move;
import model.Solitaire;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
	public void makeMove(@NotNull Move move) throws IllegalMoveException {
		CardContainer mover = findMoveSource(move);
		CardContainer destination = findDestination(move);
		mover.move(move.movedCard(), destination);
	}

	@NotNull
	private CardContainer findMoveSource(Move move) throws IllegalMoveException {
		for (Column column : columns)
			if (column.reachableCards().contains(move.movedCard())) return column;
		for (Foundation foundation : foundations)
			if (foundation.reachableCards().contains(move.movedCard())) return foundation;
		if (stock.reachableCards().contains(move.movedCard())) return stock;

		throw new IllegalMoveException("Error: Cannot find moving card in move.");
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

	private Collection<Move> stockMoves(){
		Collection<Move> moves = new ArrayList<>();

		if(stock.isEmpty()){
			return moves;
		}

		Set<Integer> stockCards = stock.reachableCards();
		for (Integer card : stockCards){
			for(Foundation foundation : foundations){
				if(foundation.canAcceptCard(card)){
					moves.add(new Move(card, foundation.asDestination()));
					break;
				}
			}
			for(Column column : columns){
				if(column.canAcceptCard(card)){
					moves.add(new Move(card, column.asDestination()));
				}
			}
		}
		return moves;
	}

	private Collection<Move> foundationMoves(Foundation foundation){
		Collection<Move> moves = new ArrayList<>();

		if(foundation.isEmpty()){
			return moves;
		}

		Integer topcard = foundation.peek();
		for(Column column : columns){
			if(column.canAcceptCard(topcard)){
				moves.add(new Move(topcard, column.asDestination()));
			}
		}
		return moves;
	}

}
