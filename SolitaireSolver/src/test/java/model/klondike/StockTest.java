package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

	@Test
	void should_be_empty() {
		assertTrue(new Stock(0).isEmpty());
	}

	@Test
	void should_not_be_empty() {
		assertFalse(new Stock(1).isEmpty());
	}

	@Test
	void should_contain_unknown_card() {
		Stock stock = new Stock(2);
		assertEquals(Card.Unknown, stock.get(0));
		assertEquals(Card.Unknown, stock.get(1));
	}

}