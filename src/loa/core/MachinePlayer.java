package loa.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Thread.sleep;

class MachinePlayer extends Player {


    private HashSet<Move> moves = new HashSet<>();


    private int hashCounter = 0;


    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    public Move makeMove() {
        System.out.println("Making move for: " + getBoard().turn());
        if (hashCounter % 10 == 0) {
            moves.clear();
            hashCounter = 0;
        }
        hashCounter += 1;
        return minMax(getBoard().turn(), 1, Integer.MIN_VALUE,
                Integer.MAX_VALUE);
    }

    private Move minMax(Piece side, int depth, int alpha, int beta) {
        Board board = new Board(getBoard());

        Iterator<Move> iter = board.legalMoves();
        Move currMove;
        Move bestMove = null;
        int currVal;
        int bestVal = Integer.MIN_VALUE;
        while (iter.hasNext()) {
            currMove = iter.next();

            if (moves.contains(currMove)) {
                continue;
            }
            currVal = eval(currMove);
            if (currVal > bestVal) {
                bestVal = currVal;
                bestMove = currMove;
            }
        }

        moves.add(bestMove);
        return bestMove;
    }

    private int piecesDist(ArrayList<ArrayList<Integer>> pieces) {
        int sum = 0;
        for (ArrayList<Integer> coords : pieces) {
            for (ArrayList<Integer> coords2 : pieces) {
                if (coords != coords2) {
                    int x1 = coords.get(0);
                    int y1 = coords.get(1);
                    int x2 = coords2.get(0);
                    int y2 = coords2.get(1);
                    double dist = Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1), 2));
                    sum += (int) dist;
                }
            }
        }
        return sum;
    }

    private int eval(Move move) {
        Board board = new Board(getBoard());
        int val;

        board.makeMove(move);
        if (board.piecesContiguous(board.turn())) {
            val = Integer.MAX_VALUE;
        } else {
            int humanMove = piecesDist(board.arrayofCoordinates(board.turn()));
            int machineMove = piecesDist(board.arrayofCoordinates(board.turn().opposite()));
            val = humanMove - machineMove;
        }
        return val;
    }
}
