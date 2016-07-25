package loa.core;


import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
import java.util.HashSet;
import static loa.core.Piece.*;
import static loa.core.Direction.*;

public class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    private static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    public Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    public Board() {
        clear();
    }

    Board(Board board) {
        copyFrom(board);
    }

    public void initialize(Piece[][] contents, Piece side) {
        moves.clear();

        for (int row = 0; row < M; row++) {
            for (int col = 0; col < M; col++) {
                set(col, row, contents[row][col]);
            }
        }
        turn = side;
    }

    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    public void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        moves.clear();
        moves.addAll(board.moves);
        turn = board.turn;

        for (int c = 0; c < M; c++) {
            for (int r = 0; r < M; r++) {
                set(c, r, board.get(c, r), turn);
            }
        }
    }

    public Piece get(int c, int r) {
        return currentState[r][c];
    }


    static int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a';
    }


    static int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    private void set(int c, int r, Piece v, Piece next) {
        currentState[r][c] = v;
        if (next != null) {
            turn = next;
        }
    }

    public void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    void makeMove(Move move) {
        assert isLegal(move);
        moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        turn = turn.opposite();
    }

    void retract() {
        assert movesMade() > 0;
        Move move = moves.remove(moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        turn = turn.opposite();
    }

    Piece turn() {
        return turn;
    }

    public boolean isLegal(Move move) {
        if (move == null) return false;
        int c0 = move.getCol0();
        int r0 = move.getRow0();
        Piece startingPiece = get(c0, r0);
        return startingPiece == turn && move.length() == pieceCountAlong(move) && !blocked(move);
    }

    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    public boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    ArrayList<ArrayList<Integer>> arrayofCoordinates(Piece side) {
        ArrayList<ArrayList<Integer>> answer = new ArrayList<>();
        for (int c = 0; c < M; c++) {
            for (int r = 0; r < M; r++) {
                if (side == get(c, r)) {
                    ArrayList<Integer> coords = new ArrayList<>();
                    coords.add(c);
                    coords.add(r);
                    answer.add(coords);
                }
            }
        }
        return answer;
    }

    private void checkAroundPiece(int c, int r, HashSet<ArrayList<Integer>> hashSet) {
        Piece piece = get(c, r);
        ArrayList<Integer> starting = new ArrayList<>();
        starting.add(c);
        starting.add(r);
        hashSet.add(starting);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int col = c + x;
                int row = r + y;
                if (col >= 1 && col <= M && row >= 1 && row <= M) {
                    ArrayList<Integer> temp = new ArrayList<>();
                    temp.add(col);
                    temp.add(row);
                    if (get(col, row) == piece
                            && !hashSet.contains(temp)) {
                        checkAroundPiece(col, row, hashSet);
                    }
                }
            }
        }
    }

    public boolean piecesContiguous(Piece side) {
        ArrayList<ArrayList<Integer>> coordinates = arrayofCoordinates(side);
        int col = coordinates.get(0).get(0);
        int row = coordinates.get(0).get(1);
        HashSet<ArrayList<Integer>> hashSet = new HashSet<>();
        checkAroundPiece(col, row, hashSet);
        return hashSet.size() == coordinates.size();
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    private int movesMade() {
        return moves.size();
    }





    private Direction getDirection(Move move) {
        int c0 = move.getCol0();
        int r0 = move.getRow0();
        int c1 = move.getCol1();
        int r1 = move.getRow1();
        int dRow = r1 - r0;
        int dCol = c1 - c0;
        if (dCol == 0 && dRow > 0) {
            return N;
        }
        if (dCol == 0 && dRow < 0) {
            return S;
        }
        if (dCol < 0 && dRow == 0) {
            return W;
        }
        if (dCol > 0 && dRow == 0) {
            return E;
        }
        if (dCol > 0 && dRow > 0) {
            return NE;
        }
        if (dCol < 0 && dRow > 0) {
            return NW;
        }
        if (dCol > 0 && dRow < 0) {
            return SE;
        }
        if (dCol < 0 && dRow < 0) {
            return SW;
        }
        return NOWHERE;
    }


    private int pieceCountAlong(Move move) {
        Direction d = getDirection(move);
        return pieceCountAlong(move.getCol0(), move.getRow0(), d);
    }

    int pieceCountAlong(int c, int r, Direction dir) {
        int counter = -1;
        int tempC = c;
        int tempR = r;
        while ((tempC >= 0 && tempR >= 0) && (tempC < M && tempR < M)) {


            if (get(tempC, tempR) != EMP) {
                counter++;
            }
            tempR += dir.dr;
            tempC += dir.dc;
        }
        tempC = c;
        tempR = r;
        while ((tempC >= 0 && tempR >= 0) && (tempC < M && tempR < M)) {
            if (get(tempC, tempR) != EMP) {
                counter++;
            }
            tempR -= dir.dr;
            tempC -= dir.dc;
        }
        return counter;
    }


    private boolean blocked(Move move) {
        if (move == null) {
            return true;
        }
        int c0 = move.getCol0();
        int r0 = move.getRow0();
        int c1 = move.getCol1();
        int r1 = move.getRow1();


        if (get(c1, r1) == turn) {


            return true;
        }
        int deltaR = 0, deltaC = 0;
        if (r1 > r0) {
            deltaR = 1;
        }
        if (r1 < r0) {
            deltaR = -1;
        }
        if (c1 > c0) {
            deltaC = 1;
        }
        if (c1 < c0) {
            deltaC = -1;
        }
        for (int i = 1; i < move.length(); i++) {
            if (get(c0 + deltaC, r0 + deltaR) == turn.opposite()) {
                return true;
            }
            c0 += deltaC;
            r0 += deltaR;
        }
        return false;
    }

    private static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    private final ArrayList<Move> moves = new ArrayList<>();

    private Piece turn;

    private Piece[][] currentState = {
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };


    private class MoveIterator implements Iterator<Move> {

        private int c, r;

        private Direction dir;

        private Move move;

        MoveIterator() {
            c = 1; r = 1; dir = NOWHERE;
            incr();
        }


        @Override
        public boolean hasNext() {
            return move != null;
        }

        @Override
        public Move next() {
            if (move == null) {
                throw new NoSuchElementException("no legal move");
            }

            Move move = this.move;
            incr();
            return move;
        }

        @Override
        public void remove() {
        }

        private void incr() {
            move = null;
            while (r <= M) {
                while (c <= M) {
                    if (get(c, r) != EMP) {
                        while (dir != null) {
                            move = Move.create(c, r,
                                    pieceCountAlong(c, r, dir),
                                    dir, Board.this);
                            dir = dir.succ();
                            if (isLegal(move)) {
                                return;
                            }
                        }
                    }
                    if (c == M) {
                        c = 1;
                        dir = N;
                        break;
                    } else {
                        c++;
                        dir = N;
                    }
                }
                r++;
            }
        }

    }
    public Piece[][] getState() {
        return currentState;
    }
}
