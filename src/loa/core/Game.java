package loa.core;

import loa.gui.BoardPane;
import loa.gui.Cell;

import javax.swing.*;
import java.awt.event.*;

import static loa.Main.error;
import static loa.core.Board.M;
import static loa.core.Piece.*;

public class Game extends JFrame implements ActionListener{


    private Board board;


    private Player[] players = new Player[2];


    private String line;


    private BoardPane boardPane;


    public Game() {

        players = new Player[2];
        players[0] = new HumanPlayer(BP, this);
        players[1] = new MachinePlayer(WP, this);

    }


    public Board getBoard() {
        return board;
    }


    private void quit() {
        System.exit(0);
    }

    Move getMove() {


            while (true) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (line != null) {

                    Move move = Move.create(line, board);
                    if (move == null) {
                        error("invalid move: %s%n", line);
                    } else if (getBoard().get(move.getCol0(), move.getRow0()) == EMP) {
                        error("invalid move: choosing an empty piece");
                    } else if (getBoard().get(move.getCol0(), move.getRow0()) != (getBoard().turn())) {
                        error("invalid move: choosing an enemy piece");
                    } else {
                        return move;
                    }
                }

            }

    }

    public void play() {

        board = new Board();
        SwingUtilities.invokeLater(() -> {
            boardPane = new BoardPane();
            boardPane.update(board.getState());
            add(boardPane);

            for(int i = M-1; i >= 0; i--) {
                for(int j = 0; j < boardPane.getBoardSquares()[i].length; j++) {
                    Cell b = boardPane.getBoardSquares()[i][j];
                    b.addActionListener(this);
                }
            }
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            WindowListener exitListener = new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    int confirm = JOptionPane.showOptionDialog(
                            null, "Are You Sure to Close Application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (confirm == 0) {
                        quit();
                    }
                }
            };
            addWindowListener(exitListener);
            setLocationByPlatform(true);
            pack();
            setMinimumSize(getSize());
            setVisible(true);


        });



        while (true) {

            int playerInd = board.turn().ordinal();
            Move next;

            if (board.gameOver()) {
                announceWinner();
                break;
            }
            next = players[playerInd].makeMove();

            board.makeMove(next);
            line = null;
            if (players[playerInd] instanceof MachinePlayer) {
                System.out.println(players[playerInd].side()
                        + "::" + next);
            }

            boardPane.update(board.getState());

        }
    }

    private void announceWinner() {
        if (board.gameOver()) {
            Piece piece;
            if (board.piecesContiguous(Piece.BP) && board.piecesContiguous(Piece.WP)) {
                piece = board.turn();
            } else {
                piece = Piece.BP;
            }
            if (piece != WP) {
                System.out.println("White wins.");
            } else {
                System.out.println("Black wins.");
            }
            System.exit(0);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        Direction dir = Direction.N;
        Move move = null;
        Cell b = (Cell)e.getSource();
        if(line != null) line = line + "-" +getCharForNumber(b.getCol()) + (b.getRow()+1);
        else line = getCharForNumber(b.getCol()) + (b.getRow()+1);
        while (dir != null) {

            move = Move.create(b.getCol(), b.getRow(), board.pieceCountAlong(b.getCol(), b.getRow(), dir), dir, board);
            dir = dir.succ();
            if (board.isLegal(move)) {
                System.exit(0);
                Cell dest = boardPane.getBoardSquares()[move.getRow1()][move.getCol1()];
                dest.setHighlighted(!dest.isHighlighted());
            }
        }
    }
    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 'a')) : null;
    }
}
