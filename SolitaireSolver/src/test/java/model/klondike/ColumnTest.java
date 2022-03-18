package model.klondike;

import model.Card;
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

	@Test
	void should_contain_unknown_card() {
		Column column = new Column(2);
		assertEquals(Card.Unknown, column.get(0));
		assertEquals(Card.Unknown, column.get(1));
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
	void should_not_accept_card_if_same_colour() {
		Column column = new Column(0);
		column.addElement(Card.Seven);
		assertFalse(column.canAcceptCard(Card.Six));
	}

}