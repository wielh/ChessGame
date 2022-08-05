package chess.ChessDesign;

import ChessGame.Camp;
import static chess.ChessDesign.Chess.zoomImage;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PawnPromotion extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Chess chess;
    JButton[] button = new JButton[4];

    public PawnPromotion(Chess c) throws HeadlessException {

        chess = c;

        this.setLayout(new GridLayout(1, 4));
        this.setTitle("Promotion");
        this.setSize(600, 150);
        this.setLocation(300, 300);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        try {
            String[] s = new String[4];
            if (chess.camp == Camp.White) {
                s[0] = "src\\img\\WhiteQueenInBlack.png";
                s[1] = "src\\img\\WhiteBishopInWhite.png";
                s[2] = "src\\img\\WhiteKnightInBlack.png";
                s[3] = "src\\img\\WhiteCastleInWhite.png";
            } else {
                s[0] = "src\\img\\BlackQueenInBlack.png";
                s[1] = "src\\img\\BlackBishopInWhite.png";
                s[2] = "src\\img\\BlackKnightInBlack.png";
                s[3] = "src\\img\\BlackCastleInWhite.png";
            }

            for (int i = 0; i < s.length; i++) {
                button[i] = new JButton();
                File file = new File(s[i]);
                BufferedImage image = ImageIO.read(file);
                image = zoomImage(image, 4);
                button[i].setIcon(new ImageIcon(image));
                button[i].setVisible(true);
                if(i%2==0){
                    button[i].setBackground(new Color(45, 175, 175));
                }else{
                    button[i].setBackground(Color.WHITE);
                }
                add(button[i]);
                button[i].addActionListener(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if (ae.getSource().equals(button[1])) {
            chess.board.LocationChessKeyValue.put(chess.getSelfLocation(), new Bishop(chess.board, chess.camp));
            chess.getSelfLocation().getJButton().setEnabled(false);
            this.dispose();
        } else if (ae.getSource().equals(button[2])) {
            chess.board.LocationChessKeyValue.put(chess.getSelfLocation(), new Knight(chess.board, chess.camp));
            chess.getSelfLocation().getJButton().setEnabled(false);
            this.dispose();
        } else if (ae.getSource().equals(button[3])) {
            chess.board.LocationChessKeyValue.put(chess.getSelfLocation(), new Castle(chess.board, chess.camp));
            chess.getSelfLocation().getJButton().setEnabled(false);
            this.dispose();
        } else if(ae.getSource().equals(button[0])){
            chess.board.LocationChessKeyValue.put(chess.getSelfLocation(), new Queen(chess.board, chess.camp));
            chess.getSelfLocation().getJButton().setEnabled(false);
            this.dispose();
        }
        
        chess.board.printWholeBoard(chess.board);

    }

}
