package model.klondike;

import model.IllegalMoveException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

class MockCardContainer implements CardContainer {
	public boolean receivedWasCalled = false;

	@Override
	public void move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		assert false;
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		receivedWasCalled = true;
	}

	@Override
	public @NotNull Set<Integer> reachableCards() {
		assert false;
		return null;
	}

	@Override
	public boolean canAcceptCard(int card) {
		assert false;
		return false;
	}

	@Override
	public boolean isEmpty() {
		assert false;
		return false;
	}

	@Override
	public int getNumberOfCards() {
		assert false;
		return 0;
	}
}
