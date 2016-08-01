package loa.core;


public enum Piece {

    BP, WP, EMP;

    Piece opposite() {
        switch (this) {
        case BP:
            return WP;
        case WP:
            return BP;
        default:
            return null;
        }
    }
}
