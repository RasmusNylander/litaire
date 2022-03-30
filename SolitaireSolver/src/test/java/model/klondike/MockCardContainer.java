package model.klondike;

import model.IllegalMoveException;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

class MockCardContainer implements CardContainer {
	public boolean receivedWasCalled = false;
	public boolean canReceive = true;

	@Override
	public MoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException {
		destination.receive(card);
		return null;
	}

	@Override
	public void undo(int card, @NotNull MoveMetaInformation moveMetaInformation) {
		assert false;
	}

	@Override
	public void receive(int... cards) throws IllegalMoveException {
		if (!canReceive) throw new IllegalMoveException("Error: MockCardContainer cannot receive.");
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
