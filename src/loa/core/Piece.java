package loa.core;


public enum Piece {

    BP,
    WP,
    EMP;

    public Piece opposite() {
        switch (this) {
            case BP:
                return WP;
            case WP:
                return BP;
            default:
                return EMP;
        }
    }
}
