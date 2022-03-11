package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

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

}




/*
	@Test
	void unknownCards_should_not_return_null() {
		Klondaike klondike = new Klondaike(new Stack[0], new Vector[0],  new ArrayList<>(0));
		assertNotNull(klondike.unknownCards());
	}

	@Test
	void unknownCards_should_return_0_when_all_cards_are_known() {
		Klondaike klondike = new Klondaike(new Stack[0], new Vector[0],  Arrays.asList(Card.fullDeck()));
		assertEquals(0, klondike.unknownCards().length);
	}

	@Test
	void unknownCards_should_return_unknown_cards() {
		Integer[] deck = Card.fullDeck();
		Klondaike klondike = new Klondaike(new Stack[0], new Vector[0],  Arrays.asList(deck));
		List<Integer> unknownCards = new ArrayList<Integer>();

		unknownCards.add(deck[0]); deck[0] = Card.Unknown;
		assertArrayEquals(unknownCards.toArray(), klondike.unknownCards());

		unknownCards.add(deck[4]); deck[4] = Card.Unknown;
		unknownCards.add(deck[38]); deck[38] = Card.Unknown;
		unknownCards.add(deck[17]); deck[17] = Card.Unknown;
		unknownCards.add(deck[19]); deck[19] = unknownCards.get(0); unknownCards.remove(0);
		unknownCards.sort(Integer::compare);
		assertArrayEquals(unknownCards.toArray(), Arrays.stream(klondike.unknownCards()).sorted().toArray());
	}

	@Test
	void unknownGame_should_not_be_null() {
		assertNotNull(Klondaike.UnknownGame());
	}

	@Test
	void unknownGame_should_have_52_unknown_cards() {
		assertEquals(52, Klondaike.UnknownGame().unknownCards().length);
	}

	@Test
	void unknownGame_should_have_7_columns() {
		assertEquals(7, Klondaike.UnknownGame().getColumns().length);
	}

	@Test
	void unknownGame_should_have_4_foundations() {
		assertEquals(7, Klondaike.UnknownGame().getColumns().length);
	}
	*/