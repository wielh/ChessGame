package ChessGame;

import javax.swing.JButton;

public class Location {

    private int xaxis;
    private int yaxis;
    public ChessBoard b;
    private JButton button;

    public Location(int i, int j, ChessBoard b) {
        if (i >= 0 && i < 8 && j >= 0 && j < 8) {
            this.xaxis = i;
            this.yaxis = j;
            this.b = b;
            this.button = b.grids[7-j][i];
        }
    }
    
    public void setxaxis(int i) {
        this.xaxis = i;
    }

    public void setyaxis(int j) {
        this.yaxis = j;
    }

    public int getxaxis() {
        return xaxis;
    }

    public int getyaxis() {
        return yaxis;
    }

    public void setJButton(JButton j) {
        this.button = j;
    }

    public JButton getJButton() {
        return button;
    }

}
