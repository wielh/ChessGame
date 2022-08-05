package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;
import ChessGame.Location;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Knight extends Chess {

    public Knight(ChessBoard b, Camp c) {
        this.board = b;
        this.camp = c;
        if (this.camp == Camp.White) {
            PrintToScreen = '\u2658';
        } else {
            PrintToScreen = '\u265E';
        }
    }

    //Movex和Movey紀錄騎士的"日"字走法
    private final int[] Movex = {2, 1, -1, -2, -2, -1, 1, 2};
    private final int[] Movey = {1, 2, 2, 1, -1, -2, -2, -1};

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
    //此方法是用來尋找騎士可以走的位置
    public void IdealMoveWay() {
        IdealMovableLocation.clear();    //每次尋找前先清除之前的紀錄
        Location l = getSelfLocation();

        for (int i = 0; i < Movex.length; i++) {
            if (BoardLocationIndicator(l.getxaxis() + Movex[i], l.getyaxis() + Movey[i])) {
                Location l2 = board.BoardLocation[l.getxaxis() + Movex[i]][l.getyaxis() + Movey[i]];
                AddIfInChessBoard(this.camp, l2); //丟給AddIfChessBoard判斷
            }
        }
    }
    
    @Override
    public void RealMoveWay() {
        if (this.camp != Camp.Other) {
            RealMovableLocation.clear();

            //判定有幾個敵方棋子正在攻擊自己的國王
            int NumberOfChess = 0;
            //取得誰在攻擊自己的國王          
            List<Chess> WhoAttackMyKing = new ArrayList<>();
            if (this.camp == Camp.White) {
                NumberOfChess = board.NumberOfAttackWhiteKing;
                WhoAttackMyKing.addAll(board.AttackWhiteKing);
            } else if (this.camp == Camp.black) {
                NumberOfChess = board.NumberOfAttackBlackKing;
                WhoAttackMyKing.addAll(board.AttackBlackKing);
            }

            //分情況討論
            if (NumberOfChess >= 2) {
                //Nothing
            } else if (this.ProtectedKingFromBCQ() == null && NumberOfChess == 1) {
                for (Location loc : IdealMovableLocation) {
                    if (this.camp == Camp.White && board.WhiteBlock.contains(loc)) {
                        this.RealMovableLocation.add(loc);
                    }

                    if (this.camp == Camp.black && board.BlackBlock.contains(loc)) {
                        this.RealMovableLocation.add(loc);
                    }
                }
            } else if (this.ProtectedKingFromBCQ() == null && NumberOfChess == 0) {
                RealMovableLocation.addAll(IdealMovableLocation);
            } else if (this.ProtectedKingFromBCQ() != null && NumberOfChess == 1) {
                //Nothing
            } else if (this.ProtectedKingFromBCQ() != null && NumberOfChess == 0) {
                //Nothing
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
                s = "src\\img\\WhiteKnightInWhite.png";
            } else if (this.camp == Camp.White && lloc % 2 == 0) {
                s = "src\\img\\WhiteKnightInBlack.png";
            } else if (this.camp == Camp.black && lloc % 2 == 1) {
                s = "src\\img\\BlackKnightInWhite.png";
            } else if (this.camp == Camp.black && lloc % 2 == 0) {
                s = "src\\img\\BlackKnightInBlack.png";
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
    public boolean getIsKnight() {
        return true;
    }
}
