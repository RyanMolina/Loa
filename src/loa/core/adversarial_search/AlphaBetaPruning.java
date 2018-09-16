package loa.core;

import loa.core.Board;
import loa.core.Coordinates;
import loa.core.Move;
import loa.minimax.AbstractAlphaBeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
