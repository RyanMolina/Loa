package loa.minimax;

import loa.core.Move;
import loa.core.Piece;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @param <P> the type of Player/Piece
 * @param <M> the type of Action/Move
 * @param <B> the type of Game Tree
 */
public abstract class AbstractGame<P, M, B> {

	public P turn;
	public List<M> moves;

	public abstract boolean isTerminalState(P turn);
	public abstract Iterator<M> getLegalMoves();
	public abstract P getTurn();
	protected abstract void makeMove(M move);
	public abstract void copyFrom(B gameState);
}
