package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;
import ChessGame.Location;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;


public class Chess {

    protected boolean firstmove = true;
    protected Camp camp;                 //棋子的陣營
    protected Boolean isSelected;        //棋子是否被選取
    public ArrayList<Location> ExtendedMovableLocation = new ArrayList<>();  //判斷哪些地方對方國王不可進入，對國王專用
    public ArrayList<Location> IdealMovableLocation = new ArrayList<>();  //棋子理論上可到達的座標
    public ArrayList<Location> RealMovableLocation = new ArrayList<>();  //棋子實際上可到達的座標(受自家國王牽制)
    protected ChessBoard board;                  //棋子所屬的棋盤
    protected char PrintToScreen = 'x';     //棋子的unicode
    protected boolean beEnPassanted = false;  //判定自己是否被吃過路兵，步兵專用
    protected int RoundOfPlus2 = 0;         //判斷在哪個回合走兩格，步兵專用
    protected boolean Plus2 = false;        //判斷一開始是否走兩格，步兵專用
    protected BufferedImage image;          //棋子圖片

    //棋子能到達的位置以及他們所保護的棋子，對國王專用
    public void ExtendedMoveWay(){
    }
    
    //理論上棋子能到達的位置
    public void IdealMoveWay() {

    }
 
    //受制於國王，實際上能到達的位置
    public void RealMoveWay() {

    }

    //根據棋盤大小所做的限制
    void AddIfInChessBoard(Camp c, Location l) {
        if (l.getxaxis() >= 0 && l.getxaxis() < 8 && l.getyaxis() >= 0 && l.getyaxis() < 8) {
            //確認棋子不超出棋盤      
            for (Map.Entry<Location,Chess> x : board.LocationChessKeyValue.entrySet()) {
                if (l.equals((Location) x.getKey())) {
                    Chess tmpChess = (Chess) x.getValue();
                    if (tmpChess.getCamp() != this.getCamp()) {
                        //確認此格子內的棋子不是己方棋子
                        this.IdealMovableLocation.add(l);
                    }
                }
            }

        }
    }

    public Camp getCamp() {
        return this.camp;
    }

    //得到自己的國王的棋子物件
    public Chess getSelfKing() {
        for (Chess x : board.LocationChessKeyValue.values()) {
            if (x.camp == this.camp && x.getIsKing()) {
                return x;
            }
        }

        return null;
    }

