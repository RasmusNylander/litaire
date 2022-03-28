package model.klondike;

import model.IllegalMoveException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface CardContainer {
	void move(int card, @NotNull CardContainer destination) throws IllegalMoveException;

	void receive(int... cards) throws IllegalMoveException;

	@NotNull
	@Contract(pure = true)
	Set<Integer> reachableCards();

	boolean canAcceptCard(int card);

	boolean isEmpty();

	int getNumberOfCards();

}
