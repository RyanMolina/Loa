package loa.core.board;

import static loa.core.board.Piece.EMP;
import static loa.core.board.Board.SIZE;

public class Move {

    public static Move create(int column0, int row0,
                              int column1, int row1,
                              Board board) {

        if (isInbounds(column0, row0) || isInbounds(column1, row1)) {
            return null;
        }

        int moved = board.get(column0, row0).ordinal();
        int replaced = board.get(column1, row1).ordinal();

        if(moved == EMP.ordinal()) {
            return null;
        }

        return moves[column0][row0][column1][row1][moved][replaced];
    }


    public static Move create(int column0, int row0,
                              int k,
                              Direction dir,
                              Board board) {

        return create(column0, row0,
                      column0 + dir.dc * k,
                      row0 + dir.dr * k,
                      board);
    }

    // first four is the board dimension,
    // the last two is the moved and replaced piece.
    private static Move[][][][][][] moves = new Move[SIZE + 1][SIZE + 1][SIZE + 1][SIZE + 1][2][3];

    static {
        for (int m = 0; m < 2; m++) {
            for (int r = 0; r < 3; r++) {
                Piece pm = Piece.values()[m],
                      pr = Piece.values()[r];

                if (pm == pr || pm == EMP) {
                    continue;
                }

                for (int r0 = 0; r0 < SIZE; r0++) {
                    for (int c0 = 0; c0 < SIZE; c0++) {
                        for (int k = 0; k < SIZE; k++) {

                            if (k != r0) {
                                moves[c0][r0][c0][k][m][r] = new Move(c0, r0, c0, k, pm, pr);
                                if ((char) (c0 - r0 + k - 1) < SIZE) {
                                    moves[c0][r0][c0 - r0 + k][k][m][r] = new Move(c0, r0, c0 - r0 + k, k, pm, pr);
                                }
                            }

                            if (k != c0) {
                                moves[c0][r0][k][r0][m][r] = new Move(c0, r0, k, r0, pm, pr);
                                if ((char) (c0 + r0 - k) < SIZE) {
                                    moves[c0][r0][k][c0 + r0 - k][m][r] = new Move(c0, r0, k, c0 + r0 - k, pm, pr);
                                }
                            }

                        }
                    }
                }
            }
        }
    }
    private Move(int col0, int row0, int col1, int row1, Piece moved, Piece replaced) {
        this.col0 = col0;
        this.row0 = row0;
        this.col1 = col1;
        this.row1 = row1;
        this.moved = moved;
        this.replaced = replaced;
    }

    private static boolean isInbounds(int c, int r) {
        return 0 > c || c >= SIZE || 0 > r || r >= SIZE;
    }

    public int getCol0() {
        return col0;
    }

    public int getRow0() {
        return row0;
    }

    public int getCol1() {
        return col1;
    }

    public int getRow1() {
        return row1;
    }

    public Piece movedPiece() {
        return moved;
    }

    public Piece replacedPiece() {
        return replaced;
    }

    int length() {
        return Math.max(Math.abs(row1 - row0), Math.abs(col1 - col0));
    }

    private final int col0, row0, col1, row1;

    private final Piece moved;

    private final Piece replaced;
}
