package loa.core;

import loa.core.Board;
import loa.core.Coordinates;
import loa.core.Move;
import loa.minimax.AbstractMinimax;

import java.util.*;

/**
 * Created by ryang on 15/06/2017.
 */
public class Minimax extends AbstractMinimax<Board, Move> {

	@Override
	public Move search(Board state) {
		return minimax(state);
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
