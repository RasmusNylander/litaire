import model.klondike.Klondike;
import model.klondike.Stock;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KlondikeSolverTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	void should_throw_exception_if_game_is_null() {
		KlondikeSolver solver = new KlondikeSolver();
		assertThrows(IllegalArgumentException.class, () -> solver.getBestMove(null, 1, Duration.ofDays(5)));
	}

	@Test
	void should_throw_exception_if_max_search_depth_is_negative() {
		KlondikeSolver solver = new KlondikeSolver();
		assertThrows(IllegalArgumentException.class, () -> solver.getBestMove(Klondike.newGame(Stock.Empty), -1, Duration.ofDays(5)));
	}

	@Test
	void should_throw_exception_if_max_search_depth_is_zero() {
		KlondikeSolver solver = new KlondikeSolver();
		assertThrows(IllegalArgumentException.class, () -> solver.getBestMove(Klondike.newGame(Stock.Empty), 0, Duration.ofDays(5)));
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	void should_throw_exception_if_max_time_is_null() {
		KlondikeSolver solver = new KlondikeSolver();
		assertThrows(IllegalArgumentException.class, () -> solver.getBestMove(Klondike.newGame(Stock.Empty), 1, null));
	}

	@Test
	void get_best_move_should_not_return_null() {
		KlondikeSolver solver = new KlondikeSolver();
		assertNotNull(solver.getBestMove(Klondike.newGame(Stock.Empty), 1, Duration.ofDays(5)));
	}
}