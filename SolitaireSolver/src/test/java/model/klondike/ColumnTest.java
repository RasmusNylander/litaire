package model.klondike;

import model.Card;
import model.IllegalMoveException;
import org.junit.jupiter.api.Test;

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
		assertEquals(Card.Unknown, column.get(0));
		assertEquals(Card.Unknown, column.get(1));
	}

	@Test
	void should_have_size_two() {
		Column column = new Column(1, Card.Ace);
		assertEquals(2, column.getNumberOfCards());
	}

	@Test
	void should_contain_ace() {
		Column column = new Column(1, Card.Ace);
		assertTrue(column.contains(Card.Ace));
	}

	@Test
	void reveal_should_change_card() {
		Column column = new Column(1);
		Integer top = column.lastElement();
		column.reveal(Card.Ace, 0);
		assertNotEquals(top, column.lastElement());
	}

	@Test
	void reveal_should_throw_exception_if_card_is_known() {
		Column column = new Column(1);
		Integer top = column.lastElement();
		column.reveal(Card.Ace, 0);
		assertThrows(Exception.class, () -> column.reveal(Card.Ace, 0));
	}

	@Test
	void reveal_should_update_card() {
		Column column = new Column(5);
		Integer top = column.lastElement();
		column.reveal(Card.Ace, 4);
		column.reveal(Card.Jack | Card.Type, 2);
		assertEquals(column.get(4), Card.Ace);
		assertEquals(column.get(2), Card.Jack | Card.Type);
	}

	@Test
	void should_return_optional_empty_as_destination_when_empty() {
		assertEquals(Optional.empty(), new Column(0).asDestination());
	}

	@Test
	void should_return_top_card_as_destination_when_not_empty() {
		Column column = new Column(0);
		column.addElement(Card.Six | Card.Colour);
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
	void should_not_accept_card_if_not_previous_card() {
		Column column = new Column(0);
		column.addElement(Card.Seven);
		assertFalse(column.canAcceptCard(Card.Five | Card.Colour));
	}

	@Test
	void should_accept_non_standard_king_when_empty() {
		assertTrue(new Column(0).canAcceptCard(Card.King | Card.Type), "Does not accept alternate suit-type");
		assertTrue(new Column(0).canAcceptCard(Card.King | Card.Colour), "Does not accept alternate suit-colour");
	}

	@Test
	void should_not_accept_card_if_same_colour() {
		Column column = new Column(0);
		column.addElement(Card.Seven);
		assertFalse(column.canAcceptCard(Card.Six));
	}

	@Test
	void reachable_cards_should_not_be_null() {
		assertNotNull(new Column(0).reachableCards());
	}

	@Test
	void reachable_cards_should_not_be_empty() {
		Column column = new Column(1);
		column.reveal(Card.Ace, 0);
		assertFalse(column.reachableCards().isEmpty());
	}

	@Test
	void reachable_cards_should_contain_multiple_cards() {
		Column column = new Column(2);
		column.reveal(Card.Ace, 0);
		column.reveal(Card.Two, 1);
		assertEquals(2, column.reachableCards().size());
	}

	@Test
	void reachable_cards_should_contain_last_card() {
		Column column = new Column(2);
		column.reveal(Card.Two, 1);
		assertTrue(column.reachableCards().contains(Card.Two));
	}

	@Test
	void reachable_cards_should_not_contain_unknown_cards() {
		Column column = new Column(2);
		column.reveal(Card.Ace, 1);
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
	void receive_should_not_modify_column_if_throws_exception() {
		Column column = new Column(0);
		assertThrows(IllegalMoveException.class, () -> column.receive(Card.King, Card.Queen, Card.Jack, Card.Four));
		assertEquals(new Column(0), column);
	}

	@Test
	void move_should_modify_column() {
		Column column = new Column(0);
		column.addElement(Card.King);
		column.move(Card.King, new MockCardContainer());
		assertEquals(new Column(0), column);
	}

	@Test
	void move_should_call_receive_on_destination() {
		Column column = new Column(1);
		column.addElement(Card.Nine);
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
		Column column = new Column(0);
		column.addElement(Card.King);
		column.addElement(Card.Queen);
		column.move(Card.King, new MockCardContainer());
		assertEquals(new Column(0), column);
	}

	@Test
	void move_should_throw_exception_if_destination_cannot_receive() {
		Column column = new Column(0);
		column.addElement(Card.King);
		MockCardContainer destination = new MockCardContainer();
		destination.canReceive = false;
		assertThrows(IllegalMoveException.class, () -> column.move(Card.King, destination));
	}

	@Test
	void move_should_not_modify_column_if_throws_exception() {
		Column column = new Column(0);
		column.addElement(Card.King);
		column.addElement(Card.Queen);
		MockCardContainer destination = new MockCardContainer();
		destination.canReceive = false;
		assertThrows(IllegalMoveException.class, () -> column.move(Card.King, destination));

		Column original = new Column(0);
		original.addElement(Card.King);
		original.addElement(Card.Queen);
		assertEquals(original, column);
	}

	@Test
	void undo_should_undo_move_single_card() {
		Column column = new Column(0), original = new Column(0);
		column.addElement(Card.King | Card.Colour);
		original.addElement(Card.King | Card.Colour);
		column.addElement(Card.Queen);
		original.addElement(Card.Queen);
		MoveMetaInformation info = column.move(Card.Queen, new MockCardContainer());
		column.undo(Card.Queen, info);
		assertEquals(original, column);
	}

	@Test
	void undo_should_undo_move_multiple_cards() {
		Column column = new Column(0), original = new Column(0);
		column.addElement(Card.King | Card.Colour);
		original.addElement(Card.King | Card.Colour);
		column.addElement(Card.Queen);
		original.addElement(Card.Queen);
		Column column2 = new Column(0);
		MoveMetaInformation info = column.move(Card.King | Card.Colour, column2);
		column.undo(Card.King | Card.Colour, info);
		assertEquals(original, column, "Column was changed after move and undo.");
		assertTrue(column2.isEmpty(), "Destination was not restored after undo.");
	}

	@Test
	void undo_should_not_throw_exception_if_moving_card_onto_unknown() {
		Column column = new Column(1);
		column.addElement(Card.Ace);
		MoveMetaInformation info = column.move(Card.Ace, new MockCardContainer());
		assertDoesNotThrow(() -> column.undo(Card.Ace, info));
	}

}