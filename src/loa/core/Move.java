package loa.core;

import static loa.core.Piece.EMP;
import static loa.core.Board.M;

public class Move {



    public static Move create(String s, Board board) {
        s = s.trim();
        if (s.matches("[a-h][1-9]-[a-h][1-9]\\b.*")) {
            String p1 = s.substring(0, 2);
            String p2 = s.substring(3);
            System.out.println(board.row(p1)-1 + ", " + board.col(p1));
            return create(board.col(p1), board.row(p1)-1,
                          board.col(p2), board.row(p2)-1, board);
        } else {
            return null;
        }
    }

    static Move create(int column0, int row0, int column1, int row1, Board board) {


        if (!inBounds(column0+1, row0+1) || !inBounds(column1+1, row1+1)) {

            return null;
        }
        int moved = board.get(column0, row0).ordinal();
        int replaced = board.get(column1, row1).ordinal();
        System.out.println(moves[column0][row0][column1][row1][moved][replaced]);
        if(moved == EMP.ordinal()) return null;
        else return moves[column0][row0][column1][row1][moved][replaced];
    }


    static Move create(int column0, int row0, int k, Direction dir, Board board) {

        return create(column0, row0, column0 + dir.dc * k, row0 + dir.dr * k,
                      board);
    }

    private Move(int col0, int row0, int col1, int row1,
                 Piece moved, Piece replaced) {
        assert 1 <= col0 && col0 <= M && 1 <= row0 && row0 <= M
            && 1 <= col1 && col1 <= M && 1 <= row1 && row1 <= M
            && (col0 == col1 || row0 == row1 || col0 + row0 == col1 + row1
                || col0 - row0 == col1 - row1)
            && moved != EMP && moved != null && replaced != null;
        this.col0 = col0;
        this.row0 = row0;
        this.col1 = col1;
        this.row1 = row1;
        this.moved = moved;
        this.replaced = replaced;
    }


    int getCol0() {
        return col0;
    }


    int getRow0() {
        return row0;
    }

    int getCol1() {
        return col1;
    }

    int getRow1() {
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
        return 1 <= c && c <= M && 1 <= r && r <= M;
    }

    @Override
    public String toString() {
        return String.format("%c%d-%c%d", (char) (col0 - 1 + 'a'), row0,
                             (char) (col1 - 1 + 'a'), row1);
    }


    private final int col0, row0, col1, row1;

    private final Piece moved;

    private final Piece replaced;

    private static Move[][][][][][] moves =
        new Move[M + 1][M + 1][M + 1][M + 1][2][3];

    static {
        for (int m = 0; m <= 1; m++) {
            for (int r = 0; r <= 2; r++) {
                Piece pm = Piece.values()[m], pr = Piece.values()[r];
                if (pm == pr || pm == EMP) {
                    continue;
                }
                for (int r0 = 1; r0 <= M; r0++) {
                    for (int c0 = 1; c0 <= M; c0++) {
                        for (int k = 1; k <= M; k++) {
                            if (k != r0) {
                                moves[c0][r0][c0][k][m][r] = new Move(c0, r0, c0, k, pm, pr);
                                if ((char) (c0 - r0 + k - 1) < M) {
                                    moves[c0][r0][c0 - r0 + k][k][m][r] = new Move(c0, r0, c0 - r0 + k, k, pm, pr);
                                }
                            }
                            if (k != c0) {
                                moves[c0][r0][k][r0][m][r] = new Move(c0, r0, k, r0, pm, pr);
                                if ((char) (c0 + r0 - k - 1) < M) {
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
