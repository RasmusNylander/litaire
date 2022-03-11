package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

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

}