package loa.core;

import loa.minimax.AbstractBoard;

import java.util.*;
import java.util.List;

import static loa.core.Piece.*;
import static loa.core.Direction.*;

public class Board extends AbstractBoard<Piece, Move, Board> implements Iterable<Move> {

    public static final int SIZE = 8;

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


    public Board() {
        initialize(INITIAL_PIECES, BP);
    }


    public Board(Board board) {
        copyFrom(board);
    }


    public Board(Piece[][] pieces, Piece side) {
    	initialize(pieces, side);
	}


    private void initialize(Piece[][] contents, Piece side) {
        moves = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                set(col, row, contents[row][col]);
            }
        }
        turn = side;
    }


	/**
	 * Return the selected Piece
	 * @param column (x)
	 * @param row (y)
	 * @return currentState[row][column]
	 */
    public Piece get(int column, int row) {
        return currentState[row][column];
    }


    private void set(int column, int row, Piece currentTurn, Piece nextTurn) {
        currentState[row][column] = currentTurn;
        if (nextTurn != null) {
            turn = nextTurn;
        }
    }


    public void set(int column, int row, Piece currentTurn) {
        set(column, row, currentTurn, null);
    }


    public Piece[][] getState() {
        return currentState;
    }


    public boolean isLegal(Move move) {
        if (move == null) return false;
        int c0 = move.getCol0();
        int r0 = move.getRow0();
        Piece piece = get(c0, r0);
        return piece == turn && move.length() == pieceCountAlong(move) && !blocked(move);
    }


    public boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }


    public List<Coordinates> arrayofCoordinates(Piece side) {
        List<Coordinates> coordinates = new ArrayList<>();
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                if(currentState[row][col] == side) {
                    coordinates.add(new Coordinates(col, row));
                }
            }
        }
        return coordinates;
    }


    public int checkAroundPiece(int c, int r, Set<Coordinates> hashSet) {
        Piece piece = get(c, r);
        Coordinates point = new Coordinates(c, r);
        hashSet.add(point);

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                int col = c + x;
                int row = r + y;
                if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                    Coordinates temp = new Coordinates(col, row);
                    if (get(col, row) == piece && !hashSet.contains(temp)) {
                        checkAroundPiece(col, row, hashSet);
                    }
                }
            }
        }

        return hashSet.size();
    }


    public boolean piecesContiguous(Piece side) {

        List<Coordinates> coordinates = arrayofCoordinates(side);
        int col = coordinates.get(0).getX();
        int row = coordinates.get(0).getY();

        Set<Coordinates> hashSet = new HashSet<>();
        int hashCount = checkAroundPiece(col, row, hashSet);
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


	public int pieceCountAlong(Move move) {
        Direction d = getDirection(move);
        return pieceCountAlong(move.getCol0(), move.getRow0(), d);
    }


    public int pieceCountAlong(int c, int r, Direction dir) {
        int counter = -1;
        int tempC = c;
        int tempR = r;
        while ((tempC >= 0 && tempR >= 0) && (tempC < SIZE && tempR < SIZE)) {

            if (get(tempC, tempR) != EMP) {
                counter++;
            }
            tempR += dir.dr;
            tempC += dir.dc;
        }
        tempC = c;
        tempR = r;
        while ((tempC >= 0 && tempR >= 0) && (tempC < SIZE && tempR < SIZE)) {
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


    @Override
    public void makeMove(Move move) {
        if(move == null) return;
        moves.add(move);

        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();

        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        turn = turn.opposite();
    }


    @Override
    public void copyFrom(Board gameState) {
        if (gameState == this) {
            return;
        }
        moves = new ArrayList<>();
        moves.addAll(gameState.moves);
        turn = gameState.turn;
        for (int c = 0; c < SIZE; c++) {
            for (int r = 0; r < SIZE; r++) {
                set(c, r,  gameState.get(c, r), turn);
            }
        }
    }


    @Override
    public Iterator<Move> iterator() {
        return getLegalMoves();
    }


    @Override
    public boolean isTerminalState(Piece turn) {
        List<Coordinates> coordinates = arrayofCoordinates(turn);
        int col = coordinates.get(0).getX();
        int row = coordinates.get(0).getY();

        Set<Coordinates> hashSet = new HashSet<>();
        int hashCount = checkAroundPiece(col, row, hashSet);
        return hashCount == coordinates.size();
    }

    @Override
    public Iterator<Move> getLegalMoves() {
        return new MoveIterator();
    }

    @Override
    public Piece getTurn() {
        return turn;
    }


    private class MoveIterator implements Iterator<Move> {

        private int col;
        private int row;
        private Direction dir;
        private Move move;

        private MoveIterator() {
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

            while (row < SIZE) {
                while (col < SIZE) {
                    if (get(col, row) != EMP) {
                        while (dir != null) {
                            move = Move.create(col, row, pieceCountAlong(col, row, dir), dir, Board.this);
                            dir = dir.next();
                            if (isLegal(move)) {
                                return;
                            }
                        }
                    }
                    if (col == SIZE -1) {
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

	public int evaluator() {
		List<Coordinates> coordinates = arrayofCoordinates(getTurn());
		int col = coordinates.get(0).getX();
		int row = coordinates.get(0).getY();
		Set<Coordinates> hashSet = new HashSet<>();
		int value = this.checkAroundPiece(col, row, hashSet) * 1000;

		List<Coordinates> humanCoords = arrayofCoordinates(getTurn());
		List<Coordinates> machineCoords = arrayofCoordinates(getTurn().opposite());
		int humanVal = concentration(getTurn()) + centralisation(humanCoords);
		int machineVal = concentration(getTurn().opposite()) + centralisation(machineCoords);
		value  += humanVal + machineVal;
		return value;
	}

    public int concentration(Piece side) {
        int centerR = 0;
        int centerC = 0;
        int count = 0;
        List<Coordinates> pieceCoords = new ArrayList<>();
        Piece[][] state = getState();
        for(int i = 0; i < Board.SIZE; i++) {
            for(int j = 0; j < Board.SIZE; j++) {
                if (state[i][j] == side) {
                    count++;
                    centerR += i;
                    centerC += j;
                    pieceCoords.add(new Coordinates(i, j));
                }
            }
        }
        centerR /= count;
        centerC /= count;

        Coordinates centerOfMass = new Coordinates(centerR, centerC);
        int x1 = centerOfMass.getX();
        int y1 = centerOfMass.getY();
        int distanceSum = 0;
        for(Coordinates p : pieceCoords) {
            int x2 = p.getX();
            int y2 = p.getY();
            int distance = Integer.max(Math.abs(x1-x2), Math.abs(y1-y2));
            distanceSum += distance;
        }
        return distanceSum;
    }


    public int centralisation(List<Coordinates> pieces) {
        int[][] pieceSquareTable = {{-80, -25, -20, -20, -20, -20, -25, -80},
                {-25,  10,  10,  10,  10,  10,  10, -25},
                {-20,  10,  25,  25,  25,  25,  10, -20},
                {-20,  10,  25,  50,  50,  25,  10, -20},
                {-20,  10,  25,  50,  50,  25,  10, -20},
                {-20,  10,  25,  25,  25,  25,  10, -20},
                {-25,  10,  10,  10,  10,  10,  10, -25},
                {-80, -25, -20, -20, -20, -20, -25, -80}};
        int sum = 0;
        for(Coordinates coords: pieces) {
            sum += pieceSquareTable[(int)coords.getX()][(int)coords.getY()];
        }
        return sum;

    }
}
