package chess.ChessDesign;

import ChessGame.Camp;
import ChessGame.Location;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Pawn extends Chess {

    int WBIndicator;
    int startyaxis = 0;           //這兩項參數是專為吃過路兵的判定所使用的
    int endyaxis = 0;

    @Override
    public boolean getIsPawn() {
        return true;
    }

    public void CommonExtendMoveWay(Camp WhiteBlackIndicator) {
        //是步兵的話，只有斜前方才算
        ExtendedMovableLocation.clear();
        Location loc = this.getSelfLocation();
        
        if (WhiteBlackIndicator == Camp.White) {
            WBIndicator = 1;
        } else {
            WBIndicator = -1;
        }
        
        if (Chess.BoardLocationIndicator(loc.getxaxis() + 1*WBIndicator, loc.getyaxis() + 1*WBIndicator)) {
            Location lloc = board.BoardLocation[loc.getxaxis() + 1*WBIndicator][loc.getyaxis() + 1*WBIndicator];
            ExtendedMovableLocation.add(lloc);
        }

        if (Chess.BoardLocationIndicator(loc.getxaxis() - 1*WBIndicator, loc.getyaxis() + 1*WBIndicator)) {
            Location lloc = board.BoardLocation[loc.getxaxis() - 1*WBIndicator][loc.getyaxis() + 1*WBIndicator];
            ExtendedMovableLocation.add(lloc);
        }
    }

    public void CommonMoveWay(Camp WhiteBlackIndicator) {
        IdealMovableLocation.clear();
        //取得自己在棋盤上的位置
        Location tmp = getSelfLocation();

        //黑白步兵的"前進"是相反的
        if (WhiteBlackIndicator == Camp.White) {
            WBIndicator = 1;
        } else {
            WBIndicator = -1;
        }

        //吃子
        Location tmp2;
        Chess tmp3;

        if (BoardLocationIndicator(tmp.getxaxis() + 1 * WBIndicator, tmp.getyaxis() + 1 * WBIndicator)) {
            tmp2 = board.BoardLocation[tmp.getxaxis() + 1 * WBIndicator][tmp.getyaxis() + 1 * WBIndicator];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);
            if (tmp3.camp != Camp.Other) {
                AddIfInChessBoard(camp, tmp2);
            }
        }

        if (BoardLocationIndicator(tmp.getxaxis() - 1 * WBIndicator, tmp.getyaxis() + 1 * WBIndicator)) {
            tmp2 = board.BoardLocation[tmp.getxaxis() - 1 * WBIndicator][tmp.getyaxis() + 1 * WBIndicator];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);

            if (tmp3.camp != Camp.Other) {
                AddIfInChessBoard(camp, tmp2);
            }
        }

        boolean Next = false;
        //前進
        if (BoardLocationIndicator(tmp.getxaxis(), tmp.getyaxis() + 1 * WBIndicator)) {
            tmp2 = board.BoardLocation[tmp.getxaxis()][tmp.getyaxis() + 1 * WBIndicator];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);

            if (tmp3.camp == Camp.Other) {
                AddIfInChessBoard(camp, tmp2);
                Next = true;
            }
        }

        if (getFirstmove() && Next) {
            tmp2 = board.BoardLocation[tmp.getxaxis()][tmp.getyaxis() + 2 * WBIndicator];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);

            if (tmp3.camp == Camp.Other) {
                AddIfInChessBoard(camp, tmp2);
            }
        }

        //吃過路兵
        if (BoardLocationIndicator(tmp.getxaxis() + 1 * WBIndicator, tmp.getyaxis())) {
            tmp2 = board.BoardLocation[tmp.getxaxis() + 1 * WBIndicator][tmp.getyaxis()];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);
            if (tmp3.getPlus2() && tmp3.getRoundOflus2() + 1 == board.getMoveTime()) {
                tmp2 = board.BoardLocation[tmp.getxaxis() + 1 * WBIndicator][tmp.getyaxis() + 1 * WBIndicator];
                AddIfInChessBoard(camp, tmp2);
            }
        }

        if (BoardLocationIndicator(tmp.getxaxis() - 1 * WBIndicator, tmp.getyaxis())) {
            tmp2 = board.BoardLocation[tmp.getxaxis() - 1 * WBIndicator][tmp.getyaxis()];
            tmp3 = (Chess) board.LocationChessKeyValue.get(tmp2);

            if (tmp3.getPlus2() && tmp3.getRoundOflus2() + 1 == board.getMoveTime()) {
                tmp2 = board.BoardLocation[tmp.getxaxis() - 1 * WBIndicator][tmp.getyaxis() + 1 * WBIndicator];
                AddIfInChessBoard(camp, tmp2);
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
                if (this.IdealMovableLocation.contains(WhoAttackMyKing.get(0).getSelfLocation())) {
                    RealMovableLocation.add(WhoAttackMyKing.get(0).getSelfLocation());
                }
                //過路兵專用
                Chess Attacker = WhoAttackMyKing.get(0);
                if (Attacker.getIsPawn() && Attacker.getPlus2() && Attacker.getRoundOflus2() + 1 == board.getMoveTime()
                        && Attacker.getSelfLocation().getyaxis() == this.getSelfLocation().getyaxis()
                        && Attacker.getSelfLocation().getxaxis() - 1 == this.getSelfLocation().getxaxis()) {
                    int i = (this.camp == Camp.White) ? 1 : -1;
                    RealMovableLocation.add(board.BoardLocation[this.getSelfLocation().getxaxis() + 1][this.getSelfLocation().getyaxis() + i]);
                }

                if (Attacker.getIsPawn() && Attacker.getPlus2() && Attacker.getRoundOflus2() + 1 == board.getMoveTime()
                        && Attacker.getSelfLocation().getyaxis() == this.getSelfLocation().getyaxis()
                        && Attacker.getSelfLocation().getxaxis() + 1 == this.getSelfLocation().getxaxis()) {
                    int i = (this.camp == Camp.White) ? 1 : -1;
                    RealMovableLocation.add(board.BoardLocation[this.getSelfLocation().getxaxis() - 1][this.getSelfLocation().getyaxis() + i]);
                }

            } else if (this.ProtectedKingFromBCQ() == null && NumberOfChess == 0) {
                RealMovableLocation.addAll(IdealMovableLocation);
            } else if (this.ProtectedKingFromBCQ() != null && NumberOfChess == 1) {
                //Nothing
            } else if (this.ProtectedKingFromBCQ() != null && NumberOfChess == 0) {
                Chess Attacker = this.ProtectedKingFromBCQ();
                if (Attacker.getIsBishop()) {
                    if (this.IdealMovableLocation.contains(Attacker.getSelfLocation())) {
                        this.RealMovableLocation.add(Attacker.getSelfLocation());
                    }
                } else if (Attacker.getIsCastle()) {
                    for (Location loc : IdealMovableLocation) {
                        if (loc.getxaxis() == Attacker.getSelfLocation().getxaxis()) {
                            this.RealMovableLocation.add(loc);
                        }
                    }
                } else {
                    if (this.IdealMovableLocation.contains(Attacker.getSelfLocation())) {
                        this.RealMovableLocation.add(Attacker.getSelfLocation());
                    }

                    for (Location loc : IdealMovableLocation) {
                        if (loc.getxaxis() == Attacker.getSelfLocation().getxaxis()) {
                            this.RealMovableLocation.add(loc);
                        }
                    }
                }

            }
        }
    }

    //升變   //記得改寫
    @Override
    public void promotion() {
        new PawnPromotion(this);
    }

    //設定"被"吃過路兵的條件
    @Override
    public void setStartyaxis(int i) {
        this.startyaxis = i;
    }

    @Override
    public void setEndyaxis(int i) {
        this.endyaxis = i;
    }

    @Override
    public void setPlus2(int start, int end) {
        this.startyaxis = start;
        this.endyaxis = end;
        if (endyaxis - startyaxis == 2 || endyaxis - startyaxis == -2) {
            Plus2 = true;
            RoundOfPlus2 = board.getMoveTime();
        }
    }

    //設定自己吃過路兵的條件以吃過路兵的及動作   
    @Override
    public void EnPassanted() {
        Location tmp = this.getSelfLocation();
        Location tmp2 = board.BoardLocation[tmp.getxaxis()][tmp.getyaxis() - 1 * WBIndicator];
        Chess tmp3 = board.LocationChessKeyValue.get(tmp2);
        if (tmp3.getIsPawn() && tmp3.getPlus2() && tmp3.getRoundOflus2() + 1 == board.getMoveTime()) {
            board.LocationChessKeyValue.put(tmp2, new NullChess(board));
            tmp2.getJButton().setIcon(null);
        }
    }

    @Override
    public void printChess() {
        try {
            String s = "";
            Location loc = this.getSelfLocation();
            int lloc = loc.getxaxis() + loc.getyaxis();
            if (this.camp == Camp.White && lloc % 2 == 1) {
                s = "src\\img\\WhitePawnInWhite.png";
            } else if (this.camp == Camp.White && lloc % 2 == 0) {
                s = "src\\img\\WhitePawnInBlack.png";
            } else if (this.camp == Camp.black && lloc % 2 == 1) {
                s = "src\\img\\BlackPawnInWhite.png";
            } else if (this.camp == Camp.black && lloc % 2 == 0) {
                s = "src\\img\\BlackPawnInBlack.png";
            }
            File file = new File(s);
            this.image = ImageIO.read(file);
            this.image = zoomImage(image, 4);
            board.grids[7 - loc.getyaxis()][loc.getxaxis()].setIcon(new ImageIcon(this.image));
        } catch (IOException e) {
            System.out.println("系統出錯，無法讀取圖片");
        }
    }

}
