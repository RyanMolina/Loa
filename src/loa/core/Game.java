package loa.core;

import loa.gui.BoardPane;
import loa.gui.Cell;

import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

import static loa.core.Board.M;
import static loa.core.Piece.*;

public class Game extends JFrame implements ActionListener{


    private Board board;

    private Player[] players = new Player[2];

    private BoardPane boardPane;

    private List<Move> legalMoves;

    private Move next;

    public Game() {

        players = new Player[2];
        players[0] = new HumanPlayer(BP, this);
        players[1] = new MachinePlayer(WP, this);
        legalMoves = new LinkedList<>();
        board = new Board();
        boardPane = new BoardPane();
        SwingUtilities.invokeLater(() -> {
            boardPane.update(board.getState());
            add(boardPane);
            for(int i = M-1; i >= 0; i--) {
                for(int j = 0; j < boardPane.getBoardSquares()[i].length; j++) {
                    Cell b = boardPane.getBoardSquares()[i][j];
                    b.addActionListener(this);

                }
            }
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
            pack();
            setMinimumSize(getSize());
            setVisible(true);
        });
    }


    public Board getBoard() {
        return board;
    }


    Move getMove() {

        while(next == null) {
            System.out.flush();
        }
        return next;
    }

    public void play() {

        while (!board.gameOver()) {

            int playerInd = board.turn().ordinal();
            next = null;

            System.out.println(board.turn());
            next = players[playerInd].makeMove();
            board.makeMove(next);


            boardPane.update(board.getState());


        }
        announceWinner();
    }

    private void announceWinner() {
        if (board.gameOver()) {
            Piece piece;
            if (board.piecesContiguous(Piece.BP) && board.piecesContiguous(Piece.WP)) {
                System.out.println("Tie");
                piece = board.turn();
            } else {
                piece = Piece.BP;
            }
            if (piece == WP) {
                System.out.println("White wins.");
            } else {
                System.out.println("Black wins.");
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Cell b = (Cell)e.getSource();
        if(!legalMoves.isEmpty()) {
            Move m = legalMoves.get(0);
            m = Move.create(m.getCol0(), m.getRow0(), b.getCol(), b.getRow(), board);
            if(legalMoves.contains(m)) {
                next = m;
            }
            boardPane.showLegalMoves(legalMoves);

        }
        legalMoves.clear();
        Direction dir = Direction.N;
        Move move;
        while (dir != null) {
            move = Move.create(b.getCol(), b.getRow(), board.pieceCountAlong(b.getCol(), b.getRow(), dir), dir, board);
            dir = dir.succ();

            if (board.isLegal(move)) {
                    legalMoves.add(move);
            }
        }
        boardPane.showLegalMoves(legalMoves);
    }

}
