package chess.ChessDesign;

import ChessGame.ChessBoard;
import ChessGame.Camp;

public class WhitePawn extends Pawn {

    public WhitePawn(ChessBoard b) {
        this.board = b;
        this.camp = Camp.White;
        this.PrintToScreen = '\u2659';
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
