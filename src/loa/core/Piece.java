package loa.core;

/** A Piece denotes the contents of a square, or identifies one side
 *  (Black or White) of a game.
 *  @author Maaz Uddin
 */
public enum Piece {
    /** The names of the pieces.  EMP indicates an empty square. The
     *  arguments give names to the piece colors. */
    BP, WP, EMP;

    /** Returns the full name of this piece (black, white, or
     *  empty). */



    /** Returns Piece with my opposing color (null for EMP). */
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
