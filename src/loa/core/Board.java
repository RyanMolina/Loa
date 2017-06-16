package loa.core;


import java.awt.Point;
import java.util.*;
import static loa.core.Piece.*;
import static loa.core.Direction.*;

public class Board implements Iterable<Move> {

    static final int M = 8;

    public Board() {
        initialize(INITIAL_PIECES, BP);
    }

    public Board(Piece[][] pieces, Piece side) {
        initialize(pieces, side);
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
    public void newGame() {
        initialize(INITIAL_PIECES, BP);
    }
    Board(Board board) {copyFrom(board);}
    void copyFrom(Board board) {
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

        if(move == null) {
            return;
        }
        moves.add(move);

        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();

        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);

        turn = turn.opposite();
    }

    public Piece turn() {
        return turn;
    }

    public boolean isLegal(Move move) {
        if (move == null) return false;
        int c0 = move.getCol0();
        int r0 = move.getRow0();
        Piece startingPiece = get(c0, r0);
        return  startingPiece == turn && move.length() == pieceCountAlong(move) && !blocked(move);
    }

    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    public boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    public List<Point> arrayofCoordinates(Piece side) {
        List<Point> coordinates = new ArrayList<>();
        for(int row = 0; row < M; row++) {
            for(int col = 0; col < M; col++) {
                if(currentState[row][col] == side) {
                    coordinates.add(new Point(col, row));
                }
            }
        }
        return coordinates;
    }

    public int checkAroundPiece(int c, int r, Set<Point> hashSet) {
        Piece piece = get(c, r);
        Point point = new Point(c, r);
        hashSet.add(point);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int col = c + x;
                int row = r + y;
                if (0 <= col  && col < M && 0 <= row && row < M) {
                    Point temp = new Point(col, row);
                    if((get(col, row) == piece) && !hashSet.contains(temp)) {
                        checkAroundPiece(col, row, hashSet);
                    }
                }
            }
        }
        return hashSet.size();
    }

    public boolean piecesContiguous(Piece side) {
        List<Point> coordinates = arrayofCoordinates(side);

        int row = (int) coordinates.get(0).getY();
        int col = (int) coordinates.get(0).getX();
        Set<Point> hashSet = new HashSet<>();
        int hashCount = checkAroundPiece(col, row, hashSet);
        System.out.println(String.format("Coords: %d Hash: %d", coordinates.size(), hashCount));
        return hashCount == coordinates.size();
    }

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

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return b.toString().equals(this.toString());
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

    private final List<Move> moves = new ArrayList<>();

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

    public Piece[][] getState() {
        return currentState;
    }

    private class MoveIterator implements Iterator<Move> {

        private int c, r;

        private Direction dir;

        private Move move;

        MoveIterator() {
            c = 0; r = 0; dir = N;
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

            while (r < M) {

                while (c < M) {

                    if (get(c, r) != EMP) {

                        while (dir != null) {
                            move = Move.create(c, r, pieceCountAlong(c, r, dir), dir, Board.this);
                            dir = dir.succ();
                            if (isLegal(move)) {
                                return;
                            }
                        }
                    }
                    if (c == M-1) {
                        c = 0;
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

}
