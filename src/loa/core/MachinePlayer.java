package loa.core;

import java.awt.Point;
import java.util.*;


class MachinePlayer extends Player {

    int nodes;

    Set<Move> bestMoves = new HashSet<>();

    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }
    @Override
    public Move makeMove() {
        Board boardState = new Board(getBoard());
        return iterativeDeepeningSearch(boardState);
    }

    private Move iterativeDeepeningSearch(Board boardState) {
        Move result = null;
        nodes = 0;
        if(bestMoves.size() > 72) bestMoves.clear();

        long start = System.currentTimeMillis() / 1000;
        for(int depth = 0; depth < Integer.MAX_VALUE; depth++) {
            result = minimaxDecision(boardState, depth);
            if(isCutoff(start, System.currentTimeMillis() / 1000)) {
                System.out.println(depth + " Nodes: " + nodes);
                break;
            }
        }
        return result;
    }
    private boolean isCutoff(long start, long end) {
        int time;
        if(nodes > 5000000) time = 0;
        else time = 1;
        return (end - start > time);
    }

    private Move minimaxDecision(Board boardState, int depth) {
        Iterator<Move> root = boardState.legalMoves();
        int currVal = 0;
        int bestVal = Integer.MIN_VALUE;
        Move currMove;
        Move bestMove = null;
        while (root.hasNext()) {
            currMove = root.next();
            if(bestMoves.contains(currMove)) {
                continue;
            }
            currVal = minValue(result(boardState, currMove), depth);
            if (currVal > bestVal) {
                bestVal = currVal;
                bestMove = currMove;
                bestMoves.add(currMove);
            }
        }
        bestMoves.add(bestMove);
        return bestMove;
    }

    private int maxValue(Board boardState, int depth) {
        nodes++;
        boolean isTerminalNode = boardState.piecesContiguous(boardState.turn());
        if(depth == 0 || isTerminalNode) {
            return (isTerminalNode) ? Integer.MAX_VALUE : eval(boardState);
        }
        int v = Integer.MIN_VALUE;
        Iterator<Move> actions = boardState.legalMoves();
        while(actions.hasNext()) {
            v = Math.max(v, minValue(result(boardState, actions.next()), depth - 1));
        }
        return v;
    }
    private int minValue(Board boardState, int depth) {
        nodes++;
        boolean isTerminalNode = boardState.piecesContiguous(boardState.turn());
        if(depth == 0 || isTerminalNode) {
            return (isTerminalNode) ? Integer.MAX_VALUE : eval(boardState);
        }
        int v = Integer.MAX_VALUE;
        Iterator<Move> actions = boardState.legalMoves();
        while(actions.hasNext()) {
            v = Math.min(v, maxValue(result(boardState, actions.next()), depth - 1));
        }
        return v;
    }
    private Board result(Board boardState, Move action) {
        Board b = new Board(boardState);
        b.makeMove(action);
        return b;
    }

    private int centralisation(List<Point> pieces) {
        int[][] pieceSquareTable = {{-80, -25, -20, -20, -20, -20, -25, -80},
                                    {-25,  10,  10,  10,  10,  10,  10, -25},
                                    {-20,  10,  25,  25,  25,  25,  10, -20},
                                    {-20,  10,  25,  50,  50,  25,  10, -20},
                                    {-20,  10,  25,  50,  50,  25,  10, -20},
                                    {-20,  10,  25,  25,  25,  25,  10, -20},
                                    {-25,  10,  10,  10,  10,  10,  10, -25},
                                    {-80, -25, -20, -20, -20, -20, -25, -80}};
        int sum = 0;
        for(Point coords: pieces) {
            sum += pieceSquareTable[(int)coords.getX()][(int)coords.getY()];
        }
        return sum;

    }


    private int concentration(Board board, Piece side) {
        int centerR = 0;
        int centerC = 0;
        int count = 0;
        List<Point> pieceCoords = new ArrayList<>();
        Piece[][] state = board.getState();
        for(int i = 0 ; i < Board.M; i++) {
            for(int j = 0; j < Board.M; j++) {
                if (state[i][j] == side) {
                    count++;
                    centerR += i;
                    centerC += j;
                    pieceCoords.add(new Point(i, j));
                }
            }
        }
        centerR /= count;
        centerC /= count;

        Point centerOfMass = new Point(centerR, centerC);
        int x1 = (int) centerOfMass.getX();
        int y1 = (int) centerOfMass.getY();
        int distanceSum = 0;
        for(Point p : pieceCoords) {
            int x2 = (int) p.getX();
            int y2 = (int) p.getY();
            int distance = (int) Math.max(Math.abs(x1-x2), Math.abs(y1-y2));
            distanceSum += distance;
        }
        return distanceSum;
    }
    private int eval(Board board) {
        int val;
        if(!board.legalMoves().hasNext()) return Integer.MAX_VALUE;
        List<Point> humanCoords = board.arrayofCoordinates(board.turn());
        List<Point> machineCoords = board.arrayofCoordinates(board.turn().opposite());
        int humanVal = concentration(board, board.turn()) + centralisation(humanCoords);
        int machineVal = concentration(board, board.turn().opposite()) + centralisation(machineCoords);
        val = humanVal + machineVal;
        return val;
    }
}
