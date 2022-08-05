package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;
import ChessGame.Location;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class King extends Chess {

    public King(ChessBoard b, Camp c) {
        this.board = b;
        this.camp = c;
        if (this.camp == Camp.White) {
            PrintToScreen = '\u2654';
        } else {
            PrintToScreen = '\u265A';
        }
    }

    //Movex和Movey紀錄國王的走法(八個方向)
    private final int[] Movex = {1, 1, 0, -1, -1, -1, 0, 1};
    private final int[] Movey = {0, 1, 1, 1, 0, -1, -1, -1};

    @Override
    public void ExtendedMoveWay() {
        ExtendedMovableLocation.clear();
        Location l = this.getSelfLocation();
        //國王的一般走法
        for (int i = 0; i < Movex.length; i++) {
            // l變成其中一個可能走到的位置  
            if (BoardLocationIndicator(l.getxaxis() + Movex[i], l.getyaxis() + Movey[i])) {
                Location loc2 = board.BoardLocation[l.getxaxis() + Movex[i]][l.getyaxis() + Movey[i]];
                ExtendedMovableLocation.add(loc2);
            }
        }

    }
  
    @Override
    //此方法是用來尋找國王可以走的位置
    public void IdealMoveWay() {
        IdealMovableLocation.clear();    //每次尋找前先清除之前的紀錄
        Location l = this.getSelfLocation();

        //國王的一般走法
        for (int i = 0; i < Movex.length; i++) {
            // l變成其中一個可能走到的位置  
            if (BoardLocationIndicator(l.getxaxis() + Movex[i], l.getyaxis() + Movey[i])) {
                Location loc2 = board.BoardLocation[l.getxaxis() + Movex[i]][l.getyaxis() + Movey[i]];
                AddIfInChessBoard(this.camp, loc2); //丟給AddIfChessBoard判斷
            }
        }

        //入堡      
        //國王自身沒動過
        if (this.getFirstmove() == true) {
            int thisyaxis = 0;
            for (Map.Entry<Location,Chess> x : board.LocationChessKeyValue.entrySet()) {
                if (x.getValue().equals(this)) {
                    Location loc = (Location) x.getKey();
                    thisyaxis = loc.getyaxis();
                    break;
                }
            }

            //王翼易位
            if (board.LocationChessKeyValue.get(board.BoardLocation[5][thisyaxis]).camp == Camp.Other
                    && board.LocationChessKeyValue.get(board.BoardLocation[6][thisyaxis]).camp == Camp.Other
                    && board.LocationChessKeyValue.get(board.BoardLocation[7][thisyaxis]).getFirstmove()
                    && board.LocationChessKeyValue.get(board.BoardLocation[7][thisyaxis]).getIsCastle()
                    && !board.isControlledByEmeny(board.BoardLocation[4][thisyaxis], this.camp)
                    && !board.isControlledByEmeny(board.BoardLocation[5][thisyaxis], this.camp)
                    && !board.isControlledByEmeny(board.BoardLocation[6][thisyaxis], this.camp)) {
                AddIfInChessBoard(this.camp, board.BoardLocation[6][thisyaxis]); //丟給AddIfChessBoard判斷
            } else {
                // board.BoardLocation[6][thisyaxis].getJButton().setBackground(Color.WHITE);
            }

            //后翼易位
            if (board.LocationChessKeyValue.get(board.BoardLocation[3][thisyaxis]).camp == Camp.Other
                    && board.LocationChessKeyValue.get(board.BoardLocation[2][thisyaxis]).camp == Camp.Other
                    && board.LocationChessKeyValue.get(board.BoardLocation[0][thisyaxis]).getFirstmove()
                    && board.LocationChessKeyValue.get(board.BoardLocation[0][thisyaxis]).getIsCastle()
                    && !board.isControlledByEmeny(board.BoardLocation[4][thisyaxis], this.camp)
                    && !board.isControlledByEmeny(board.BoardLocation[3][thisyaxis], this.camp)
                    && !board.isControlledByEmeny(board.BoardLocation[2][thisyaxis], this.camp)) {
                AddIfInChessBoard(this.camp, board.BoardLocation[2][thisyaxis]); //丟給AddIfChessBoard判斷
            }

        }

    }

    @Override
    public void RealMoveWay() {
        RealMovableLocation.clear();
        RealMovableLocation.addAll(IdealMovableLocation);

        for (Location loc : IdealMovableLocation) {
            if (board.isControlledByEmeny(loc, this.camp)) {
                RealMovableLocation.remove(loc);
            }
        }

    }

    @Override
    public void printChess() {

        try {
            String s = "";
            Location loc = this.getSelfLocation();
            int lloc = loc.getxaxis() + loc.getyaxis();
            if (this.camp == Camp.White && lloc % 2 == 1) {
                s = "src\\img\\WhiteKingInWhite.png";
            } else if (this.camp == Camp.White && lloc % 2 == 0) {
                s = "src\\img\\WhiteKingInBlack.png";
            } else if (this.camp == Camp.black && lloc % 2 == 1) {
                s = "src\\img\\BlackKingInWhite.png";
            } else if (this.camp == Camp.black && lloc % 2 == 0) {
                s = "src\\img\\BlackKingInBlack.png";
            }
            File file = new File(s);
            this.image = ImageIO.read(file);
            this.image = zoomImage(image, 4);
            board.grids[7 - loc.getyaxis()][loc.getxaxis()].setIcon(new ImageIcon(this.image));
        } catch (IOException e) {
            System.out.println("系統出錯，無法讀取圖片");
        }

    }

    @Override
    public boolean getIsKing() {
        return true;
    }

    @Override
    public Chess ProtectedKingFromBCQ() {
        return null;
    }

}
