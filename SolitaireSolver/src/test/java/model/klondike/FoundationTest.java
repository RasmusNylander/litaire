package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class FoundationTest {
	@Test
	void receive_should_change_contents() {
		Foundation foundation = new Foundation();
		foundation.receive(Card.Ace);
		assertNotEquals(new Foundation(), foundation);
	}

	@Test
	void foundation_copy_constructor_should_return_equal_foundation() {
		Foundation foundation = new Foundation(Card.Ace | Card.Colour, Card.Two | Card.Colour, Card.Three | Card.Colour);
		Foundation copy = new Foundation(foundation);
		assertEquals(foundation, copy);
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
		Foundation foundation = new Foundation(Card.Two | Card.Colour);
		assertFalse(foundation.canAcceptCard(Card.Ace | Card.Colour));
	}

	@Test
	void should_return_true_given_next_card() {
		Foundation foundation = new Foundation(Card.Ace);
		assertTrue(foundation.canAcceptCard(Card.Two));
	}

	@Test
	void should_reject_card_if_different_suit() {
		Foundation foundation = new Foundation(Card.Ace);
		assertFalse(foundation.canAcceptCard(Card.Two | Card.Type));
	}

	@Test
	void should_reject_card_if_unknown() {
		Foundation foundation = new Foundation(Card.Ace);
		assertFalse(foundation.canAcceptCard(Card.Unknown));
	}

	@Test
	void should_throw_exception_when_receiving_not_accepted_card() {
		assertThrows(IllegalArgumentException.class, () -> new Foundation().receive(Card.Unknown));
	}

	@Test
	void should_throw_exception_when_receiving_invalid_sequence() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two);
		assertThrows(IllegalMoveException.class, () -> foundation.receive(Card.Three, Card.Four | Card.Colour, Card.Five));
	}

	@Test
	void should_remain_unchanged_when_receiving_unacceptable_card() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two);
		Foundation original = new Foundation(foundation);
		assertThrows(IllegalMoveException.class, () -> foundation.receive(Card.Three | Card.Colour));
		assertEquals(original, foundation);
	}

	@Test
	void should_remain_unchanged_when_receiving_invalid_sequence() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two);
		Foundation original = new Foundation(foundation);
		assertThrows(IllegalMoveException.class, () -> foundation.receive(Card.Three, Card.Four | Card.Colour, Card.Five));
		assertEquals(original, foundation);
	}

	@Test
	void should_return_optional_empty_as_destination_when_empty() {
		assertEquals(Optional.empty(), new Foundation().asDestination());
	}

	@Test
	void should_return_top_card_as_destination_when_not_empty() {
		Foundation foundation = new Foundation(Card.Six | Card.Colour);
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
		Foundation foundation = new Foundation(Card.Ace | Card.Colour);
		assertFalse(foundation.reachableCards().isEmpty());
	}

	@Test
	void reachable_cards_should_only_be_size_one() {
		Foundation foundation = new Foundation(Card.Ace | Card.Colour, Card.Two | Card.Colour, Card.Three | Card.Colour);
		assertEquals(1, foundation.reachableCards().size());
	}

	@Test
	void reachable_cards_should_contain_last_card() {
		Foundation foundation = new Foundation(Card.Ace | Card.Colour, Card.Two | Card.Colour, Card.Three | Card.Colour);
		assertTrue(foundation.reachableCards().contains(Card.Three | Card.Colour));
	}

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@Test
	void receive_cards_should_throw_exception_when_cannot_accept_card() {
		assertThrows(IllegalMoveException.class, () -> new Foundation().receive(Card.Unknown), "Cannot receive Unknown card"); // TODO: Move to separate test

		Foundation foundation = new Foundation(Card.Ace | Card.Colour);
		assertThrows(IllegalMoveException.class, () -> new Foundation().receive((Card.Six | Card.Type)));
	}

	@Test
	void receive_cards_should_add_card_to_foundation() {
		Foundation foundation = new Foundation();
		foundation.receive(Card.Ace | Card.Colour);
		assertEquals(1, foundation.getNumberOfCards());
	}

	@Test
	void state_should_be_unchanged_when_cannot_receive_card() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two, Card.Three);
		Foundation original = new Foundation(foundation);
		assertThrows(Exception.class, () -> foundation.receive(Card.Four, Card.Five, Card.King), "Assumption: Cannot receive cards, and should thus throw exception.\nError: No exception thrown.");
		assertEquals(original, foundation);
	}

	@Test
	void move_should_throw_exception_if_card_is_not_reachable() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two, Card.Three);
		assertThrows(IllegalMoveException.class, () -> foundation.move(Card.Two, new MockCardContainer()));
	}

	@Test
	void move_should_call_receive_on_destination_with_moved_card() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two, Card.Three);
		MockCardContainer destination = new MockCardContainer();
		foundation.move(Card.Three, destination);
		assertTrue(destination.receivedWasCalled);
	}

	@Test
	void move_should_modify_foundation() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two, Card.Three);
		Foundation original = new Foundation(foundation);
		foundation.move(Card.Three, new MockCardContainer());
		assertNotEquals(original, foundation);
	}

	@Test
	void undo_should_undo_move() {
		Foundation foundation = new Foundation(Card.Ace, Card.Two, Card.Three);
		Foundation original = new Foundation(foundation);
		MoveMetaInformation info = foundation.move(Card.Three, new MockCardContainer());
		foundation.undo(Card.Three, info);
		assertEquals(original, foundation);
	}

	@Test
	void foundation_should_have_size_of_rank_given_one_card() {
		assertEquals(1, new Foundation(Card.Ace | Card.Colour).getNumberOfCards());
		assertEquals(7, new Foundation(Card.Seven | Card.Colour).getNumberOfCards());
	}

	@Test
	void should_throw_exception_if_last_card_is_unknown() {
		assertThrows(IllegalArgumentException.class, () -> new Foundation(Card.Unknown));
	}

	@Test
	void should_throw_exception_if_last_card_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> new Foundation(97));
	}

}