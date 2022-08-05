package ChessGame;

import chess.ChessDesign.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public final class ChessBoard extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final Location[][] BoardLocation = new Location[8][8];    //棋子的格子
    public JButton[][] grids = new JButton[8][8];                    //棋子的格子對應到的格子
    //location[i][j] ==>grid[7-j][i]
    private SupportBoard sboard;                                     //輔助用的版面
    private Location FirstSelect;                                    //此項記錄之前選取的格子
    private Chess ChooseToMove = new NullChess(this);                //此項記錄之前選取的棋子
    public Map<Location, Chess> LocationChessKeyValue = new HashMap<>();
    private int MoveTime;                                            //紀錄目前已經動了多少步
    public List<Location> WhiteControl = new ArrayList<>();          //紀錄黑白雙方的勢力，國王不得走對方的勢力範圍
    public List<Location> BlackControl = new ArrayList<>();          //by extendedmoveway
    public int NumberOfAttackWhiteKing = 0;                          //紀錄有幾個棋子正在攻擊國王
    public int NumberOfAttackBlackKing = 0;
    public List<Chess> AttackWhiteKing = new ArrayList<>();          //紀錄那些棋子正在攻擊國王
    public List<Chess> AttackBlackKing = new ArrayList<>();
    public List<Location> WhiteBlock = new ArrayList<>();            //紀錄黑白雙方可幫國王抵擋攻擊的格子
    public List<Location> BlackBlock = new ArrayList<>();
    private boolean WhiteKingLeftCastling = false;                   //此四項為入堡判定專用
    private boolean BlackKingLeftCastling = false;
    private boolean WhiteKingRightCastling = false;
    private boolean BlackKingRightCastling = false;

    public ChessBoard() {
        MoveTime = 0;
        setBoard();
        PlaceChessBeforeGameStart();
        printWholeBoard(this);
        //全部棋子須執行一次MoveWay
        this.FirstSelect = BoardLocation[3][0];
        for (Chess c : LocationChessKeyValue.values()) {
            if (c.getCamp() != Camp.Other) {
                c.IdealMoveWay();
                c.RealMoveWay();
            }
        }
        this.setVisible(false);
        this.setWhichCampMovable(Camp.White);
    }

    //設定棋盤環境
    public void setBoard() {

        this.setLayout(new GridLayout(8, 8));
        sboard = new SupportBoard(this);

        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids.length; j++) {
                grids[i][j] = new JButton();
                grids[i][j].setVisible(true);
                add(grids[i][j]);                      //將此元件加入版面
                grids[i][j].addActionListener(this);   //this需implement ActionListener                
            }
        }

        this.ColoringChessBoard();
        this.setTitle("Chess Game");
        this.setSize(800, 800);
        this.setLocation(0, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

    }

    //為棋盤上色
    public void ColoringChessBoard() {
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[0].length; j++) {
                if ((i + j) % 2 == 0) {
                    grids[i][j].setBackground(Color.WHITE);
                } else {
                    grids[i][j].setBackground(new Color(45, 175, 175));
                }
            }
        }
    }

    //遊戲開始前擺好棋子
    public void PlaceChessBeforeGameStart() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Location tmp = new Location(i, j, this);
                BoardLocation[i][j] = tmp;
                //設定棋盤座標值
            }
        }

        //將每個座標上的棋子擺放好
        //null
        for (Location[] BoardLocation1 : BoardLocation) {
            for (int j = 2; j <= 5; j++) {
                LocationChessKeyValue.put(BoardLocation1[j], new NullChess(this));
            }
        }
        for (Location[] BoardLocation1 : BoardLocation) {
            LocationChessKeyValue.put(BoardLocation1[1], new WhitePawn(this));
            LocationChessKeyValue.put(BoardLocation1[6], new BlackPawn(this));
        } //擺放pawn     
        LocationChessKeyValue.put(BoardLocation[0][0], new Castle(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[7][0], new Castle(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[1][0], new Knight(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[6][0], new Knight(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[2][0], new Bishop(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[5][0], new Bishop(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[3][0], new Queen(this, Camp.White));
        LocationChessKeyValue.put(BoardLocation[4][0], new King(this, Camp.White));
        //擺放白方其他棋子
        LocationChessKeyValue.put(BoardLocation[0][7], new Castle(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[7][7], new Castle(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[1][7], new Knight(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[6][7], new Knight(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[2][7], new Bishop(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[5][7], new Bishop(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[3][7], new Queen(this, Camp.black));
        LocationChessKeyValue.put(BoardLocation[4][7], new King(this, Camp.black));
        //擺放黑方其他棋子
    }

    //設定現在該輪哪一方走棋
    public void setWhichCampMovable(Camp c) {
        LocationChessKeyValue.entrySet().forEach((x) -> {
            Location loc = (Location) x.getKey();
            Chess ch = (Chess) x.getValue();
            loc.getJButton().setEnabled(false);

            if (WhiteKingSurvival() && BlackKingSurvival()) {
                if (c == ch.getCamp()) {
                    loc.getJButton().setEnabled(true);
                }

                if (ChooseToMove.RealMovableLocation.contains(x.getKey())) {
                    loc.getJButton().setEnabled(true);
                }
            }
        });

    }

    //遊戲進行中
    //判定遊戲結束了沒
    //按鈕們定義何謂移動棋子並移動一顆棋子
    @Override
    public void actionPerformed(ActionEvent ae) {

        printWholeBoard(this);
        CalculatMoveWay();

        boolean indicator = true;     //判斷是否沒棋可走
        if (MoveTime % 2 == 1) {
            for (Chess c : this.LocationChessKeyValue.values()) {
                if (c.getCamp() == Camp.black && !c.RealMovableLocation.isEmpty()) {
                    indicator = false;
                    break;
                }
            }

            if (indicator && NumberOfAttackBlackKing == 0) {
                sboard.info2.setText("Tie by Stalemate.");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            } else if (indicator && NumberOfAttackBlackKing > 0) {
                sboard.info2.setText("CheckMate.White win.");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            }

        } else {
            for (Chess c : this.LocationChessKeyValue.values()) {
                if (c.getCamp() == Camp.White && !c.RealMovableLocation.isEmpty()) {
                    indicator = false;
                    break;
                }
            }

            if (indicator && NumberOfAttackWhiteKing == 0) {
                sboard.info2.setText("Tie by Stalemate.");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            } else if (indicator && NumberOfAttackWhiteKing > 0) {
                sboard.info2.setText("CheckMate.black win.");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            }
        }

        //判定哪一個格子被選定
        JButton jb = new JButton();
        for (JButton[] dd : grids) {
            for (JButton d : dd) {
                if (ae.getSource().equals(d)) {
                    jb = d;
                    break;
                }
            }
        }

        //選取格子所對應的Location  
        Location StartLocation = null;
        for (Location loc : this.LocationChessKeyValue.keySet()) {
            if (loc.getJButton().equals(jb)) {
                StartLocation = loc;
                break;
            }
        }

        indicator = false;   //indicator 是用來指示選取的格子不是上一個格子內棋子的可移動範圍
        if (!ChooseToMove.RealMovableLocation.isEmpty()) {
            for (Location loc : ChooseToMove.RealMovableLocation) {
                if (StartLocation.equals(loc)) {
                    indicator = true;
                    break;
                }
            }
        }

        if (!indicator) {
            //如果選取的格子不是上一個格子內棋子的可移動範圍，則...
            FirstSelect = StartLocation;
            ChooseToMove = LocationChessKeyValue.get(StartLocation);

            //著色
            ColoringChessBoard();
            StartLocation.getJButton().setBackground(Color.BLACK);
            if (!ChooseToMove.RealMovableLocation.isEmpty()) {
                for (Location loc : ChooseToMove.RealMovableLocation) {
                    loc.getJButton().setBackground(Color.darkGray);
                }
            }

            //控制哪一方該行棋
            if (MoveTime % 2 == 0) {
                setWhichCampMovable(Camp.White);
            } else {
                setWhichCampMovable(Camp.black);
            }

        } else {
            //如果選取的格子是上一個格子內棋子的可移動範圍，則...
            ColoringChessBoard();
            ChooseToMove.setFirstmoveFalse();
            LocationChessKeyValue.put(StartLocation, ChooseToMove);
            LocationChessKeyValue.put(FirstSelect, new NullChess(this));
            FirstSelect.getJButton().setIcon(null); //清空前一個的棋子影像

            //執行"被"吃過路兵的條件設定，步兵專用
            ChooseToMove.setPlus2(FirstSelect.getyaxis(), StartLocation.getyaxis());
            //執行吃過路兵
            ChooseToMove.EnPassanted();
            //(入堡用)國王是否已移動兩格的判斷
            if (ChooseToMove.getIsKing() && ChooseToMove.getCamp() == Camp.White
                    && FirstSelect.equals(BoardLocation[4][0])
                    && StartLocation.equals(BoardLocation[2][0])) {
                WhiteKingLeftCastling = true;
            }

            if (ChooseToMove.getIsKing() && ChooseToMove.getCamp() == Camp.White
                    && FirstSelect.equals(BoardLocation[4][0])
                    && StartLocation.equals(BoardLocation[6][0])) {
                WhiteKingRightCastling = true;
            }

            if (ChooseToMove.getIsKing() && ChooseToMove.getCamp() == Camp.black
                    && FirstSelect.equals(BoardLocation[4][7])
                    && StartLocation.equals(BoardLocation[2][7])) {
                BlackKingLeftCastling = true;
            }

            if (ChooseToMove.getIsKing() && ChooseToMove.getCamp() == Camp.black
                    && FirstSelect.equals(BoardLocation[4][7])
                    && StartLocation.equals(BoardLocation[6][7])) {
                BlackKingRightCastling = true;
            }

            //執行入堡(Castling)
            for (Map.Entry<Location,Chess> x : LocationChessKeyValue.entrySet()) {
                Chess ch = (Chess) x.getValue();
                ch.Castling();
            }
            //執行升變      
            Location tmp = ChooseToMove.getSelfLocation();
            if (ChooseToMove.getIsPawn() && ChooseToMove.getCamp() == Camp.White
                    && tmp.getyaxis() == 7) {
                ChooseToMove.promotion();
            } else if (ChooseToMove.getIsPawn() && ChooseToMove.getCamp() == Camp.black
                    && tmp.getyaxis() == 0) {
                ChooseToMove.promotion();
            }

            //收尾
            printWholeBoard(this);
            MoveTime = MoveTime + 1;
            //控制哪一方該行棋
            if (MoveTime % 2 == 0) {

                LocationChessKeyValue.entrySet().forEach((x) -> {
                    Location loc = (Location) x.getKey();
                    Chess ch = (Chess) x.getValue();
                    loc.getJButton().setEnabled(false);
                    if (WhiteKingSurvival() && BlackKingSurvival()) {
                        if (Camp.White == ch.getCamp()) {
                            loc.getJButton().setEnabled(true);
                        }

                    }
                });

                sboard.WhiteTask.setPause(false);
                sboard.BlackTask.setPause(true);
                sboard.revalidate();
                sboard.repaint();
            } else {
                LocationChessKeyValue.entrySet().forEach((x) -> {
                    Location loc = (Location) x.getKey();
                    Chess ch = (Chess) x.getValue();
                    loc.getJButton().setEnabled(false);
                    if (WhiteKingSurvival() && BlackKingSurvival()) {
                        if (Camp.black == ch.getCamp()) {
                            loc.getJButton().setEnabled(true);
                        }

                    }
                });
                sboard.WhiteTask.setPause(true);
                sboard.BlackTask.setPause(false);
                sboard.revalidate();
                sboard.repaint();
            }

            //重新設定選定的棋子跟其位置
            for (Map.Entry<Location,Chess> x : LocationChessKeyValue.entrySet()) {
                Chess chess = (Chess) x.getValue();
                if (chess.getIsKing() && chess.getCamp() != ChooseToMove.getCamp()) {
                    FirstSelect = (Location) x.getKey();
                    ChooseToMove = LocationChessKeyValue.get(FirstSelect);
                    FirstSelect.getJButton().setBackground(Color.BLACK);
                    break;
                }
            }

            //印出棋局狀況
            CalculatMoveWay();
            sboard.info.setText("Move " + (this.getMoveTime() + 1));
            if (WhiteKingSurvival() && BlackKingSurvival()) {
                if (this.getMoveTime() % 2 == 1) {
                    sboard.info2.setText("It's black turn.");
                } else {
                    sboard.info2.setText("It's white turn.");
                }
            } else if (!WhiteKingSurvival()) {
                sboard.info2.setText("CheckMate.Black win");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            } else if (!BlackKingSurvival()) {
                sboard.info2.setText("CheckMate.White win");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            } else {
                sboard.info2.setText("Tie by agreement");
                sboard.WhiteTimer.cancel();
                sboard.BlackTimer.cancel();
            }

            //設定平手請求false
            sboard.SetTieFalse();
            sboard.WhiteTie.setLabel("TieRequest");
            sboard.BlackTie.setLabel("TieRequest");
        }
    }

    private void CalculatMoveWay() {
        //判斷棋局情況
        if (!WhiteBlock.isEmpty()) {
            WhiteBlock.clear();
        }
        
        if (!BlackBlock.isEmpty()) {
            BlackBlock.clear();
        }
             
        //每回合結束時，重新判定每一個棋子的控制範圍(對國王而言)
        for (Map.Entry<Location,Chess> x : this.LocationChessKeyValue.entrySet()) {
            Chess tmpchess = (Chess) x.getValue();
            if (tmpchess.getCamp() != Camp.Other) {
                tmpchess.ExtendedMoveWay();
            }
        }
                
        //每回合結束時，重新判定每一個棋子理想上可行走的範圍
        for (Map.Entry<Location,Chess> x : this.LocationChessKeyValue.entrySet()) {
            Chess tmpchess = (Chess) x.getValue();
            if (tmpchess.getCamp() != Camp.Other) {
                tmpchess.IdealMoveWay();
            }
        }

        //判斷誰正在攻擊國王
        WhoIsAttackKing();
        
        //每回合開始時，重新判定那些格子是被黑(白)所控制
        if (!WhiteControl.isEmpty()) {
            WhiteControl.clear();
        }
        if (!BlackControl.isEmpty()) {
            BlackControl.clear();
        }

        for (Map.Entry<Location,Chess> x : this.LocationChessKeyValue.entrySet()) {
            Chess tmpchess = (Chess) x.getValue();
            if (tmpchess.getCamp() == Camp.White) {
               WhiteControl.addAll(tmpchess.ExtendedMovableLocation);
            } else if (tmpchess.getCamp() == Camp.black) {
               BlackControl.addAll(tmpchess.ExtendedMovableLocation);
            }
        }

        //算出RealMovableLocation
        for (Map.Entry<Location,Chess> x : this.LocationChessKeyValue.entrySet()) {
            Chess tmpchess = (Chess) x.getValue();
            if (tmpchess.getCamp() != Camp.Other) {
                tmpchess.RealMoveWay();
            }
        }
    }

    //印出每一個格子上的棋子
    public void printWholeBoard(ChessBoard b) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (Map.Entry<Location,Chess> x : b.LocationChessKeyValue.entrySet()) {
                    if (BoardLocation[j][7 - i].equals((Location) x.getKey())) {
                        Chess c = (Chess) x.getValue();
                        c.printChess();
                        break;
                    }
                }
            }
        }

    }

    public List<Location> getWhiteControl() {
        return WhiteControl;
    }

    public List<Location> getBlackControl() {
        return BlackControl;
    }

    public boolean isControlledByEmeny(Location loc, Camp camp) {
        if (null == camp) {
            return false;
        } else {
            switch (camp) {
                case black:
                    return getWhiteControl().contains(loc);
                case White:
                    return getBlackControl().contains(loc);
                default:
                    return false;
            }
        }
    }
    //判斷黑白國王是否存活
    public boolean WhiteKingSurvival() {
        boolean indicator = false;
        for (Map.Entry<Location, Chess> entry : LocationChessKeyValue.entrySet()) {
            if (entry.getValue().getIsKing() && entry.getValue().getCamp() == Camp.White) {
                indicator = true;
                break;
            }
        }
        return indicator;
    }

    public boolean BlackKingSurvival() {
        boolean indicator = false;
        for (Map.Entry<Location, Chess> entry : LocationChessKeyValue.entrySet()) {
            if (entry.getValue().getIsKing() && entry.getValue().getCamp() == Camp.black) {
                indicator = true;
                break;
            }
        }
        return indicator;
    }

    public int getMoveTime() {
        return MoveTime;
    }

    //判斷黑白國王被誰攻擊
    public void WhoIsAttackKing() {

        Chess WhiteKing = null;
        Chess BlackKing = null;

        //找尋黑白國王
        for (Chess x : LocationChessKeyValue.values()) {
            if (x.getIsKing()) {
                if (x.getCamp() == Camp.White) {
                    WhiteKing = x;
                } else {
                    BlackKing = x;
                }
            }
        }

        AttackBlackKing.clear();
        AttackWhiteKing.clear();
        NumberOfAttackBlackKing = 0;
        NumberOfAttackWhiteKing = 0;

        for (Chess ch : this.LocationChessKeyValue.values()) {
            if (ch.getCamp() != Camp.Other) {
                if (!ch.IdealMovableLocation.isEmpty() && BlackKingSurvival() && WhiteKingSurvival()) {          
                    if (ch.IdealMovableLocation.contains(WhiteKing.getSelfLocation())
                       ) {
                        NumberOfAttackWhiteKing = NumberOfAttackWhiteKing + 1;
                        AttackWhiteKing.add(ch);
                    }

                    if (ch.IdealMovableLocation.contains(BlackKing.getSelfLocation())) {
                        NumberOfAttackBlackKing = NumberOfAttackBlackKing + 1;
                        AttackBlackKing.add(ch);
                    }
                }

            }
        }
    }

    //入堡專用項
    public void setWhiteLeftCastling(boolean b) {
        this.WhiteKingLeftCastling = b;
    }

    public void setWhiteRightCastling(boolean b) {
        this.WhiteKingRightCastling = b;
    }

    public void setBlackLeftCastling(boolean b) {
        this.BlackKingLeftCastling = b;
    }

    public void setBlackRightCastling(boolean b) {
        this.BlackKingRightCastling = b;
    }

    public boolean getWhiteLeftCastling() {
        return WhiteKingLeftCastling;
    }

    public boolean getWhiteRightCastling() {
        return WhiteKingRightCastling;
    }

    public boolean getBlackLeftCastling() {
        return BlackKingLeftCastling;
    }

    public boolean getBlackRightCastling() {
        return BlackKingRightCastling;
    }

}
