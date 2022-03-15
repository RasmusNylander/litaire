package model;

import java.util.List;

public interface Solitaire {
	//static Solitaire newGame();
	// This is annoying! I guess it's necessary to use some creational pattern then.

	List<Move> possibleMoves();
	//void MakeMove(Move move) throws IllegalMoveException;
	//void UndoMove(Move move) throws IllegalMoveException;
}
