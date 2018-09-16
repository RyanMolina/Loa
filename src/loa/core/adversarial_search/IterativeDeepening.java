package loa.core;

import loa.core.Board;
import loa.core.Coordinates;
import loa.core.Move;
import loa.minimax.AbstractIterativeDeepening;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		return Board.evaluator(board);
	}

}
