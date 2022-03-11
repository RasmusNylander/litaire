package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

	@Test
	void deck_should_not_be_null() {
		assertNotNull(Card.fullDeck());
	}

	@Test
	void deck_should_not_be_empty() {
		assertNotEquals(0, Card.fullDeck().length);
	}

	@Test
	void num_cards_should_be_52() {
		assertEquals(52, Card.fullDeck().length);
	}

	@Test
	void deck_should_not_contain_unknown() {
		Integer[] deck = Card.fullDeck();
		assertNotEquals(Card.Unknown, deck[0]);
		assertNotEquals(Card.Unknown, deck[16]);
	}

	@Test
	void deck_should_contain_different_cards() {
		Integer[] deck = Card.fullDeck();
		assertNotEquals(deck[0], deck[32]);
		assertNotEquals(deck[3], deck[4]);
	}

}