package model;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Solitaire {
	@NotNull
	Set<Move> possibleMoves();

	void makeMove(@NotNull Move move) throws IllegalMoveException;

	/**
	 * Undoes the last move made, restoring the state before the move. This means that
	 * it can be called multiple times, to undo multiple moves.
	 *
	 * @throws EmptyHistoryException if there are no moves to undo
	 */
	void undoMove() throws EmptyHistoryException;

	boolean isLegalMove(@NotNull Move move);
}
