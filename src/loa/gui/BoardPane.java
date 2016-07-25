package loa.gui;


import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import loa.core.Piece;



/**
 * Created by Ryan Gabriel Molina on 7/25/2016.
 */
public class BoardPane extends JPanel {


    private Cell[][] boardSquares = new Cell[8][8];
    private JPanel board = new JPanel(new GridLayout(0, 9));
    private static final String COLS = "ABCDEFGH";


    public BoardPane() {
        init();
    }

    public void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        board = new JPanel(new GridLayout(0, 9)) {
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Component c = getParent();
                Dimension prefSize = null;

                if(c == null) {
                    prefSize = new Dimension((int)d.getWidth(), (int)d.getHeight());
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
        add(board);

        board.setBorder(new CompoundBorder(new EmptyBorder(18, 18, 18, 18), new LineBorder(Color.BLACK)));
        for(int row = 7; row >= 0; row--) {
            for (int col = 0; col < boardSquares[row].length; col++) {
                Cell b;
                if ((col % 2 == 1 && row % 2 == 1) || (col % 2 == 0 && row % 2 == 0)) {
                    b = new Cell(row, col, Color.WHITE);
                } else {
                    b = new Cell(row, col, Color.BLACK);
                }
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                boardSquares[row][col] = b;
            }
        }
        board.add(new JLabel(""));

        for(int i = 0; i < 8; i++) {
            board.add(new JLabel("" + COLS.charAt(i), SwingConstants.CENTER));
        }
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                switch (col) {
                    case 0:
                        board.add(new JLabel("" + (row+1),
                                SwingConstants.CENTER));
                    default:
                        board.add(boardSquares[row][col]);
                }
            }
        }


    }


    public Cell[][] getBoardSquares() {
        return boardSquares;
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

}
