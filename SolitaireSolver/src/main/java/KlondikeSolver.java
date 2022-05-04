import model.Move;
import model.klondike.Klondike;
import org.jetbrains.annotations.NotNull;

import java.time.temporal.TemporalAmount;


public class KlondikeSolver implements Solver<Klondike> {

	@Override
	public Move getBestMove(@NotNull Klondike game, int maxSearchDepth, @NotNull TemporalAmount maxTime) {
		if (maxSearchDepth <= 0)
			throw new IllegalArgumentException("Error: maxSearchDepth must be >= 1");
		//TODO: Respect time limit
		//TODO: Look more than one move ahead
		//TODO: Respect max search depth
		Move[] possibleMoves = game.possibleMoves().toArray(new Move[0]);
		return possibleMoves[(int) (Math.random() * possibleMoves.length)];
	}
}
