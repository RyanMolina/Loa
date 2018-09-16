package loa.minimax;

import java.util.Iterator;
import java.util.List;

public abstract class AbstractGame<P, M, S> {
    public P turn;
    public List<M> moves;

    /**
     *
     */
    protected abstract void makeMove(M move);

    /**
     * To copy the current state and use that to generate adversarial search game tree.
     */
    public abstract void copyFrom(S gameState);
    public abstract boolean isTerminalState(P turn);
    public abstract Iterator<M> getLegalMoves();
    public abstract P getTurn();
}
