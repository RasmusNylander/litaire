import model.Move;
import model.Solitaire;

import java.time.temporal.TemporalAmount;


public interface Solver<T extends Solitaire> {
	Move getBestMove(T game, int maxSearchDepth, TemporalAmount maxTime);
}
