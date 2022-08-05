package chess.ChessDesign;

import ChessGame.Camp;
import ChessGame.Location;
import java.util.Map;

public class ChessBCQ extends Chess {

    //For Bishop,Castle and Queen.Because they are similar.

    public void ShareBCQExtendedMoveWay(int[][] v) {
        ExtendedMovableLocation.clear();

        Location tmp = getSelfLocation();
        Chess tmp3 = new NullChess(board);

        for (int[] vtmp : v) {
            for (int i = 1; i <= 7; i++) {
                if (BoardLocationIndicator(tmp.getxaxis() + vtmp[0] * i, tmp.getyaxis() + vtmp[1] * i)) {
                    Location tmp2 = board.BoardLocation[tmp.getxaxis() + vtmp[0] * i][tmp.getyaxis() + vtmp[1] * i];
                    for (Map.Entry<Location,Chess> x : board.LocationChessKeyValue.entrySet()) {
                        if (tmp2.equals((Location) x.getKey())) {
                            tmp3 = (Chess) x.getValue();
                        }
                    }
                    
                    this.ExtendedMovableLocation.add(tmp2);

                    if (tmp3.getCamp() != Camp.Other && !(tmp3.getIsKing() && tmp3.getCamp()!= this.camp)) {
                        break;
                    } 
                    
                } else {
                    break;
                }

            }
        }
    }
    
    public void ShareBCQMoveWay(int[][] v) {
        IdealMovableLocation.clear();
        
        Location tmp = getSelfLocation();
        Chess tmp3 = new NullChess(board);
       
        for (int[] vtmp : v) {
            for (int i = 1; i <= 7; i++) {
                if (BoardLocationIndicator(tmp.getxaxis() + vtmp[0] * i, tmp.getyaxis() + vtmp[1] * i)) {
                    Location tmp2 = board.BoardLocation[tmp.getxaxis() + vtmp[0] * i][tmp.getyaxis() + vtmp[1] * i];
                    for (Map.Entry<Location,Chess> x : board.LocationChessKeyValue.entrySet()) {
                        if (tmp2.equals((Location) x.getKey())) {
                            tmp3 = (Chess) x.getValue();
                        }
                    }

                    if (tmp3.getCamp() == Camp.Other) {
                        this.IdealMovableLocation.add(tmp2);
                    } else if (tmp3.getCamp() == this.camp) {
                        break;
                    } else {
                        this.IdealMovableLocation.add(tmp2);
                        break;
                    }
                } else {
                    break;
                }

            }
        }

        getBlock(v);
    }

    public void getBlock(int[][] v) {
        Chess EnemyKing = new NullChess(board);

        for (Chess x : board.LocationChessKeyValue.values()) {
            if (x.camp != this.camp && x.getIsKing()) {
                EnemyKing = x;
            }
        }

        int[] w = null;
        for (int[] vtmp : v) {
            for (int i = 1; i <= 7; i++) {
                if (BoardLocationIndicator(this.getSelfLocation().getxaxis() + vtmp[0] * i,
                        this.getSelfLocation().getyaxis() + vtmp[1] * i)) {
                    if (board.BoardLocation[this.getSelfLocation().getxaxis() + vtmp[0] * i][this.getSelfLocation().getyaxis() + vtmp[1] * i]
                            .equals(EnemyKing.getSelfLocation()) && this.IdealMovableLocation.contains(EnemyKing.getSelfLocation())) {
                        w = vtmp;
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        
        if (this.camp == Camp.White && w != null) {
            for (int i = 0; i <= 7; i++) {
                if ((BoardLocationIndicator(this.getSelfLocation().getxaxis() + w[0] * i,
                        this.getSelfLocation().getyaxis() + w[1] * i))) {
                    Location tmp = board.BoardLocation[this.getSelfLocation().getxaxis() + w[0] * i][this.getSelfLocation().getyaxis() + w[1] * i];
                    Chess chess = board.LocationChessKeyValue.get(tmp);
                    if (chess.camp == Camp.Other || chess.equals(this)) {
                        board.BlackBlock.add(tmp);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        } else if (this.camp == Camp.black && w != null) {
            for (int i = 0; i <= 7; i++) {
                if ((BoardLocationIndicator(this.getSelfLocation().getxaxis() + w[0] * i,
                        this.getSelfLocation().getyaxis() + w[1] * i))) {
                    Location tmp = board.BoardLocation[this.getSelfLocation().getxaxis() + w[0] * i][this.getSelfLocation().getyaxis() + w[1] * i];
                    Chess chess = board.LocationChessKeyValue.get(tmp);
                    if (chess.camp == Camp.Other || chess.equals(this)) {
                        board.WhiteBlock.add(tmp);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

    }

}
