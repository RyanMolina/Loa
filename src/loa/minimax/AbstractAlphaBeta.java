package loa.minimax;

import java.util.Iterator;

/**
 *
 * @param <G> type of Game Tree
 * @param <M> type of Action/Move
 */
public abstract class AbstractAlphaBeta<G extends AbstractGame, M> implements AdversarialSearch<G, M> {

	protected M alphaBeta(G game) {
		Iterator<M> root = game.getLegalMoves();
		int currVal;
		int bestVal = Integer.MIN_VALUE;
		M currMove;
		M bestMove = null;
		while (root.hasNext()) {
			currMove = root.next();
			currVal = max(getChild(game, currMove), Integer.MIN_VALUE, Integer.MAX_VALUE);
			if (currVal > bestVal) {
				bestVal = currVal;
				bestMove = currMove;
			}
		}
		return bestMove;
	}


	private int min(G game, int alpha, int beta) {
		boolean isTerminalState = game.isTerminalState(game.getTurn());
		if(isTerminalState) {
			return eval(game);
		}
		int value = Integer.MAX_VALUE;
		Iterator<M> actions = game.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.min(value, max(getChild(game, actions.next()), alpha, beta));
			beta = Integer.min(beta, value);
			if(beta <= alpha) {
				break;
			}
		}
		return value;
	}


	private int max(G game, int alpha, int beta) {
		boolean isTerminalState = game.isTerminalState(game.getTurn());
		if(isTerminalState) {
			return eval(game);
		}
		int value = Integer.MIN_VALUE;
		Iterator<M> actions = game.getLegalMoves();
		while(actions.hasNext()) {
			value = Integer.max(value, min(getChild(game, actions.next()), alpha, beta));
			alpha = Integer.max(alpha, value);
			if(beta <= alpha) {
				break;
			}
		}
		return value;
	}

	protected abstract G getChild(G boardState, M action);
	protected abstract int eval(G boardState);
}
