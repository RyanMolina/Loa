package loa.core.board;


import java.util.Iterator;
import java.util.NoSuchElementException;

import static loa.core.board.Direction.N;
import static loa.core.board.Piece.EMP;

class MoveIterator implements Iterator<Move> {

    private int col;
    private int row;
    private Direction dir;
    private Move move;
    private Board board;

    MoveIterator(Board board) {
        this.board = board;
        col = 0;
        row = 0;
        dir = N;
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

        while (row < Board.SIZE) {
            while (col < Board.SIZE) {
                if (this.board.get(col, row) != EMP) {
                    while (dir != null) {
                        move = Move.create(col, row, this.board.pieceCountAlong(col, row, dir), dir, this.board);
                        dir = dir.next();
                        if (this.board.isLegal(move)) {
                            return;
                        }
                    }
                }
                if (col == Board.SIZE -1) {
                    col = 0;
                    dir = N;
                    break;
                } else {
                    col++;
                    dir = N;
                }
            }
            row++;
        }
    }
}