    //控制影像縮放
    public static BufferedImage zoomImage(BufferedImage inputImage, Integer times) {
        try {
            int width = inputImage.getWidth() * times;
            int height = inputImage.getHeight() * times;

            BufferedImage newImage = new BufferedImage(width, height, inputImage.getType());
            Graphics g = newImage.getGraphics();
            g.drawImage(inputImage, 0, 0, width, height, null);
            g.dispose();
            return newImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    //符合條件board.BoardLocation的arrayBound的指示器
    public static boolean BoardLocationIndicator(int i, int j) {
        return i >= 0 && i < 8 && j >= 0 && j < 8;
    }

    //設定及獲取此棋子是不是第一次移動
    public void setFirstmoveFalse() {
        firstmove = false;
    }

    public boolean getFirstmove() {
        return firstmove;
    }

    public void printChess() {

    }

    //獲取此棋子的unicode
    public char getPrintToScreen() {
        return PrintToScreen;
    }

    public boolean getIsKing() {
        return false;
    }

    public boolean getIsPawn() {
        return false;
    }

    public boolean getIsCastle() {
        return false;
    }

    public boolean getIsQueen() {
        return false;
    }

    public boolean getIsBishop() {
        return false;
    }

    public boolean getIsKnight() {
        return false;
    }

    //得到自己的位置
    public Location getSelfLocation() {
        //取得自己在棋盤上的位置
        Location tmp = new Location(0, 0, board);
        for (Map.Entry<Location,Chess> x : board.LocationChessKeyValue.entrySet()) {
            if (x.getValue() == this) {
                tmp = (Location) x.getKey();
                break;
            }
        }
        return tmp;
    }

    //以下6項方法和函數為步兵專用，用來吃過路兵
    public void setStartyaxis(int i) {

    }

    public void setEndyaxis(int i) {

    }

    public void setPlus2(int i, int j) {
    }

    public boolean getPlus2() {
        return Plus2;
    }

    public int getRoundOflus2() {
        return RoundOfPlus2;
    }

    public void EnPassanted() {

    }

    //以下為升變，為步兵專用的方法
    public void promotion() {

    }

    //以下為入堡，為城堡專用的方法
    public void Castling() {

    }

    public Chess ProtectedKingFromBCQ() {
        //判定自己是否從對方的主教，城堡或者皇后中保護自家國王
        Location loc = this.getSelfLocation();
        Location locOfKing= this.getSelfKing().getSelfLocation();

        int[][] v = {{1, 1}, {1, -1}, {-1, -1}, {-1, 1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        //判斷國王在自己的哪裡       
        int[] w = null;
        for (int[] vtmp : v) {
            for (int i = 1; i <= 7; i++) {
                if (BoardLocationIndicator(loc.getxaxis() + vtmp[0] * i, loc.getyaxis() + vtmp[1] * i)) {
                    Chess chess = board.LocationChessKeyValue.get(board.BoardLocation[loc.getxaxis() + vtmp[0] * i][loc.getyaxis() + i * vtmp[1]]);
                    if (chess.camp == Camp.Other) {
                        //Nothing
                    } else if (chess.camp != Camp.Other && locOfKing.equals(board.BoardLocation[loc.getxaxis() + vtmp[0] * i][loc.getyaxis() + i * vtmp[1]])) {
                        w = vtmp;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        if (w == v[0] || w == v[1] || w == v[2] || w == v[3]) {
            for (int i = 1; i <= 7; i++) {
                //確認自己的國王在自己的斜線上後，確認自己的另一端是否有敵方的主教或皇后
                if (BoardLocationIndicator(loc.getxaxis() - w[0] * i, loc.getyaxis() - w[1] * i)) {
                    Location lloc = board.BoardLocation[loc.getxaxis() - w[0] * i][loc.getyaxis() - i * w[1]];
                    Chess ch = board.LocationChessKeyValue.get(lloc);
                    if (ch.camp == Camp.Other) {
                        //Nothing
                    } else if (ch.camp != Camp.Other && ((ch.getIsBishop() && ch.camp != this.camp) || (ch.getIsQueen() && ch.camp != this.camp))) {
                        return ch;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        if (w == v[4] || w == v[5] || w == v[6] || w == v[7]) {
            for (int i = 1; i <= 7; i++) {
                //確認自己的國王在自己的直線上後，確認自己的另一端是否有敵方的城堡或皇后
                if (BoardLocationIndicator(loc.getxaxis() - w[0] * i, loc.getyaxis() - w[1] * i)) {
                    Location lloc = board.BoardLocation[loc.getxaxis() - w[0] * i][loc.getyaxis() - i * w[1]];
                    Chess ch = board.LocationChessKeyValue.get(lloc);
                    if (ch.camp == Camp.Other) {
                        continue;
                    } else if (ch.camp != Camp.Other && ((ch.getIsCastle() && ch.camp != this.camp) || (ch.getIsQueen() && ch.camp != this.camp))) {
                        return ch;
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        return null;
    }

    //告訴對方，走到哪些位置可以幫國王擋住攻擊
    public ArrayList<Location> getBlock() {
        ArrayList<Location> array = new ArrayList<>();
        Chess EnemyKing=null;
        
        for (Chess x : board.LocationChessKeyValue.values()) {
            if (x.camp != this.camp && x.getIsKing()) {
                EnemyKing = x;
            }
        }
        
        if(this.IdealMovableLocation.contains(EnemyKing.getSelfLocation())){
            array.add(this.getSelfLocation());
        }
       
        return array;
    }
    
}
