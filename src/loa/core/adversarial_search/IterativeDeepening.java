package loa.core.adversarial_search;

import loa.core.board.Board;
import loa.core.board.Move;
import loa.minimax.AbstractIterativeDeepening;

/**
 * Created by ryang on 16/06/2017.
 */
public class IterativeDeepening extends AbstractIterativeDeepening<Board, Move> {


	private int depth;


	public IterativeDeepening() {
		depth = 0;
	}

	public IterativeDeepening(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public Move search(Board state) {
		return iterativeDeepening(state, depth);
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
