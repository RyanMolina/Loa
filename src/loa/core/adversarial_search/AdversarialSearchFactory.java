package loa.core.adversarial_search;

import loa.core.board.Board;
import loa.core.board.Move;
import loa.minimax.AdversarialSearch;

/**
 * Created by ryang on 16/06/2017.
 */
public class AdversarialSearchFactory {
	public AdversarialSearch<Board, Move> createAlgorithm(String type) {
		switch(type) {
			case "Minimax":
				return new Minimax();
			case "AlphaBeta":
				return new AlphaBetaPruning();
			default:
				return new IterativeDeepening(2);
		}
	}
}
