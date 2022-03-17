package model.klondike;

import model.Card;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

	@Test
	void empty_should_not_be_null() {
		assertNotNull(Stock.Empty);
	}

	@Test
	void empty_should_be_empty() {
		assertTrue(Stock.Empty.isEmpty());
	}

	@Test
	void should_not_be_empty() {
		assertFalse(new Stock(Card.Ace).isEmpty());
	}

	@Test
	void should_be_empty() {
		assertTrue(new Stock().isEmpty());
	}

	@Test
	void empty_size_should_be_0() {
		assertEquals(0, Stock.Empty.size());
	}

	@Test
	void size_should_be_2() {
		assertEquals(2, new Stock(Card.Ace, Card.Eight | Card.Colour).size());
	}

	@Test
	void should_throw_exception_if_cards_is_null() {
		assertThrows(IllegalArgumentException.class, () -> new Stock((int[]) null));
	}

	@Test
	void should_throw_exception_if_card_is_null() {
		Collection<Integer> cards = new ArrayList<>(1);
		cards.add(null);
		assertThrows(IllegalArgumentException.class, () -> new Stock(cards));
	}

	@Test
	void should_throw_exception_if_collection_is_null() {
		assertThrows(IllegalArgumentException.class, () -> new Stock((Collection<Integer>) null));
	}

	@Test
	void should_throw_exception_if_unknown_card() {
		assertThrows(IllegalArgumentException.class, () -> new Stock(Card.Unknown | Card.Colour));
	}

	@Test
	void reachableCards_should_not_be_null() {
		assertNotNull(new Stock().reachableCards());
	}

	@Test
	void reachableCards_should_not_be_empty() {
		assertFalse(new Stock(Card.Ace).reachableCards().isEmpty());
	}

	@Test
	void reachable_card_should_not_be_null() {
		assertNotNull(new Stock(Card.Ace).reachableCards().toArray()[0]);
	}

	@Test
	void reachable_card_should_contain_only_card() {
		assertTrue(new Stock(Card.Five).reachableCards().contains(Card.Five));
	}

	@Test
	void reachable_card_should_contain_every_third_card() {
		Collection<Integer> reachableCards = new Stock(
				Card.Five, Card.Three | Card.Colour, Card.Seven,
				Card.Ten | Card.Type, Card.Three, Card.Ace).reachableCards();
		assertTrue(reachableCards.contains(Card.Seven), "Deck does not contain third card.\nDeck: " + reachableCards);
		assertTrue(reachableCards.contains(Card.Ace), "Deck does not contain sixth card.\nDeck: " + reachableCards);
	}

	@Test
	void reachable_card_should_only_contain_every_third_card() {
		Set<Integer> reachableCards = new Stock(
				Card.Five, Card.Three | Card.Colour, Card.Seven,
				Card.Ten | Card.Type, Card.Three, Card.Ace).reachableCards();
		assertEquals(Set.of(Card.Seven, Card.Ace), reachableCards);
	}

	@Test
	void should_throw_exception_if_duplicate_cards() {
		assertThrows(IllegalArgumentException.class, () -> new Stock(Card.Ace, Card.Ace));
	}

	@Test
	void fourth_card_should_be_reachable() {
		assertTrue(new Stock(Card.Ace, Card.Two, Card.Three, Card.Four).reachableCards().contains(Card.Four));
	}

	@Test
	void second_card_should_be_reachable() {
		assertTrue(new Stock(Card.Ace, Card.Two).reachableCards().contains(Card.Two));
	}

	@Test
	void take_should_remove_card() {
		Stock stock = new Stock(Card.Ace);
		stock.take(Card.Ace);
		assertTrue(stock.isEmpty());
	}

	@Test
	void take_should_return_card() {
		Stock stock = new Stock(Card.Ace);
		assertEquals(Card.Ace, stock.take(Card.Ace));
	}

	@Test
	void take_should_throw_exception_if_not_containing_card() {
		Stock stock = new Stock(Card.Ace);
		assertThrows(IllegalArgumentException.class, () -> stock.take(Card.Two));
	}

	@Test
	void taking_unreachable_card_should_throw_exception() {
		Stock stock = new Stock(Card.Ace, Card.Two);
		assertThrows(IllegalArgumentException.class, () -> stock.take(Card.Ace));
	}

	@Test
	void given_five_card_fourth_should_be_reachable_after_taking_third() {
		Stock stock = new Stock(Card.Ace, Card.Two, Card.Three, Card.Four, Card.Five);
		stock.take(Card.Three);
		assertTrue(stock.reachableCards().contains(Card.Four));
	}

	@Test
	void card_below_taken_should_be_reachable() {
		Stock stock = new Stock(Card.Ace, Card.Two, Card.Three, Card.Four);
		stock.take(Card.Three);
		assertTrue(stock.reachableCards().contains(Card.Two));
	}

	@Test
	void taking_should_keep_every_third_from_waste_reachable() {
		Stock stock = new Stock(Card.Ace, Card.Two, Card.Three, Card.Four, Card.Five, Card.Six, Card.Seven, Card.Eight, Card.Nine, Card.Ten);
		stock.take(Card.Three);
		assertTrue(stock.reachableCards().contains(Card.Six));
		assertTrue(stock.reachableCards().contains(Card.Nine));
	}

	@Test
	void taking_after_waste_should_make_unreachable_every_third_until_new_waste() {
		Stock stock = new Stock(Card.Ace, Card.Two, Card.Three, Card.Four, Card.Five, Card.Six, Card.Seven, Card.Eight, Card.Nine, Card.Ten);
		stock.take(Card.Three);
		stock.take(Card.Nine);
		assertFalse(stock.reachableCards().contains(Card.Six));
	}

	@Test
	void taking_from_waste_should_only_remove_taken_card_from_reachable() {
		Stock stock = new Stock(Card.Ace, Card.Two, Card.Three, Card.Four, Card.Five, Card.Six, Card.Seven, Card.Eight, Card.Nine, Card.Ten);
		stock.take(Card.Six);
		Set<Integer> reachableCards = stock.reachableCards();
		reachableCards.remove(stock.take(Card.Five));
		reachableCards.add(Card.Four);
		assertEquals(reachableCards, stock.reachableCards());
	}

}