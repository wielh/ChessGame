package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;


public class BlackPawn extends Pawn {

    public BlackPawn(ChessBoard b) {
        this.board = b;
        this.camp = Camp.black;
        this.PrintToScreen = '\u265F';
    }

    @Override
    public void ExtendedMoveWay() {
        CommonExtendMoveWay(this.camp);
    }

    
    @Override
    public void IdealMoveWay() {
        CommonMoveWay(this.camp);
    }

}
