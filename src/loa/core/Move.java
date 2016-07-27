package loa.core;

import static loa.core.Piece.EMP;
import static loa.core.Board.M;

public class Move {



    static Move create(int column0, int row0, int column1, int row1, Board board) {


        if (!inBounds(column0, row0) || !inBounds(column1, row1)) {

            return null;
        }
        int moved = board.get(column0, row0).ordinal();
        int replaced = board.get(column1, row1).ordinal();
        if(moved == EMP.ordinal()) return null;
        return moves[column0][row0][column1][row1][moved][replaced];
    }


    static Move create(int column0, int row0, int k, Direction dir, Board board) {

        return create(column0, row0, column0 + dir.dc * k, row0 + dir.dr * k,
                      board);
    }

    private Move(int col0, int row0, int col1, int row1, Piece moved, Piece replaced) {

        this.col0 = col0;
        this.row0 = row0;
        this.col1 = col1;
        this.row1 = row1;
        this.moved = moved;
        this.replaced = replaced;
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

    Piece movedPiece() {
        return moved;
    }

    Piece replacedPiece() {
        return replaced;
    }

    int length() {
        return Math.max(Math.abs(row1 - row0), Math.abs(col1 - col0));
    }

    private static boolean inBounds(int c, int r) {
        return 0 <= c && c < M &&  0 <= r && r < M;
    }




    private final int col0, row0, col1, row1;

    private final Piece moved;

    private final Piece replaced;

    private static Move[][][][][][] moves = new Move[M + 1][M + 1][M + 1][M + 1][2][3];

    static {
        for (int m = 0; m < 2; m++) {
            for (int r = 0; r < 3; r++) {
                Piece pm = Piece.values()[m], pr = Piece.values()[r];
                if (pm == pr || pm == EMP) {
                    continue;
                }
                for (int r0 = 0; r0 < M; r0++) {
                    for (int c0 = 0; c0 < M; c0++) {
                        for (int k = 0; k < M; k++) {
                            if (k != r0) {
                                moves[c0][r0][c0][k][m][r] = new Move(c0, r0, c0, k, pm, pr);
                                if ((char) (c0 - r0 + k - 1) < M) {
                                    moves[c0][r0][c0 - r0 + k][k][m][r] = new Move(c0, r0, c0 - r0 + k, k, pm, pr);
                                }
                            }
                            if (k != c0) {
                                moves[c0][r0][k][r0][m][r] = new Move(c0, r0, k, r0, pm, pr);
                                if ((char) (c0 + r0 - k) < M) {
                                    moves[c0][r0][k][c0 + r0 - k][m][r] = new Move(c0, r0, k, c0 + r0 - k,
                                                   pm, pr);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
