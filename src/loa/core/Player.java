package loa.core;

public abstract class Player {


    Player(Piece side, Game game) {
        this.side = side;
        this.game = game;
    }

    public abstract Move makeMove();

    public Piece side() {
        return side;
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public Game getGame() {
        return game;
    }

    private final Piece side;

    private Game game;

}
