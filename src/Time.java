package se206_a3;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class Time{
	private final JFrame frame;
	//Fields
	private static BufferedReader stdoutBuffered;
	private String directoryPath = "", mediapath="";
	private static JFileChooser fileSelector ;
	private static File videoFile;
	private static File currentDir;
    private  EmbeddedMediaPlayerComponent mediaPlayerComponent;
    private JSlider move, voice;
    JPanel contentPane = new JPanel(new BorderLayout());
    JButton pauseButton = new JButton("Pause");
    JPanel controlsPane = new JPanel();
    JButton  skipBackwardButton = new JButton("Back");
    JButton  skipForwardButton = new JButton("Skip");
    JButton  openButton = new JButton("Open");
    JButton muteButton = new JButton("Mute");
    JButton saveMP3Button = new JButton("Save as Mp3");
    
    JTextField inputField = new JTextField();
    Timer videoTimer, forwardTimer, backwardTimer;
    //newwwwwwwwwwwwwwwwwww
    static JLabel videoTime = new JLabel();
    boolean sliderSkip = true;
    int forwardSpeed = 500, backwardSpeed = -500;
    int timerCondition = 1;
    static String totalTime = "00:00:00";
	static String playTime ="00:00:00";
	//
    
    
    

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new Time(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

    public Time(String[] args) throws IOException {    	
    	setCurentDir();
    	fileSelector = new JFileChooser();
    	fileSelector .setCurrentDirectory(currentDir);
        frame = new JFrame("Media Player");
        frame.setBounds(100, 100, 1280, 720);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mediaPlayerComponent.release();
                System.exit(0);
            }
        });
        
        stdoutBuffered = CallBash.callBashBuffer("test -d ./.soundFile; echo $?");
		
		if (!stdoutBuffered.readLine().equals("0")){
			CallBash.callBashVoid("mkdir ./.soundFile");
		}
        
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        
        
        
        //Open file
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
					playVideo();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	mediaPlayerComponent.getMediaPlayer().mute(false);
            }
        });
        controlsPane.add(openButton);
        
        //Play and pause
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(forwardTimer.isRunning()){
            		forwardTimer.stop();
            	}else if(backwardTimer.isRunning()){
            		backwardTimer.stop();
            	}
            	mediaPlayerComponent.getMediaPlayer().mute(false);
            	
            	if (pauseButton.getText().equals("Pause")){
            		pauseButton.setText("Resume");
            		mediaPlayerComponent.getMediaPlayer().pause();
            		//newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
            		videoTimer.stop();
            		//
            	}else if(pauseButton.getText().equals("Resume")){
            		pauseButton.setText("Pause");
            		//newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
            		videoTimer.start();
            		//
            		mediaPlayerComponent.getMediaPlayer().play();
            	}
            	
            }
        });
        pauseButton.setEnabled(false);
        controlsPane.add(pauseButton);
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
        //Skip backward
        backwardTimer = new Timer(100, new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		mediaPlayerComponent.getMediaPlayer().skip(backwardSpeed);
            }
        });
        skipBackwardButton .addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (forwardTimer.isRunning()){
            		forwardTimer.stop();
            	}
            	pauseButton.setText("Resume");
            	mediaPlayerComponent.getMediaPlayer().mute(true);
            	
            	if(!backwardTimer.isRunning()){
            		backwardSpeed = -500;
            	}else{
            		if(backwardSpeed < -3000){
            			backwardSpeed = backwardSpeed -500;
            		}else{
            			backwardSpeed = -500;
            		}
            	}
            	backwardTimer.start();
            	
            }
        });
        skipBackwardButton.setEnabled(false);
        controlsPane.add(skipBackwardButton);
        //
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
        //Skip forward
        forwardTimer = new Timer(100, new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		mediaPlayerComponent.getMediaPlayer().skip(forwardSpeed);
            }
        });
        skipForwardButton .addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (backwardTimer.isRunning()){
            		backwardTimer.stop();
            	}
            	pauseButton.setText("Resume");
            	mediaPlayerComponent.getMediaPlayer().mute(true);
            	
            	if(!forwardTimer.isRunning()){
            		forwardSpeed = 500;
            	}else{
            		if(forwardSpeed < 3000){
            			forwardSpeed = forwardSpeed + 500;
            		}else{
            			forwardSpeed = 500;
            		}
            	}
            	
            	forwardTimer.start();
            }
        });
        skipForwardButton.setEnabled(false);
        controlsPane.add(skipForwardButton);
        //
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwww
        //Time
        videoTime .setText(playTime + " / " + totalTime);
        videoTimer= new Timer(100, new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		if(runtimeToSecond(playTime) >= runtimeToSecond(totalTime)){
        			playTime = "00:00:00";
        			videoTimer.stop();
        		}else{
        			playTime = secondToRuntime(Integer.parseInt(Long.toString(mediaPlayerComponent.getMediaPlayer().getTime())) / 1000) ;
            		videoTime .setText(playTime + " / " + totalTime);
            		sliderSkip = false;
            		move.setValue(runtimeToSecond(playTime) * 100 / runtimeToSecond(totalTime));
            		sliderSkip = true;
        		}
        		
            }
        });
        controlsPane.add( videoTime );
        //
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
        //move
        move = new JSlider(0,100,0);
        move.setValue(0);
        move.setPreferredSize(new Dimension(500, 20));
        move.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (sliderSkip){
					int totalTime = Integer.parseInt(Long.toString(mediaPlayerComponent.getMediaPlayer().getLength()));
					int jump = (totalTime * move.getValue()) / 100;
					mediaPlayerComponent.getMediaPlayer().setTime(new Long(jump));
					playTime = secondToRuntime(Integer.parseInt(Long.toString(mediaPlayerComponent.getMediaPlayer().getTime())) / 1000) ;
				}
				
			}
	    });
        controlsPane.add( move);
        //
        
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);
        
        frame.setContentPane(contentPane);
        frame.setVisible(true);
        

    }
    
    private void playVideo() throws IOException{
    	File selectedFile = null;
    	fileSelector.setSelectedFile(null);
    	fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileSelector.showSaveDialog(null);
		selectedFile = fileSelector.getSelectedFile();
		//mediaPlayerComponent.getMediaPlayer().mute(false);
		if (selectedFile.exists() ){
			videoFile = selectedFile;
			mediapath = videoFile.getAbsolutePath();
			mediaPlayerComponent.getMediaPlayer().startMedia(mediapath);
			playTime = "00:00:00";
			stdoutBuffered = CallBash.callBashBuffer("ffmpeg -i " + videoFile.getAbsolutePath() +  " 2>&1 | grep \"Duration\"");
			setTotalTime(stdoutBuffered .readLine());
			//newwwwwwwwwwwwwwwwwwwwwwwwwwwwww
			videoTimer.start();
			//
			enableButtons();
		}
		
		
    }
    
    private void enableButtons(){
    	pauseButton.setEnabled(true);
    	skipBackwardButton.setEnabled(true);
        skipForwardButton.setEnabled(true);
        muteButton.setEnabled(true);
    }
    
    private void setCurentDir() throws IOException{
    	stdoutBuffered = CallBash.callBashBuffer("pwd");
    	currentDir = new File (stdoutBuffered .readLine());
    }
    
    public static void setTotalTime(String str){
    	String msg = "";
   	 Matcher m = Pattern.compile("  Duration: \\w+:\\w+:\\w+").matcher(str);
   	 while(m.find()){
   		 msg = m.group();
   		 msg = msg.replace("  Duration: ", "");
   	 }
      totalTime = msg;
      videoTime .setText(playTime + " / " + totalTime);
   }
    
    public static int runtimeToSecond(String str){
        String[] hms=str.trim().split(":");
        if(hms.length!=3){
            return 0;
        }
        int time=Integer.valueOf(hms[0])*60*60+Integer.valueOf(hms[1])*60+Integer.valueOf(hms[2]);
        return time;
    }
    
    //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
    public static String secondToRuntime(int time){
    	StringBuffer buffer = new StringBuffer();
    	if(time >= 3600){
        		if(time / 3600 > 10){
        			buffer.append(Integer.toString(time/3600) + ":");
        		}else{
        			buffer.append("0" + Integer.toString(time/3600) + ":");
        		}
    		time = time - 3600 * (time/3600);
    	}else{
    		buffer.append("00:");
    	}
    	
    	if(time >=  60){
    		if(time / 60 > 10){
    			buffer.append(Integer.toString(time/60) + ":");
    		}else{
    			buffer.append("0"+Integer.toString(time/60) + ":");
    		}
    		time = time - 60* (time/60);
    	}else{
    		buffer.append("00:");
    	}
    	
    	if(time >= 10){
			buffer.append(Integer.toString(time));
		}else{
			buffer.append("0"+Integer.toString(time));
		}
    	return buffer.toString();
   }
    //
 }
