package loa.core;

class HumanPlayer extends Player{

    HumanPlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    public Move makeMove() {
        return getGame().getMove();
    }

}
