package model.klondike;

import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface CardContainer {
	MoveMetaInformation move(int card, @NotNull CardContainer destination) throws IllegalMoveException;

	void undo(int card, @NotNull MoveMetaInformation moveMetaInformation);

	void receive(int... cards) throws IllegalMoveException;

	boolean canAcceptCard(int card);

	@NotNull
	@Contract(pure = true)
	Set<Integer> reachableCards();

	boolean isEmpty();

	int getNumberOfCards();

}
