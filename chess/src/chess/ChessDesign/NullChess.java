package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;
import ChessGame.Location;
import java.util.ArrayList;


public class NullChess extends chess.ChessDesign.Chess {

    public ArrayList<Location> MovableLocation = new ArrayList<>();  //棋子可到達的座標

    public NullChess(ChessBoard b) {
        this.camp = Camp.Other;
        this.board = b;
        this.PrintToScreen='\uFF38';
    }

    @Override
    public void IdealMoveWay() {

    }
    
    @Override
    public void RealMoveWay() {

    }
 
}
