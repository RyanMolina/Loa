package loa.core.board;

public enum Direction {

    NOWHERE(0, 0),
    N(0, 1),
    NE(1, 1),
    E(1, 0),

    SE(1, -1),
    S(0, -1),
    SW(-1, -1),

    W(-1, 0),
    NW(-1, 1);


    public final int dc, dr;

    Direction(int dc, int dr) {
        this.dc = dc;
        this.dr = dr;
    }

    public Direction next() {
        return (this == NW) ? null : values()[ordinal() + 1];
    }


}

