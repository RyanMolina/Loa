package loa.core.player;

import loa.core.Game;
import loa.core.board.Board;
import loa.core.board.Move;
import loa.core.board.Piece;
import loa.core.adversarial_search.AdversarialSearchFactory;
import loa.minimax.AdversarialSearch;


public class MachinePlayer extends Player {

	private AdversarialSearch<Board, Move> adversarialSearch;
	private AdversarialSearchFactory adversarialSearchFactory;

    public MachinePlayer(Piece side, Game game) {
        super(side, game);
        adversarialSearchFactory = new AdversarialSearchFactory();
		setAlgorithm("Iterative Deepening");
    }

    @Override
    public Move makeMove() {
        Board boardState = new Board(getBoard());
		return adversarialSearch.search(boardState);
    }

	public void setAlgorithm(String algorithm) {
		adversarialSearch = adversarialSearchFactory.createAlgorithm(algorithm);
	}

}
