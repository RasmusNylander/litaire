package model.klondike;

import model.Card;
import model.EmptyHistoryException;
import model.IllegalMoveException;
import model.Move;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class KlondikeTest {

	@Test
	void new_game_should_not_be_null() {
		assertNotNull(Klondike.newGame(new Stock()));
	}

	@Test
	void new_game_foundations_should_not_be_null() {
		assertNotNull(Klondike.newGame(new Stock()).foundations);
	}

	@Test
	void new_game_should_have_4_foundations() {
		assertEquals(4, Klondike.newGame(new Stock()).foundations.length);
	}

	@Test
	void new_game_should_have_empty_foundations() {
		Foundation[] foundations = Klondike.newGame(new Stock()).foundations;
		assertTrue(foundations[0].isEmpty());
		assertTrue(foundations[3].isEmpty());
	}

	@Test
	void new_game_columns_should_not_be_null() {
		assertNotNull(Klondike.newGame(new Stock()).columns);
	}

	@Test
	void new_game_should_have_7_columns() {
		assertEquals(7, Klondike.newGame(new Stock()).columns.length);
	}

	@Test
	void new_game_columns_should_contain_cards() {
		Column[] columns = Klondike.newGame(new Stock()).columns;
		assertFalse(columns[3].isEmpty());
		assertFalse(columns[1].isEmpty());
	}

	@Test
	void new_game_columns_should_contain_unknown_cards() {
		Column[] columns = Klondike.newGame(new Stock()).columns;
		assertTrue(columns[6].contains(Card.Unknown));
		assertTrue(columns[2].contains(Card.Unknown));
		assertTrue(columns[3].contains(Card.Unknown));
	}

	@Test
	void new_game_columns_should_contain_only_unknown_cards() {
		Column[] columns = Klondike.newGame(new Stock()).columns;
		assertFalse(columns[1].contains(Card.Ace));
		assertFalse(columns[6].contains(Card.Three | Card.Type));
		assertFalse(columns[4].contains(Card.Queen | Card.Colour));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void new_game_should_throw_exception_if_stock_is_null() {
		assertThrows(IllegalArgumentException.class, () -> Klondike.newGame(null));
	}

	@Test
	void new_game_stock_should_not_be_null() {
		assertNotNull(Klondike.newGame(new Stock()).stock);
	}

	@Test
	void new_game_stock_should_not_be_equal_specified_stock() {
		assertEquals(new Stock(Card.Ace, Card.Six), Klondike.newGame(new Stock(Card.Ace, Card.Six)).stock);
	}

	@Test
	void possible_moves_should_not_be_null() {
		assertNotNull(Klondike.newGame(new Stock()).possibleMoves());
	}

	@Test
	void possible_moves_should_be_empty() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], Stock.Empty); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_from_column_to_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Column[] columns = new Column[]{new Column(1)};
		columns[0].reveal(Card.Ace | Card.Colour, 0);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty); // This is cumbersome, please change
		assertFalse(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_not_be_possible_to_move_two_to_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Column[] columns = new Column[]{new Column(1)};
		columns[0].reveal(Card.Two | Card.Type, 0);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_onto_previous_card_in_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation(), new Foundation()};
		foundations[0].addElement(Card.Ace);
		foundations[1].addElement(Card.Jack | Card.Colour);
		Column[] columns = new Column[]{new Column(1), new Column(2)};
		columns[0].reveal(Card.Two, 0);
		columns[1].reveal(Card.Queen | Card.Colour, 1);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().containsAll(List.of(
				new Move(Card.Two, Optional.of(Card.Ace)),
				new Move(Card.Queen | Card.Colour, Optional.of(Card.Jack | Card.Colour))
		)));
	}

	@Test
	void should_be_possible_to_move_between_columns() {
		Column[] columns = new Column[]{new Column(0), new Column(2)};
		columns[1].reveal(Card.King, 1);
		Klondike klondike = new Klondike(new Foundation[0], columns, new Stock());
		assertTrue(klondike.possibleMoves().contains(new Move(Card.King, Optional.empty())), "Possible moves: " + klondike.possibleMoves());
	}

	@Test
	void should_not_be_possible_to_move_unknown_between_columns() {
		Column[] columns = new Column[]{new Column(0), new Column(2), new Column(19)};
		Klondike klondike = new Klondike(new Foundation[0], columns, new Stock());
		assertTrue(klondike.possibleMoves().isEmpty(), "Possible moves: " + klondike.possibleMoves());
	}

	@Test
	void should_not_be_possible_to_move_king_between_non_empty_columns() {
		Column[] columns = new Column[]{new Column(1), new Column(2), new Column(19)};
		columns[1].reveal(Card.King, 1);
		Klondike klondike = new Klondike(new Foundation[0], columns, new Stock());
		assertTrue(klondike.possibleMoves().isEmpty(), "Possible moves: " + klondike.possibleMoves());
	}

	@Test
	void should_not_be_possible_to_move_non_king_to_empty_columns() {
		Column[] columns = new Column[]{new Column(0), new Column(2), new Column(0)};
		columns[1].reveal(Card.Seven, 1);
		Klondike klondike = new Klondike(new Foundation[0], columns, new Stock());
		assertTrue(klondike.possibleMoves().isEmpty(), "Possible moves: " + klondike.possibleMoves());
	}

	@Test
	void should_not_be_possible_to_move_card_to_same_color() {
		Column[] columns = new Column[]{new Column(1), new Column(2), new Column(19)};
		columns[0].reveal(Card.Three, 0);
		columns[1].reveal(Card.Two, 1);
		Klondike klondike = new Klondike(new Foundation[0], columns, new Stock());
		assertTrue(klondike.possibleMoves().isEmpty(), "Possible moves: " + klondike.possibleMoves());
	}

	
	@Test
	void should_be_possible_to_move_from_stock_to_foundations() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Stock stock = new Stock(Card.Ace); 
		Klondike klondike = new Klondike( foundations, new Column[0], stock);
		assertFalse(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_not_be_possible_to_get_null_move(){
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Stock stock = new Stock(Card.Ace); 
		Klondike klondike = new Klondike( foundations, new Column[0], stock);
		assertNotNull(klondike.possibleMoves().toArray()[0]);
	}

	@Test
	void should_not_be_possible_to_move_non_ace_from_stock_to_empty_foundation(){
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Stock stock = new Stock(Card.Two); 
		Klondike klondike = new Klondike( foundations, new Column[0], stock);
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_from_stock_to_columns() {
		Column[] column = new Column[]{new Column(0)};
		Stock stock = new Stock(Card.King); 
		Klondike klondike = new Klondike( new Foundation[0], column, stock);
		assertFalse(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_not_be_possible_to_get_null_move_from_columns(){
		Column[] column = new Column[]{new Column(0)};
		Stock stock = new Stock(Card.King); 
		Klondike klondike = new Klondike( new Foundation[0], column, stock);
		assertNotNull(klondike.possibleMoves().toArray()[0]);
	}

	@Test
	void should_not_be_possible_to_move_non_king_from_stock_to_empty_column(){
		Column[] column = new Column[]{new Column(0)};
		Stock stock = new Stock(Card.Eight); 
		Klondike klondike = new Klondike( new Foundation[0], column, stock);
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_from_foundations_to_columns() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three);
		Column[] columns = new Column[]{new Column(1)};
		columns[0].addElement(Card.Four | Card.Colour);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertFalse(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_from_multiple_foundations_to_columns() {
		Foundation[] foundations = new Foundation[]{new Foundation(),new Foundation()};
		foundations[0].addElement(Card.Three);
		foundations[1].addElement(Card.Jack | Card.Colour);
		Column[] columns = new Column[]{new Column(1), new Column(3)};
		columns[0].addElement(Card.Four | Card.Colour);
		columns[1].addElement(Card.Queen);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertEquals(2, klondike.possibleMoves().size());
	}

	@Test
	void possible_moves_should_only_contain_one_move_when_only_one_foundation_and_only_one_column() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three);
		Column[] columns = new Column[]{new Column(1)};
		columns[0].addElement(Card.Four | Card.Colour);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertEquals(1, klondike.possibleMoves().size());
	}

	@Test
	void move_from_foundation_to_column_should_include_involved_cards(){
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three);
		Column[] columns = new Column[]{new Column(1)};
		columns[0].addElement(Card.Four | Card.Colour);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertEquals(new Move(Card.Three, Optional.of(Card.Four | Card.Colour)), klondike.possibleMoves().toArray()[0]);
	}

	@Test
	void move_from_foundation_to_mulitple_columns_should_be_possible(){
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three);

		Column[] columns = new Column[]{new Column(1), new Column(3)};
		columns[0].addElement(Card.Four | Card.Colour);
		columns[1].addElement(Card.Four | Card.Colour | Card.Type);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertEquals(2, klondike.possibleMoves().size());

	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void legal_move_should_throw_exception_if_move_is_null() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], Stock.Empty);
		assertThrows(IllegalArgumentException.class, () -> klondike.isLegalMove(null));
	}

	@Test
	void legal_move_should_return_false_if_moving_card_is_not_in_game() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], Stock.Empty);
		assertFalse(klondike.isLegalMove(new Move(0, Optional.empty())));
	}

	@Test
	void legal_move_should_return_true_if_move_is_in_possible_moves() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three | Card.Colour);
		Column[] columns = new Column[]{new Column(1), new Column(3)};
		columns[0].reveal(Card.Four | Card.Colour, 0);
		columns[1].reveal(Card.Five, 2);
		Klondike klondike = new Klondike(foundations, columns, Stock.Empty);
		assertTrue(klondike.isLegalMove(new Move(Card.Four | Card.Colour, Optional.of(Card.Three | Card.Colour))));
		assertTrue(klondike.isLegalMove(new Move(Card.Four | Card.Colour, Optional.of(Card.Five))));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void make_move_should_throw_exception_is_move_is_null() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], Stock.Empty);
		assertThrows(IllegalArgumentException.class, () -> klondike.makeMove(null));
	}

	@Test
	void make_move_should_throw_exception_if_moving_card_not_contained() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], new Stock(1));
		assertThrows(IllegalMoveException.class, () -> klondike.makeMove(new Move(0, Optional.empty())));
	}

	@Test
	void make_move_should_throw_exception_if_destination_card_not_contained() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		foundations[0].addElement(Card.Three | Card.Colour);
		Klondike klondike = new Klondike(foundations, new Column[]{new Column(0)}, Stock.Empty);
		assertThrows(IllegalMoveException.class, () -> klondike.makeMove(new Move(Card.Three | Card.Colour, Optional.of(Card.Four))));
	}

	@Test
	void make_move_should_move_card_from_stock() {
		Stock stock = new Stock(Card.Ace);
		Klondike klondike = new Klondike(new Foundation[]{new Foundation(), new Foundation()}, new Column[0], stock);
		klondike.makeMove(new Move(Card.Ace, Optional.empty()));
		assertTrue(stock.isEmpty());
	}

	@Test
	void make_move_should_move_card_from_foundation() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.King);
		Klondike klondike = new Klondike(new Foundation[]{foundation}, new Column[]{new Column(0)}, Stock.Empty);
		klondike.makeMove(new Move(Card.King, Optional.empty()));
		assertTrue(foundation.isEmpty());
	}

	@Test
	void make_move_should_move_card_to_column() {
		Column[] columns = new Column[]{new Column(1), new Column(3)};
		columns[0].addElement(Card.Four | Card.Colour);
		columns[1].addElement(Card.Five);
		Klondike klondike = new Klondike(new Foundation[0], columns, Stock.Empty);
		klondike.makeMove(new Move(Card.Four | Card.Colour, Optional.of(Card.Five)));
		assertEquals(Card.Four | Card.Colour, columns[1].lastElement());
	}

	@Test
	void make_move_should_move_card_to_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation(), new Foundation()};
		foundations[0].addElement(Card.Three | Card.Colour);
		Klondike klondike = new Klondike(foundations, new Column[0], new Stock(Card.Four | Card.Colour));
		klondike.makeMove(new Move(Card.Four | Card.Colour, Optional.of(Card.Three | Card.Colour)));
		assertEquals(Card.Four | Card.Colour, foundations[0].peek());
	}

	@Test
	void make_move_should_throw_exception_if_moving_non_king_or_ace_and_no_specified_destination() {
		Foundation[] foundations = new Foundation[]{new Foundation(), new Foundation()};
		foundations[0].addElement(Card.Three | Card.Colour);
		Klondike klondike = new Klondike(foundations, new Column[0], new Stock(Card.Four | Card.Colour));
		assertThrows(IllegalMoveException.class, () -> klondike.makeMove(new Move(Card.Four | Card.Colour, Optional.empty())));
	}

}