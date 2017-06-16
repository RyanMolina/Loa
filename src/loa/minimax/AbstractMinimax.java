package loa.minimax;

import java.util.Iterator;

/**
 *
 * @param <S> type of Game Tree
 * @param <M> type of Action/Move
 */
public abstract class AbstractMinimax<S extends AbstractGame, M> implements AdversarialSearch<S, M> {

	protected M minimax(S boardState) {
		Iterator<M> root = boardState.getLegalMoves();
		int currVal = 0;
		int bestVal = Integer.MIN_VALUE;
		M currMove;
		M bestMove = null;
		while (root.hasNext()) {
			currMove = root.next();
			currVal = min(getChild(boardState, currMove));
			if (currVal > bestVal) {
				bestVal = currVal;
				bestMove = currMove;
			}
		}
		return bestMove;
	}

	protected int min(S boardState) {
		boolean isTerminalState = boardState.isTerminalState(boardState.getTurn());
		if(isTerminalState) {
			return eval(boardState);
		}
		int value = Integer.MAX_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.min(value, max(getChild(boardState, actions.next())));
		}
		return value;
	}

	protected int max(S boardState) {
		boolean isTerminalState = boardState.isTerminalState(boardState.getTurn());
		if(isTerminalState) {
			return eval(boardState);
		}
		int value = Integer.MIN_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.max(value, min(getChild(boardState, actions.next())));
		}
		return value;
	}

	protected abstract S getChild(S boardState, M action);
	protected abstract int eval(S boardState);

}
