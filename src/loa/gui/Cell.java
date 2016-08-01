package loa.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by Ryan Gabriel Molina on 7/25/2016.
 */
public class Cell extends JButton {
    public int getRow() {
        return row;
    }



    public int getCol() {
        return col;
    }


    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
        setBackground((isHighlighted) ? new Color(0xdedf58) : this.color);

    }
    public boolean isHighlighted() { return isHighlighted; };
    private int row;
    private int col;
    private Color color;
    private boolean isHighlighted;

    public Cell(int row, int col, Color color) {
        this.row = row;
        this.col = col;
        this.color = color;
        setBackground(color);
    }

}
