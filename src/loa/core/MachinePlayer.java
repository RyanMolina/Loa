package loa.core;

import loa.minimax.AdversarialSearch;


class MachinePlayer extends Player {

	private AdversarialSearch<Board, Move> adversarialSearch;
	private AdversarialSearchFactory adversarialSearchFactory;

    protected MachinePlayer(Piece side, Game game) {
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
