package model.klondike;

import model.Card;
import model.Move;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class KlondikeTest {

	@Test
	void new_game_should_not_be_null() {
		assertNotNull(Klondike.newGame());
	}

	@Test
	void new_game_foundations_should_not_be_null() {
		assertNotNull(Klondike.newGame().columns());
	}

	@Test
	void new_game_should_have_4_foundations() {
		assertEquals(4, Klondike.newGame().foundations().length);
	}

	@Test
	void new_game_should_have_empty_foundations() {
		Foundation[] foundations = Klondike.newGame().foundations();
		assertTrue(foundations[0].isEmpty());
		assertTrue(foundations[3].isEmpty());
	}

	@Test
	void new_game_columns_should_not_be_null() {
		assertNotNull(Klondike.newGame().columns());
	}

	@Test
	void new_game_should_have_7_columns() {
		assertEquals(7, Klondike.newGame().columns().length);
	}

	@Test
	void new_game_columns_should_contain_cards() {
		Column[] columns = Klondike.newGame().columns();
		assertFalse(columns[3].isEmpty());
		assertFalse(columns[1].isEmpty());
	}

	@Test
	void new_game_columns_should_contain_unknown_cards() {
		Column[] columns = Klondike.newGame().columns();
		assertTrue(columns[6].contains(Card.Unknown));
		assertTrue(columns[2].contains(Card.Unknown));
		assertTrue(columns[3].contains(Card.Unknown));
	}

	@Test
	void new_game_columns_should_contain_only_unknown_cards() {
		Column[] columns = Klondike.newGame().columns();
		assertFalse(columns[1].contains(Card.Ace));
		assertFalse(columns[6].contains(Card.Three | Card.Type));
		assertFalse(columns[4].contains(Card.Queen | Card.Colour));
	}

	@Test
	void new_game_stock_should_not_be_null() {
		assertNotNull(Klondike.newGame().stock());
	}

	@Test
	void new_game_stock_should_not_be_empty() {
		assertFalse(Klondike.newGame().stock().isEmpty());
	}

	@Test
	void new_game_stock_should_be_24_cards() {
		assertEquals(24, Klondike.newGame().stock().size());
	}

	@Test
	void possible_moves_should_not_be_null() {
		assertNotNull(Klondike.newGame().possibleMoves());
	}

	@Test
	void possible_moves_should_be_empty() {
		Klondike klondike = new Klondike(new Foundation[0], new Column[0], new Stock(0)); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_from_column_to_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Column[] columns = new Column[]{new Column(1)};
		columns[0].reveal(Card.Ace | Card.Colour, 0);
		Klondike klondike = new Klondike(foundations, columns, new Stock(0)); // This is cumbersome, please change
		assertFalse(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_not_be_possible_to_move_two_to_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation()};
		Column[] columns = new Column[]{new Column(1)};
		columns[0].reveal(Card.Two | Card.Type, 0);
		Klondike klondike = new Klondike(foundations, columns, new Stock(0)); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().isEmpty());
	}

	@Test
	void should_be_possible_to_move_onto_previous_card_in_foundation() {
		Foundation[] foundations = new Foundation[]{new Foundation(), new Foundation()};
		foundations[0].push(Card.Ace);
		foundations[1].push(Card.Jack | Card.Colour);
		Column[] columns = new Column[]{new Column(1), new Column(2)};
		columns[0].reveal(Card.Two, 0);
		columns[1].reveal(Card.Queen | Card.Colour, 1);
		Klondike klondike = new Klondike(foundations, columns, new Stock(0)); // This is cumbersome, please change
		assertTrue(klondike.possibleMoves().containsAll(List.of(
				new Move(Card.Two, Optional.of(Card.Ace)),
				new Move(Card.Queen | Card.Colour, Optional.of(Card.Jack | Card.Colour))
		)));
	}
}