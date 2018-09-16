package loa.core.adversarial_search;

import loa.core.board.Board;
import loa.core.board.Move;
import loa.minimax.AbstractAlphaBeta;

/**
 * Created by ryang on 16/06/2017.
 */
public class AlphaBetaPruning extends AbstractAlphaBeta<Board, Move> {

	@Override
	public Move search(Board state) {
		return alphaBeta(state);
	}


	@Override
	protected Board getChild(Board boardState, Move action) {
		Board b = new Board(boardState);
		b.makeMove(action);
		return b;
	}


	@Override
	protected int eval(Board board) {
		return board.evaluator();
	}
}
