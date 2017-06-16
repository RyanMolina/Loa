package loa.minimax;


import java.util.Iterator;

/**
 *
 * @param <S> type of Game Tree
 * @param <M> type of Action/Move
 */
public abstract class AbstractIterativeDeepening<S extends AbstractGame, M> implements AdversarialSearch <S, M> {

	protected M iterativeDeepening(S boardState, int depth) {
		Iterator<M> root = boardState.getLegalMoves();
		int currVal = 0;
		int bestVal = Integer.MIN_VALUE;
		M currM;
		M bestM = null;
		while (root.hasNext()) {
			currM = root.next();
			currVal = min(getChild(boardState, currM), depth);
			if (currVal > bestVal) {
				bestVal = currVal;
				bestM = currM;
			}
		}
		return bestM;
	}


	protected int max(S boardState, int depth) {
		boolean isTerminalNode = boardState.isTerminalState(boardState.getTurn());
		if(depth == 0 || isTerminalNode) {
			return (isTerminalNode) ? Integer.MAX_VALUE : eval(boardState);
		}
		int v = Integer.MIN_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			v = Integer.max(v, min(getChild(boardState, actions.next()), depth - 1));
		}
		return v;
	}


	protected int min(S boardState, int depth) {
		boolean isTerminalNode = boardState.isTerminalState(boardState.getTurn());
		if(depth == 0 || isTerminalNode) {
			return (isTerminalNode) ? Integer.MAX_VALUE : eval(boardState);
		}
		int v = Integer.MAX_VALUE;
		Iterator<M> actions = boardState.getLegalMoves();
		while(actions.hasNext()) {
			v = Integer.min(v, max(getChild(boardState, actions.next()), depth - 1));
		}
		return v;
	}

	protected abstract S getChild(S boardState, M action);
	protected abstract int eval(S boardState);

}
