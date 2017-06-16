package loa.core;

import loa.gui.BoardPane;
import loa.gui.Cell;

import javax.swing.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

import static loa.core.Board.SIZE;
import static loa.core.Piece.*;

public class Game extends JFrame implements ActionListener {

    private Board board;
    private Player[] players = new Player[2];

    private List<Move> legalMoves;
    private Move next;
    private boolean showPlayerMoves;

    private BoardPane boardPane;

    public Game() {
        setTitle("Lines of Action");

        players = new Player[2];

        assignPiece();
        legalMoves = new LinkedList<>();
        board = new Board();


        boardPane = new BoardPane();
        boardPane.getAlgorithmComboBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                ((MachinePlayer) players[1]).setAlgorithm((String)cb.getSelectedItem());
            }
        });
        SwingUtilities.invokeLater(() -> {
            boardPane.update(board.getState());
            add(boardPane);
            for(int i = SIZE -1; i >= 0; i--) {
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


    private void assignPiece() {

        int selectedPiece;

        do {
            Object[] options = {"Black", "White"};
            selectedPiece = JOptionPane.showOptionDialog(null,
                    "Your Piece",
                    "Choose",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.DEFAULT_OPTION,
                    null,
                    options,
                    options[1]);

        } while (selectedPiece == JOptionPane.CLOSED_OPTION);

        JOptionPane.showMessageDialog(null, "Just use Iterative Deepening, since the long running recursive calls " +
                "of Minimax and AlphaBeta Pruning throws stackoverflow error");

        if(selectedPiece == BP.ordinal()) {
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

    /**
     * Wait for the next move.
     * @return
     */
    public Move getMove() {
        while(next == null) {
            try {
                Thread.sleep(100);}
            catch (InterruptedException e) {}
        }
        return next;
    }

    /**
     * Game Loop
     */
    public void play() {
        while (true) {
            if(!board.gameOver()) {
                int playerInd = board.getTurn().ordinal();
                next = null;
                if (players[playerInd] instanceof HumanPlayer) {
                    boardPane.setMessage("Your turn  ");
                    showPlayerMoves = true;
                } else {
                    boardPane.setMessage("Thinking...");
                }
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
        Piece piece;
        boolean white = board.piecesContiguous(Piece.WP);
        boolean black = board.piecesContiguous(Piece.BP);
        /**
         * If the last move make both sides win, the last one who made the move gets to win.
         */
        if (white && black) {
            piece = board.getTurn().opposite();
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

    /**
     * Highlight the legal moves of the selected piece.
     * @param e
     */
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
                dir = dir.next();

                if (board.isLegal(move)) {
                    legalMoves.add(move);
                }
            }
            boardPane.showLegalMoves(legalMoves);
        }
    }
}
