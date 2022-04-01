package model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MoveTest {

	@Test
	void should_not_throw_exception() {
		assertDoesNotThrow(() -> new Move(Card.Two, Optional.of(Card.Ace)));
		assertDoesNotThrow(() -> new Move(Card.Ace, Optional.empty()));
	}

	@Test
	void should_throw_exception_if_moved_card_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> new Move(-1, Optional.empty()));
	}

	@Test
	void should_throw_exception_if_destination_card_is_invalid() {
		assertThrows(IllegalArgumentException.class, () -> new Move(Card.Ace, Optional.of(-1)));
	}

	@Test
	void destination_should_be_empty_if_destination_card_is_unspecified() {
		Move move = new Move(Card.Ace);
		assertEquals(Optional.empty(), move.destination());
	}

	@Test
	void destination_should_be_set_if_destination_card_is_specified() {
		Move move = new Move(Card.Ace, Card.Two);
		assertEquals(Optional.of(Card.Two), move.destination());
	}


}