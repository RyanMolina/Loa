package loa.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static java.lang.Thread.sleep;

class MachinePlayer extends Player {

    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    public Move makeMove() {
        Board boardState = new Board(getBoard());
        Iterator<Move> root = boardState.legalMoves();
        int currVal = 0;
        int bestVal = Integer.MIN_VALUE;
        Move currMove;
        Move bestMove = null;
        while (root.hasNext()) {
            currMove = root.next();
            currVal = minimax(result(boardState, currMove), 2, true);
            if (currVal > bestVal) {
                bestVal = currVal;
                bestMove = currMove;
            }
        }
        return bestMove;
    }
    private int minimax(Board boardState, int depth, boolean maximizingPlayer) {
        if(depth == 0) return eval(boardState);
        if(maximizingPlayer) {
            int v = Integer.MIN_VALUE;
            Iterator<Move> actions = boardState.legalMoves();
            while(actions.hasNext()) {
                v = max(v, minimax(result(boardState, actions.next()), depth - 1, false));
            }
            return v;
        } else {
            int v = Integer.MAX_VALUE;
            Iterator<Move> actions = boardState.legalMoves();
            while(actions.hasNext()) {
                v = min(v, minimax(result(boardState, actions.next()), depth - 1, true));
            }
            return v;
        }
    }

    private Board result(Board boardState, Move action) {
        Board b = new Board(boardState);
        b.makeMove(action);
        System.out.println(action.movedPiece());
        return b;
    }

    private int min(int x, int y) {
        return (x >= y) ? y : x;
    }
    private int max(int x, int y) {

        return (x <= y) ? y : x;
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

    private int eval(Board board) {
        int[] pieceSquareTable = {  -80, -25, -20, -20, -20, -25, -80,
                                    -25,  10,  10,  10,  10,  10, -25,
                                    -20,  10,  25,  25,  25,  10, -20,
                                    -20,  10,  25,  50,  50,  10, -20,
                                    -20,  10,  25,  50,  50,  10, -20,
                                    -20,  10,  25,  25,  25,  10, -20,
                                    -25,  10,  10,  10,  10,  10, -25,
                                    -80, -25, -20, -20, -20, -25, -80
                                };

        int val;
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
