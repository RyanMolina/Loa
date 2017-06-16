package loa.minimax;

/**
 *
 * @param <S> type of Game Tree
 * @param <M> type of Action/Move
 */
public interface AdversarialSearch<S, M> {
	/**
	 *
	 * @param state Game Tree
	 * @return move Action/Move
	 */
	M search(S state);
}
