package ChessGame;

import java.awt.Font;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JLabel;

public class TimerTaskForChess extends java.util.TimerTask {

    SupportBoard thisBoard;
    private final int BasicTime;
    private final int CountDown;
    private int CountdownTime;
    private final JLabel thisJLabel;
    private final Camp camp;

    private int Totalsecond;
    private boolean P;                          //讀秒秒數重置判定以及暫停判定
    private int CountDown2;
    
    //以下為音檔
    AudioInputStream FiveSecondSound;
    AudioInputStream FourSecondSound;
    AudioInputStream ThreeSecondSound;
    AudioInputStream TwoSecondSound;
    AudioInputStream OneSecondSound;
    
    AudioInputStream FiveMinSound;
    AudioInputStream OneMinSound;
    AudioInputStream WhiteThreeTimes;
    AudioInputStream WhiteTwoTimes;
    AudioInputStream WhiteLastTimes;
    AudioInputStream BlackThreeTimes;
    AudioInputStream BlackTwoTimes;
    AudioInputStream BlackLastTimes;
    
    TimerTaskForChess(SupportBoard sb, JLabel j, boolean pause, Camp c) {
        thisBoard = sb;
        BasicTime = sb.getBasicTime();
        CountDown = sb.getCountDown();
        CountDown2 = sb.getCountDown();
        CountdownTime = sb.getCountDownTime();
        thisJLabel = j;
        Totalsecond = BasicTime * 60;
        P = pause;
        camp=c;
        
        try {
        	 FiveSecondSound=AudioSystem.getAudioInputStream(new File("src\\voice\\Five.wav"));
             FourSecondSound=AudioSystem.getAudioInputStream(new File("src\\voice\\Four.wav"));
             ThreeSecondSound=AudioSystem.getAudioInputStream(new File("src\\voice\\Three.wav"));
             TwoSecondSound=AudioSystem.getAudioInputStream(new File("src\\voice\\Two.wav"));
             OneSecondSound=AudioSystem.getAudioInputStream(new File("src\\voice\\One.wav"));   
             FiveMinSound= AudioSystem.getAudioInputStream(new File("src\\voice\\5min.wav"));
             OneMinSound= AudioSystem.getAudioInputStream(new File("src\\voice\\1min.wav"));
            
            if(thisBoard.getCountDown()==30) {
    			WhiteThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White330.wav"));  			
    			WhiteTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White230.wav"));	
    			WhiteLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White130.wav"));
    			BlackThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black330.wav"));
    			BlackTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black230.wav"));
    			BlackLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black130.wav"));
    		}else if(thisBoard.getCountDownTime()==2) {
    			WhiteThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White320.wav"));
    			WhiteTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White220.wav"));
    			WhiteLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White120.wav"));
    			BlackThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black320.wav"));
    			BlackTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black220.wav"));
    			BlackLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black120.wav"));
    		}else{
    			WhiteThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White310.wav"));
    			WhiteTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White210.wav"));
    			WhiteLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\White110.wav"));
    			BlackThreeTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black310.wav"));
    			BlackTwoTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black210.wav"));
    			BlackLastTimes=AudioSystem.getAudioInputStream(new File("src\\voice\\Black110.wav"));
    		}
            
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
    }

    @Override
    public void run() {
  
        if (Totalsecond > 0) {
            //基本時限
            int min = Totalsecond / 60;
            int sec = Totalsecond % 60;
            String minString, secString;
            if (min < 10) {
                minString = "0" + min;
            } else {
                minString = String.valueOf(min);
            }

            if (sec < 10) {
                secString = "0" + sec;
            } else {
                secString = String.valueOf(sec);
            }
            thisJLabel.setText(minString + ":" + secString);
            thisJLabel.setFont(new Font("新細明體", Font.CENTER_BASELINE, 60));

            if (!P) {
                Totalsecond = Totalsecond - 1;
            }
            
            if(Totalsecond == 5*60) {
            	PlaySound(FiveMinSound);
            }
            
            if(Totalsecond == 60) {
            	PlaySound(OneMinSound);         
            }

        } else {
                     
        	if(CountdownTime==3 && CountDown2==CountDown && !P) {
        		if(this.camp==Camp.White) {
        			PlaySound(WhiteThreeTimes);
        		}else {
        			PlaySound(BlackThreeTimes);
        		}      		
        		
        	}else if(CountdownTime==2 && CountDown2==CountDown && !P) {
        		if(this.camp==Camp.White) {
        			PlaySound(WhiteTwoTimes);
        		}else {
        			PlaySound(BlackTwoTimes);
        		}     		
        	}else if(CountdownTime==1 && CountDown2==CountDown && !P) {
        		if(this.camp==Camp.White) {
        			PlaySound(WhiteLastTimes);
        		}else {
        			PlaySound(BlackLastTimes);
        		}
        	}
        	        	
        	if(CountDown2==5&& !P) {
        		PlaySound(FiveSecondSound);
        	}
        	if(CountDown2==4&& !P) {
        		PlaySound(FourSecondSound);
        	}
        	if(CountDown2==3&& !P) {
        		PlaySound(ThreeSecondSound);
        	}
        	if(CountDown2==2&& !P) {
        		PlaySound(TwoSecondSound);
        	}
        	if(CountDown2==1&& !P) {
        		PlaySound(OneSecondSound);
        	}
        	
            //讀秒
            if (CountDown2 >= 0 && CountdownTime>0) {
                thisJLabel.setText(CountdownTime + ":" + CountDown2);
                thisJLabel.setFont(new Font("新細明體", Font.CENTER_BASELINE, 60));
            }else if(CountDown2 == -1 && CountdownTime>1){
                CountDown2= CountDown;
                CountdownTime=CountdownTime--;
            }else if(CountDown2 == -1 && CountdownTime==1){
                if(camp==Camp.White){
                    thisBoard.info.setText("White run out of time.");
                    thisBoard.info2.setText("Black win.");
                    thisBoard.WhiteTimer.cancel();
                    thisBoard.BlackTimer.cancel(); 
                }else{
                    thisBoard.info.setText("Black run out of time.");
                    thisBoard.info2.setText("White win.");
                    thisBoard.WhiteTimer.cancel();
                    thisBoard.BlackTimer.cancel();                  
                }
                
                for(Location loc:thisBoard.Chessboard.LocationChessKeyValue.keySet()){
                    loc.getJButton().setEnabled(false);
                }
            }
            
            if(P){
                CountDown2 = CountDown;
            }else {
            	CountDown2 = CountDown2 - 1;
            }
            
        }

    }

    public void setPause(boolean b) {
        P = b;
    }
    
    public void PlaySound(AudioInputStream audioInputStream) {
    	AudioFormat audioFormat=audioInputStream.getFormat();
		int bufferSize = (int) Math.min(audioInputStream.getFrameLength() * audioFormat.getFrameSize(), Integer.MAX_VALUE); 
    	//緩衝大小，如果音訊檔案不大，可以全部存入緩衝空間。這個數值應該要按照用途來決定
    	DataLine.Info dataLineInfo = new DataLine.Info(Clip.class, audioFormat, bufferSize);
    	Clip clip;
		try {
			clip = (Clip) AudioSystem.getLine(dataLineInfo);
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}  		
   
    }

}
