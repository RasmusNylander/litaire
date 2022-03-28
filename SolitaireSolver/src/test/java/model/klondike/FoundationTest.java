package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FoundationTest {
	@Test
	void push_should_change_contents() {
		Foundation foundation = new Foundation();
		foundation.push(Card.Ace);
		assertNotEquals(new Foundation(), foundation);
	}

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

	@Test
	void should_throw_exception_when_pushing_not_accepted_card() {
		assertThrows(IllegalArgumentException.class, () -> new Foundation().push(Card.Unknown));
	}

	@Test
	void should_return_optional_empty_as_destination_when_empty() {
		assertEquals(Optional.empty(), new Foundation().asDestination());
	}

	@Test
	void should_return_top_card_as_destination_when_not_empty() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.Six | Card.Colour);
		assertEquals(Optional.of(Card.Six | Card.Colour), foundation.asDestination());
	}

}