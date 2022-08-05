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

public class Queen extends ChessBCQ {

    public Queen(ChessBoard b, Camp c) {
        this.board = b;
        this.camp = c;
        if (this.camp == Camp.White) {
            PrintToScreen = '\u2655';
        } else {
            PrintToScreen = '\u265B';
        }
    }

    public int[][] v = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1},
    {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    
    @Override
    public void ExtendedMoveWay() {
       this.ShareBCQExtendedMoveWay(v);
    }

    @Override
    public void IdealMoveWay() {
        this.ShareBCQMoveWay(v);
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
                Chess Attacker = this.ProtectedKingFromBCQ();   //取出正在試圖攻擊國王的棋子
                Location loc = this.getSelfLocation();

                if (this.IdealMovableLocation.contains(Attacker.getSelfLocation())) {
                    //判斷此棋子是否在自己的攻擊範圍
                    //如果是的話，取出方向w                   
                    int[] w = null;
                    for (int[] vtmp : v) {
                        for (int i = 1; i <= 7; i++) {
                            if (BoardLocationIndicator(loc.getxaxis() + vtmp[0] * i, loc.getyaxis() + vtmp[1] * i)) {
                                Chess chess = board.LocationChessKeyValue.get(board.BoardLocation[loc.getxaxis() + vtmp[0] * i][loc.getyaxis() + i * vtmp[1]]);
                                if (chess.camp == Camp.Other) {
                                    //Nothing
                                } else if (chess.camp != Camp.Other && Attacker.equals(chess)) {
                                    w = vtmp;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                    }

                    for (int i = 1; i <= 7; i++) {
                        if (BoardLocationIndicator(loc.getxaxis() + w[0] * i, loc.getyaxis() + w[1] * i)) {
                            Chess chess = board.LocationChessKeyValue.get(board.BoardLocation[loc.getxaxis() + w[0] * i][loc.getyaxis() + i * w[1]]);
                            if (chess.camp == Camp.Other) {
                               RealMovableLocation.add(chess.getSelfLocation());
                            } else if (chess.camp != Camp.Other && Attacker.equals(chess)) {
                               RealMovableLocation.add(chess.getSelfLocation());
                               break;
                            } else{
                                //Nothing
                            }
                        }
                    }

                }

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
                s = "src\\img\\WhiteQueenInWhite.png";
            } else if (this.camp == Camp.White && lloc % 2 == 0) {
                s = "src\\img\\WhiteQueenInBlack.png";
            } else if (this.camp == Camp.black && lloc % 2 == 1) {
                s = "src\\img\\BlackQueenInWhite.png";
            } else if (this.camp == Camp.black && lloc % 2 == 0) {
                s = "src\\img\\BlackQueenInBlack.png";
            }
            File file = new File(s);
            this.image = ImageIO.read(file);
            this.image=zoomImage(image, 4);
            board.grids[7 - loc.getyaxis()][loc.getxaxis()].setIcon(new ImageIcon(this.image));
        } catch (IOException e) {
            System.out.println("系統出錯，無法讀取圖片");
        }

    }
    
    @Override
    public boolean getIsQueen(){
        return true;
    } 
}
