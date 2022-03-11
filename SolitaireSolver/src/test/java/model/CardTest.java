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

	@Test
	void string_should_not_be_null() {
		assertNotNull(Card.asString(0));
	}

	@Test
	void string_should_not_be_blank() {
		assertFalse(Card.asString(0).isBlank());
	}

	@Test
	void string_should_contain_number() {
		assertTrue(Card.asString(Card.Seven).contains("7"));
		assertTrue(Card.asString(Card.Two).contains("2"));
	}

	@Test
	void string_should_contain_suit() {
		assertTrue(Card.asString(Card.Colour).matches(".*[♠♣♥♦].*"));
		assertTrue(Card.asString(Card.Nine | Card.Type).matches(".*[♠♣♥♦].*"));
		assertTrue(Card.asString(Card.Jack).matches(".*[♠♣♥♦].*"));
	}

	@Test
	void strings_should_be_same_size() {
		assertEquals(Card.asString(Card.King | Card.Colour | Card.Type).length(), Card.asString(Card.Ace | Card.Colour).length());
		assertEquals(Card.asString(Card.Ten | Card.Type).length(), Card.asString(Card.Nine | Card.Colour).length());
	}

	@Test
	void unknown_cards_should_have_string_representation() {
		assertDoesNotThrow(() -> {
			Card.asString(Card.Unknown);
		});
	}

}