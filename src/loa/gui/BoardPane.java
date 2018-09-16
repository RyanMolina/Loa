package loa.gui;


import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import loa.core.board.Move;
import loa.core.board.Piece;



/**
 * Created by Ryan Gabriel Molina on 7/25/2016.
 */
public class BoardPane extends JPanel {

    private String[] algorithms = {"Iterative Deepening", "Minimax", "AlphaBeta"};

    private Cell[][] boardSquares = new Cell[8][8];

    private JLabel message = new JLabel("", SwingConstants.CENTER);

    private JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);

    private static final String COLS = "ABCDEFGH";


    public BoardPane() {
        init();
    }


    public void setMessage(String s) {
        message.setText(s);
    }


    private void init() {

        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new BorderLayout());


        algorithmComboBox.setSelectedIndex(0);
        add(algorithmComboBox, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(0, 9)) {
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Component c = getParent();
                Dimension prefSize = null;

                if (c == null) {
                    prefSize = new Dimension((int) d.getWidth(), (int) d.getHeight());
                } else if (c.getWidth() > d.getWidth() && c.getHeight() > d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }

                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();

                int s = (w > h ? h : w);
                return new Dimension(s, s);
            }
        };


        add(board, BorderLayout.CENTER);

        board.setBorder(new CompoundBorder(new EmptyBorder(18, 18, 18, 18), new LineBorder(Color.BLACK)));

        for(int row = 7; row >= 0; row--) {
            for (int col = 0; col < boardSquares[row].length; col++) {
                Cell b;
                if ((col % 2 == 1 && row % 2 == 1) || (col % 2 == 0 && row % 2 == 0)) {
                    b = new Cell(row, col, new Color(0xC0C9D2));
                } else {
                    b = new Cell(row, col, new Color(0x2F4E69));
                }
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                boardSquares[row][col] = b;
            }
        }


        board.add(new JLabel(""));

        for(int i = 0; i < 8; i++) {
            board.add(new JLabel("" + COLS.charAt(i), SwingConstants.CENTER)).
                    setFont(new Font("Serif", Font.BOLD, 24));
        }

        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                switch (col) {
                    case 0:
                        board.add(new JLabel("" + (row+1), SwingConstants.CENTER)).
                                setFont(new Font("Serif", Font.BOLD, 24));

                    default:
                        board.add(boardSquares[row][col]);
                }
            }
        }
        add(message, BorderLayout.SOUTH);
        message.setFont(new Font("Serif", Font.BOLD, 32));

    }



    public Cell[][] getBoardSquares() {
        return boardSquares;
    }


    public JComboBox getAlgorithmComboBox() {
        return this.algorithmComboBox;
    }


    public void update(Piece[][] p) {
        //Update Board
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < boardSquares[row].length; col++) {
                 switch(p[row][col]) {
                     case BP: boardSquares[row][col].setIcon(new ImageIcon(getClass().getResource("piece.png"))); break;
                     case WP: boardSquares[row][col].setIcon(new ImageIcon(getClass().getResource("w_piece.png"))); break;
                     case EMP: boardSquares[row][col].setIcon(null); break;
                 }
            }
        }
    }


    public void insert(Move move) {
        switch(move.movedPiece()) {
            case BP: {
                boardSquares[move.getRow0()][move.getCol0()].setIcon(null);
                boardSquares[move.getRow1()][move.getCol1()].setIcon(new ImageIcon(getClass().getResource("piece.png")));
                break;
            }
            case WP: {
                boardSquares[move.getRow0()][move.getCol0()].setIcon(null);
                boardSquares[move.getRow1()][move.getCol1()].setIcon(new ImageIcon(getClass().getResource("w_piece.png")));
                break;
            }
        }
    }


    public void undo(Move move) {
        switch(move.movedPiece()) {
            case BP: {
                boardSquares[move.getRow1()][move.getCol1()].setIcon(null);
                boardSquares[move.getRow0()][move.getCol0()].setIcon(new ImageIcon(getClass().getResource("piece.png")));
                break;
            }
            case WP: {
                boardSquares[move.getRow1()][move.getCol1()].setIcon(null);
                boardSquares[move.getRow0()][move.getCol0()].setIcon(new ImageIcon(getClass().getResource("w_piece.png")));
                break;
            }
        }
    }


    public void showLegalMoves(List<Move> legalMoves){
        for(Move m : legalMoves) {
            Cell c = boardSquares[m.getRow1()][m.getCol1()];
            c.setHighlighted(!c.isHighlighted());
        }
    }


}
