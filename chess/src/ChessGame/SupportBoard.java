package ChessGame;

import java.awt.Button;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import java.util.Timer;

public final class SupportBoard extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ChessBoard Chessboard;

    private int BasicTime;
    private int CountDown;
    private int CountdownTime;
    private Button submit;

    public JComboBox<Object> c1, c2, c3;

    String[] BasicTimeSetting = {"0", "5", "10", "20", "30", "60"};  //基本時限
    String[] CountdownSetting = {"10", "20", "30"};       //讀秒
    String[] CountdownTimeSetting = {"1", "2", "3"};            //讀秒次數

    public Button WhiteResign, BlackResign, WhiteTie, BlackTie;   //雙方投降及和局的按鈕
    public JLabel info, info2, WhiteStopwatch, BlackStopwatch;
    public Timer WhiteTimer = new Timer(); //建立兩個Timer物件(計時器)
    public Timer BlackTimer = new Timer();
    public TimerTaskForChess WhiteTask;
    public TimerTaskForChess BlackTask;
    private boolean wTie = false;
    private boolean bTie = false;

    public SupportBoard(ChessBoard b) throws HeadlessException {
        this.setLayout(new GridLayout(15, 1));
        this.Chessboard = b;

        setUIFont(new FontUIResource("新細明體", Font.CENTER_BASELINE, 40));
        this.setTitle("Chess Board Setting");
        this.setSize(400, 800);
        this.setLocation(800, 0);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTimeLimit();         //這裡必須在setVisible(true)之前
        this.setVisible(true);
       
        submit.addActionListener((ActionEvent ae) -> {
            BasicTime = Integer.parseInt(BasicTimeSetting[c1.getSelectedIndex()]);
            CountDown = Integer.valueOf(CountdownSetting[c2.getSelectedIndex()]);
            CountdownTime = Integer.valueOf(CountdownTimeSetting[c3.getSelectedIndex()]);
            b.setVisible(true);
            getContentPane().removeAll();
            revalidate();       //當 container 中加入新的元件或改變元件大小, 
            //需要通知 container 做調整時, 你可以呼叫 revalidate.
            repaint();          //重新繪製jframe
            CreateTwoTimer();
        });
    }

    //設定字型大小用
    public void setUIFont(FontUIResource fui) {
        Enumeration<?> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof FontUIResource) {
                UIManager.put(key, fui);
            }
        }
    }

	void setTimeLimit() {
        add(new JLabel("", 0));
        add(new JLabel("Please set the time", 0));  //0置中、2靠左、4靠右
        add(new JLabel("limit of this game.", 0));
        add(new JLabel("", 0));
        add(new JLabel("Basic Time (min):", 0));  //0置中、2靠左、4靠右
        c1 = new JComboBox<Object>(BasicTimeSetting);  
        add(c1);
        add(new JLabel("CountDown (s):", 0));  //0置中、2靠左、4靠右
        c2 = new JComboBox<Object>(CountdownSetting);
        add(c2);
        add(new JLabel("CountTime (times):", 0));  //0置中、2靠左、4靠右
        c3 = new JComboBox<Object>(CountdownTimeSetting);
        add(c3);
        add(new JLabel("", 0));

        submit = new Button("Submit & GameStart");
        submit.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));
        add(submit);
    }

    void CreateTwoTimer() {
        this.setLayout(new GridLayout(14, 1));                                    //版面

        WhiteResign = new Button("WhiteResign");
        WhiteResign.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));    //雙方的投降按鈕
        BlackResign = new Button("BlackResign");
        BlackResign.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));

        WhiteTie = new Button("TieRequest");
        WhiteTie.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));
        BlackTie = new Button("TieRequest");
        BlackTie.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));

        WhiteStopwatch = new JLabel("", 0);
        WhiteStopwatch.setFont(new Font("新細明體", Font.CENTER_BASELINE, 70)); //雙方碼表
        BlackStopwatch = new JLabel("", 0);
        BlackStopwatch.setFont(new Font("新細明體", Font.CENTER_BASELINE, 70));
        info = new JLabel("Move" + (Chessboard.getMoveTime() + 1), 0);       //棋局訊息顯示                                           //訊息
        info.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));
        info2 = new JLabel("It's White turn.", 0);                          
        info2.setFont(new Font("新細明體", Font.CENTER_BASELINE, 40));

        WhiteTimer = new Timer(); //建立兩個Timer物件(計時器)
        BlackTimer = new Timer();
        WhiteTask = new TimerTaskForChess(this, WhiteStopwatch, false, Camp.White);
        BlackTask = new TimerTaskForChess(this, BlackStopwatch, true, Camp.black);
        WhiteTimer.schedule(WhiteTask, 0, 1000);
        BlackTimer.schedule(BlackTask, 0, 1000);

        WhiteTie.addActionListener((ActionEvent ae) -> {
            wTie = true;
            if (bTie) {
                info.setText("Tie");
                info2.setText("by argeement");
                WhiteTimer.cancel();			//關閉碼表
                BlackTimer.cancel();
 
                for (Location loc : this.Chessboard.LocationChessKeyValue.keySet()) {
                    loc.getJButton().setEnabled(false);
                }
                
            } else {
                info.setText("White requeset for tie.");
                info2.setText("If reject, please keep playing chess");
                BlackTie.setLabel("Accept");
            }
            revalidate();
            repaint();
        });

        BlackTie.addActionListener((ActionEvent ae) -> {
            bTie = true;
            if (wTie) {
                info.setText("Tie");
                info2.setText("by agreement");
                WhiteTimer.cancel();
                BlackTimer.cancel();
              
                this.Chessboard.LocationChessKeyValue.keySet().forEach((loc) -> {
                    loc.getJButton().setEnabled(false);
                });
            } else {
                info.setText("Black requeset for tie.");
                info2.setText("If reject,keep playing");
                WhiteTie.setLabel("Accept");
            }
            revalidate();
            repaint();
        });

        WhiteResign.addActionListener((ActionEvent ae) -> {
            this.BlackResign.setEnabled(false);
            this.info.setText("White Resign,Black win");
            
            WhiteTimer.cancel();
            BlackTimer.cancel();        

            for (Location loc : 
                    this.Chessboard.LocationChessKeyValue.keySet()) {
                loc.getJButton().setEnabled(false);          
            }
            
            revalidate();
            repaint();
        });

        BlackResign.addActionListener((ActionEvent ae) -> {
            this.WhiteResign.setEnabled(false);
            this.info.setText("Black Resign,White win");
            for (Location loc : this.Chessboard.LocationChessKeyValue.keySet()) {
                loc.getJButton().setEnabled(false);               
            }
            
            WhiteTimer.cancel();
            BlackTimer.cancel();        
         
            revalidate();
            repaint();
        });

        add(BlackResign);
        add(new JLabel("", 0));
        add(BlackTie);
        add(new JLabel("", 0));
        add(BlackStopwatch);
        add(new JLabel("", 0));
        add(info);
        add(info2);
        add(new JLabel("", 0));
        add(WhiteStopwatch);
        add(new JLabel("", 0));
        add(WhiteTie);
        add(new JLabel("", 0));
        add(WhiteResign);
        revalidate();       //當 container 中加入新的元件或改變元件大小, 需要通知 container 做調整時, 
        //你可以呼叫 revalidated.
        repaint();          //重新繪製jframe
    }

    public int getBasicTime() {
        return BasicTime;
    }

    public int getCountDown() {
        return CountDown;
    }

    public int getCountDownTime() {
        return CountdownTime;
    }

    public JLabel getWhiteStopWatch() {
        return WhiteStopwatch;
    }

    public JLabel getBlackStopWatch() {
        return BlackStopwatch;
    }

    public void SetTieFalse() {
        wTie = false;
        bTie = false;
    }

}
