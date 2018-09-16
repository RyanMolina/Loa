package loa.core.player;

import loa.core.Game;
import loa.core.board.Move;
import loa.core.board.Piece;

public class HumanPlayer extends Player {

    public HumanPlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    public Move makeMove() {
        return getGame().getMove();
    }

}
