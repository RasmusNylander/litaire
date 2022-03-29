package model;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Solitaire {
	@NotNull
	Set<Move> possibleMoves();

	void makeMove(@NotNull Move move) throws IllegalMoveException;

	//void undoMove(@NotNull Move move) throws IllegalMoveException;
	boolean isLegalMove(@NotNull Move move);
}
