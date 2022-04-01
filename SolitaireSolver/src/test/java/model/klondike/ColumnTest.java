package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ColumnTest {

	@Test
	void should_be_empty() {
		assertTrue(new Column(0).isEmpty());
	}

	@Test
	void should_not_be_empty() {
		assertFalse(new Column(1).isEmpty());
	}

	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	@Test
	void should_contain_unknown_card() {
		Column column = new Column(2);
		assertEquals(Card.Unknown, column.lastCard());
	}

	@Test
	void should_have_size_two() {
		Column column = new Column(1, Card.Ace);
		assertEquals(2, column.getNumberOfCards());
	}

	@Test
	void should_throw_exception_if_card_rank_order_invalid() {
		assertThrows(IllegalArgumentException.class, () -> new Column(0, Card.Seven, Card.Nine | Card.Colour));
	}

	@Test
	void should_throw_exception_if_card_colour_order_invalid() {
		assertThrows(IllegalArgumentException.class, () -> new Column(0, Card.Seven, Card.Six));
	}

	@Test
	void last_card_should_be_ace_when_only_contains_ace() {
		Column column = new Column(1, Card.Ace);
		assertEquals(Card.Ace, column.lastCard());
	}

	@Test
	void last_card_should_be_nine_when_nine_is_last_card() {
		Column column = new Column(5, Card.Jack, Card.Ten | Card.Colour, Card.Nine);
		assertEquals(Card.Nine, column.lastCard());
	}

	@Test
	void last_card_should_throw_exception_when_empty() {
		Column column = new Column(0);
		assertThrows(NoSuchElementException.class, column::lastCard);
	}


	@Test
	void reveal_should_change_card() {
		Column column = new Column(1);
		column.reveal(Card.Ace, 0);
		assertNotEquals(new Column(1), column);
	}

	@Test
	void reveal_should_throw_exception_if_card_is_known() {
		Column column = new Column(1, Card.Seven);
		assertThrows(IllegalArgumentException.class, () -> column.reveal(Card.Ace, 1));
	}

	@Test
	void reveal_should_update_card() {
		Column column = new Column(5);
		column.reveal(Card.Ace, 4);
		column.reveal(Card.Two | Card.Colour, 3);
		assertEquals(new Column(3, Card.Two | Card.Colour, Card.Ace), column);
	}

	@Test
	void should_return_optional_empty_as_destination_when_empty() {
		assertEquals(Optional.empty(), new Column(0).asDestination());
	}

	@Test
	void should_return_top_card_as_destination_when_not_empty() {
		Column column = new Column(0, Card.Six | Card.Colour);
		assertEquals(Optional.of(Card.Six | Card.Colour), column.asDestination());
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	void should_throw_exception_if_card_is_unknown() {
		Column column = new Column(1);
		assertThrows(IllegalStateException.class, column::asDestination);
	}

	@Test
	void should_accept_card_if_king() {
		assertTrue(new Column(0).canAcceptCard(Card.King));
	}

	@Test
	void should_not_accept_card_if_king_and_not_empty() {
		assertFalse(new Column(1).canAcceptCard(Card.King));
	}

	@Test
	void should_accept_coloured_king_when_empty() {
		assertTrue(new Column(0).canAcceptCard(Card.King | Card.Colour), "Does not accept alternate suit-colour");
	}

	@Test
	void should_accept_alternate_king_type_when_empty() {
		assertTrue(new Column(0).canAcceptCard(Card.King | Card.Type), "Does not accept alternate suit-type");
	}

	@Test
	void should_not_accept_card_if_not_previous_card() {
		Column column = new Column(0, Card.Seven);
		assertFalse(column.canAcceptCard(Card.Five | Card.Colour));
	}

	@Test
	void should_not_accept_card_if_same_colour() {
		Column column = new Column(0, Card.Seven);
		assertFalse(column.canAcceptCard(Card.Six));
	}

	@Test
	void reachable_cards_should_not_be_null() {
		assertNotNull(new Column(0).reachableCards());
	}

	@Test
	void reachable_cards_should_not_be_empty() {
		Column column = new Column(0, Card.Ace);
		assertFalse(column.reachableCards().isEmpty());
	}

	@Test
	void reachable_cards_should_contain_multiple_cards() {
		Column column = new Column(0, Card.Two, Card.Ace | Card.Colour);
		assertEquals(2, column.reachableCards().size());
	}

	@Test
	void reachable_cards_should_contain_last_card() {
		Column column = new Column(1, Card.Two);
		assertTrue(column.reachableCards().contains(Card.Two));
	}

	@Test
	void reachable_cards_should_not_contain_unknown_cards() {
		Column column = new Column(1, Card.Ace);
		assertEquals(1, column.reachableCards().size());
	}

	@Test
	void receive_should_throw_exception_when_cannot_accept_card() {
		Column column = new Column(0);
		assertThrows(IllegalMoveException.class, () -> column.receive(Card.Ace));
	}

	@Test
	void receive_should_modify_column() {
		Column column = new Column(0);
		column.receive(Card.King);
		assertNotEquals(new Column(0), column);
	}

	@Test
	void should_contain_received_cards() {
		Column column = new Column(0);
		column.receive(Card.King, Card.Queen | Card.Colour);
		assertEquals(new Column(0, Card.King, Card.Queen | Card.Colour), column);
	}

	@Test
	void receive_should_not_modify_column_if_throws_exception() {
		Column column = new Column(0);
		assertThrows(IllegalMoveException.class, () -> column.receive(Card.King, Card.Queen, Card.Jack, Card.Four));
		assertEquals(new Column(0), column);
	}

	@Test
	void move_should_modify_column() {
		Column column = new Column(0, Card.King);
		Column original = new Column(column);
		column.move(Card.King, new MockCardContainer());
		assertNotEquals(original, column);
	}

	@Test
	void move_should_call_receive_on_destination() {
		Column column = new Column(1, Card.Nine);
		MockCardContainer destination = new MockCardContainer();
		column.move(Card.Nine, destination);
		assertTrue(destination.receivedWasCalled);
	}

	@Test
	void move_should_throw_exception_if_card_not_reachable() {
		Column column = new Column(0);
		assertThrows(IllegalMoveException.class, () -> column.move(Card.Seven, new MockCardContainer()));
	}

	@Test
	void move_should_move_cards_below() {
		Column column = new Column(0, Card.Jack | Card.Colour, Card.Ten);
		column.move(Card.Jack | Card.Colour, new MockCardContainer());
		assertEquals(new Column(0), column);
	}

	@Test
	void move_should_throw_exception_if_destination_cannot_receive() {
		Column column = new Column(3, Card.Six);
		MockCardContainer destination = new MockCardContainer();
		destination.canReceive = false;
		assertThrows(IllegalMoveException.class, () -> column.move(Card.Six, destination));
	}

	@Test
	void move_should_not_modify_column_if_throws_exception() {
		Column column = new Column(0, Card.Seven, Card.Six | Card.Colour);
		Column original = new Column(column);
		MockCardContainer destination = new MockCardContainer();
		destination.canReceive = false;
		assertThrows(IllegalMoveException.class, () -> column.move(Card.King, destination));
		assertEquals(original, column);
	}

	@Test
	void undo_should_undo_move_single_card() {
		Column column = new Column(0, Card.King | Card.Colour, Card.Queen);
		Column original = new Column(column);
		MoveMetaInformation info = column.move(Card.Queen, new MockCardContainer());
		column.undo(Card.Queen, info);
		assertEquals(original, column);
	}

	@Test
	void undo_should_undo_move_multiple_cards() {
		Column original = new Column(0, Card.King | Card.Colour, Card.Queen);
		Column copy = new Column(original);
		Column column2 = new Column(0);
		MoveMetaInformation info = copy.move(Card.King | Card.Colour, column2);
		copy.undo(Card.King | Card.Colour, info);
		assertEquals(original, copy, "Column was changed after move and undo.");
	}

	@Test
	void undo_should_restore_state_of_destination() {
		Column original = new Column(0, Card.King | Card.Colour, Card.Queen);
		Column copy = new Column(original);
		Column column2 = new Column(0);
		MoveMetaInformation info = copy.move(Card.King | Card.Colour, column2);
		copy.undo(Card.King | Card.Colour, info);
		assertTrue(column2.isEmpty(), "Destination was not restored after undo.");
	}

	@Test
	void undo_should_not_throw_exception_if_moving_card_onto_unknown() {
		Column column = new Column(1, Card.Ace);
		MoveMetaInformation info = column.move(Card.Ace, new MockCardContainer());
		assertDoesNotThrow(() -> column.undo(Card.Ace, info));
	}

	@Test
	void should_throw_exception_if_20_unknown_cards() {
		assertThrows(IllegalArgumentException.class, () -> new Column(20), "Column should not be created with more than 19 Unknown cards.");
	}

	@Test
	void should_throw_exception_if_20_total_cards() {
		assertThrows(IllegalArgumentException.class, () -> new Column(18, Card.Ace, Card.Two | Card.Colour), "Column should not be created with more than 19 total cards.");
	}

	@Test
	void should_throw_exception_if_known_cards_contain_unknown_card() {
		assertThrows(IllegalArgumentException.class, () -> new Column(0, Card.Unknown, Card.Ace), "Column should not be created with Unknown card in known cards.");
	}
}