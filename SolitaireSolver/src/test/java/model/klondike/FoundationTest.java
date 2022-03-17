package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoundationTest {
	@Test
	void should_return_true_given_ace() {
		assertTrue(new Foundation().canAcceptCard(Card.Ace));
	}

	@Test
	void should_return_false_given_five() {
		assertFalse(new Foundation().canAcceptCard(Card.Five));
	}

	@Test
	void should_return_false_given_ace_and_not_empty() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.Two | Card.Colour);
		assertFalse(foundation.canAcceptCard(Card.Ace | Card.Colour));
	}

	@Test
	void should_return_true_given_next_card() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.Ace);
		assertTrue(foundation.canAcceptCard(Card.Two));
	}

	@Test
	void should_reject_card_if_different_suit() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.Jack | Card.Colour);
		assertFalse(foundation.canAcceptCard(Card.Queen | Card.Type));
	}

	@Test
	void should_reject_card_if_unknown() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.King);
		assertFalse(foundation.canAcceptCard(Card.Unknown));
	}
}