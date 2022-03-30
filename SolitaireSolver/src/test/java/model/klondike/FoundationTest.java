package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.junit.jupiter.api.Test;

import java.util.List;
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


	@Test
	void reachable_cards_should_be_not_be_null() {
		assertNotNull(new Foundation().reachableCards());
	}

	@Test
	void reachable_cards_should_be_empty_when_empty() {
		assertTrue(new Foundation().reachableCards().isEmpty());
	}

	@Test
	void reachable_cards_should_not_be_empty_when_empty() {
		Foundation foundation = new Foundation();
		foundation.addElement(Card.Ace | Card.Colour);
		assertFalse(foundation.reachableCards().isEmpty());
	}

	@Test
	void reachable_cards_should_only_be_size_one() {
		Foundation foundation = new Foundation();
		foundation.addAll(List.of(Card.Ace | Card.Colour, Card.Two | Card.Colour, Card.Three | Card.Colour));
		assertEquals(1, foundation.reachableCards().size());
	}

	@Test
	void reachable_cards_should_contain_last_card() {
		Foundation foundation = new Foundation();
		foundation.addAll(List.of(Card.Ace | Card.Colour, Card.Two | Card.Colour, Card.Three | Card.Colour));
		assertTrue(foundation.reachableCards().contains(Card.Three | Card.Colour));
	}

	@Test
	void receive_cards_should_throw_exception_when_cannot_accept_card() {
		Foundation foundation = new Foundation();
		assertThrows(IllegalMoveException.class, () -> new Foundation().receive(Card.Unknown), "Cannot receive Unknown card");
		foundation.addElement(Card.Five | Card.Colour);
		assertThrows(IllegalMoveException.class, () -> new Foundation().receive((Card.Six | Card.Type)));
	}

	@Test
	void receive_cards_should_add_card_to_foundation() {
		Foundation foundation = new Foundation();
		foundation.receive(Card.Ace | Card.Colour);
		assertEquals(1, foundation.size());
	}

	@Test
	void state_should_be_unchanged_when_cannot_receive_card() {
		Foundation foundation = new Foundation(), original = new Foundation();
		original.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		foundation.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		assertThrows(Exception.class, () -> foundation.receive(Card.Four, Card.Five, Card.King), "Assumption: Cannot receive cards, and should thus throw exception.\nError: No exception thrown.");
		assertEquals(original, foundation);
	}

	@Test
	void move_should_throw_exception_if_card_is_not_reachable() {
		Foundation foundation = new Foundation();
		foundation.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		assertThrows(IllegalMoveException.class, () -> foundation.move(Card.Two, new MockCardContainer()));
	}

	@Test
	void move_should_call_receive_on_destination_with_moved_card() {
		Foundation foundation = new Foundation();
		foundation.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		MockCardContainer destination = new MockCardContainer();
		foundation.move(Card.Three, destination);
		assertTrue(destination.receivedWasCalled);
	}

	@Test
	void move_should_modify_foundation() {
		Foundation foundation = new Foundation(), original = new Foundation();
		foundation.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		original.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		foundation.move(Card.Three, new MockCardContainer());
		assertNotEquals(original, foundation);
	}

	@Test
	void undo_should_undo_move() {
		Foundation foundation = new Foundation(), original = new Foundation();
		foundation.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		original.addAll(List.of(Card.Ace, Card.Two, Card.Three));
		MoveMetaInformation info = foundation.move(Card.Three, new MockCardContainer());
		foundation.undo(Card.Three, info);
		assertEquals(original, foundation);
	}

}