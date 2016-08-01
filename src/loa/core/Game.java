package loa.core;

import jdk.nashorn.internal.scripts.JO;
import loa.gui.BoardPane;
import loa.gui.Cell;

import javax.swing.*;
import java.awt.*;
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

    private boolean showPlayerMoves;

    public Game() {

        players = new Player[2];
        assignPiece();
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
            boardPane.getNewBtn().addActionListener(e-> {
                board.newGame();
                boardPane.update(board.getState());
            });
            boardPane.getUndoBtn().addActionListener(e-> {
                Move move = board.retract();
                if(move != null)  {
                    boardPane.undo(move);
                }
                move = board.retract();
                if(move != null)  {
                    boardPane.undo(move);
                }
            });
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
            pack();
            setMinimumSize(getSize());
            setVisible(true);
        });
    }

    private void assignPiece() {
        int n;

        do {
            Object[] options = {"Black", "White"};
            n = JOptionPane.showOptionDialog(null,
                    "Your Piece",
                    "Choose",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[1]);

        } while (n == JOptionPane.CLOSED_OPTION);

        if(n == BP.ordinal()) {
            players[0] = new HumanPlayer(BP, this);
            players[1] = new MachinePlayer(WP, this);
        } else {
            players[1] = new HumanPlayer(WP, this);
            players[0] = new MachinePlayer(BP, this);
        }

    }

    public Board getBoard() {
        return board;
    }

    Move getMove() {
        while(next == null) {
            try {
                Thread.sleep(100);}
            catch (InterruptedException e) {}
        }
        return next;
    }

    public void play() {

        while (true) {
            if(!board.gameOver()) {
                int playerInd = board.turn().ordinal();
                next = null;
                if (players[playerInd] instanceof HumanPlayer) {
                    boardPane.setMessage(" Your turn");
                    showPlayerMoves = true;
                } else {
                    boardPane.setMessage(" Thinking...");
                }
                boardPane.getNewBtn().setEnabled(players[playerInd] instanceof HumanPlayer);
                boardPane.getUndoBtn().setEnabled(players[playerInd] instanceof HumanPlayer);
                next = players[playerInd].makeMove();
                board.makeMove(next);
                boardPane.insert(next);
                showPlayerMoves = false;
            } else {
                announceWinner();
            }
        }

    }

    private void announceWinner() {
        if (board.gameOver()) {
            Piece piece;
            boolean white = board.piecesContiguous(Piece.WP);
            boolean black = board.piecesContiguous(Piece.BP);
            if (white && black) {
                piece = board.turn().opposite();
                if (piece == BP) {
                    boardPane.setMessage(" Black wins");
                } else {
                    boardPane.setMessage(" White wins");
                }
            } else if (black) {
                boardPane.setMessage(" Black wins");
            } else {
                boardPane.setMessage(" White wins");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(showPlayerMoves) {
            Cell b = (Cell) e.getSource();
            if (!legalMoves.isEmpty()) {
                Move m = legalMoves.get(0);
                m = Move.create(m.getCol0(), m.getRow0(), b.getCol(), b.getRow(), board);
                if (legalMoves.contains(m)) {
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
}
