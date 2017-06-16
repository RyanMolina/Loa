package loa.minimax;

import loa.core.Board;
import loa.core.Game;
import loa.core.Move;

import java.util.Iterator;

/**
 *
 * @param <S> type of Game Tree
 * @param <M> type of Action/Move
 */
public abstract class AbstractAlphaBeta<S extends AbstractGame, M> implements AdversarialSearch<Board, Move> {

	protected M alphaBeta(S boardState) {
		Iterator<M> root = boardState.getLegalMoves();
		int currVal = 0;
		int bestVal = Integer.MIN_VALUE;
		M currMove;
		M bestMove = null;
		while (root.hasNext()) {
			currMove = root.next();
			currVal = max(getChild(boardState, currMove), Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (currVal > bestVal) {
				bestVal = currVal;
				bestMove = currMove;
			}
		}
		return bestMove;
	}


	protected int min(S boardState, int alpha, int beta) {
		boolean isTerminalState = boardState.isTerminalState(boardState.getTurn());
		if(isTerminalState) {
			return eval(boardState);
		}
		int value = Integer.MAX_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.min(value, max(getChild(boardState, actions.next()), alpha, beta));
			beta = Integer.min(beta, value);
			if(beta <= alpha) {
				break;
			}
		}
		return value;
	}


	protected int max(S boardState, int alpha, int beta) {
		boolean isTerminalState = boardState.isTerminalState(boardState.getTurn());
		if(isTerminalState) {
			return eval(boardState);
		}
		int value = Integer.MIN_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.max(value, min(getChild(boardState, actions.next()), alpha, beta));
			alpha = Integer.max(alpha, value);
			if(beta <= alpha) {
				break;
			}
		}
		return value;
	}

	protected abstract S getChild(S boardState, M action);
	protected abstract int eval(S boardState);
}
