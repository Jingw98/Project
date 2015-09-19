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

public class Tutorial {
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
    static JLabel videoTime = new JLabel();
    JTextField inputField = new JTextField();
    Timer videoTimer, forwardTimer, backwardTimer;
    int forwardSpeed = 500, backwardSpeed = -500;
    static String totalTime = "00:00:00";
	static String playTime ="00:00:00";
    
    
    

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new Tutorial(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

    public Tutorial(String[] args) throws IOException {    	
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
            	}else if(pauseButton.getText().equals("Resume")){
            		pauseButton.setText("Pause");
            		mediaPlayerComponent.getMediaPlayer().play();
            	}
            	
            }
        });
        pauseButton.setEnabled(false);
        controlsPane.add(pauseButton);
        
        //Mute
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	mediaPlayerComponent.getMediaPlayer().mute();
            }
        });
        muteButton.setEnabled(false);
        controlsPane.add( muteButton);
        
        //Skip backward
        backwardTimer = new Timer(50, new ActionListener() {
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
            			backwardSpeed = backwardSpeed - 250;
            		}else{
            			backwardSpeed = -500;
            		}
            	}
            	backwardTimer.start();
            	
            }
        });
        skipBackwardButton.setEnabled(false);
        controlsPane.add(skipBackwardButton);
        
        
        //Skip forward
        forwardTimer = new Timer(50, new ActionListener() {
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
            			forwardSpeed = forwardSpeed + 250;
            		}else{
            			forwardSpeed = 500;
            		}
            	}
            	forwardTimer.start();
            }
        });
        skipForwardButton.setEnabled(false);
        controlsPane.add(skipForwardButton);
        
        //Time
        videoTimer= new Timer(50, new ActionListener() {
        	public void actionPerformed(ActionEvent e){
        		
        		videoTime .setText(playTime + " / " + totalTime);
            }
        });
     
        
        controlsPane.add( videoTime );
        
        
        //Text input field
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		stdoutBuffered = CallBash.callBashBuffer("echo \"" + inputField.getText() + "\" |  wc -w");
            		int word = Integer.parseInt(stdoutBuffered.readLine());
            		if ( word < 40 && word > 0){
            			//rm -rfv
            			CallBash.callBashVoid("echo \"" + inputField.getText() + "\" |  festival --tts");
            			CallBash.callBashVoid("rm -rfv ./.soundFile/*");
    					CallBash.callBashVoid("echo \"" + inputField.getText() + "\" |  text2wave -o ./.soundFile/audio.wav; lame ./.soundFile/audio.wav ./.soundFile/audio.mp3");
    					saveMP3Button.setEnabled(true);
    					//echo "Hello"| text2wave -o  /home/jeffmc/Uni/audio.wav
    					//lame  /home/jeffmc/Uni/audio.wav  /home/jeffmc/Uni/audio.mp3
            		}else{
            			//show error window
            			saveMP3Button.setEnabled(false);
            		}
            		
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	inputField.setText("");
            }
        });
        controlsPane.add(inputField);
        
        //Volume
        voice = new JSlider(0, 100, 0); 
		// voice.setOpaque(false);
		voice.setPreferredSize(new Dimension(120, 20));
		voice.setValue(50);
		voice.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				mediaPlayerComponent.getMediaPlayer().setVolume(voice.getValue() * 2);
			}
	    });
		//voice.setUI(new SliderUI());
		//!!!!!!!!!
		controlsPane.add(voice);
        
        //Save as MP3
        saveMP3Button.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
        		File directoryFile;
        		fileSelector.setSelectedFile(null);
            	fileSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        		fileSelector.showSaveDialog(null);
        		directoryFile = fileSelector.getSelectedFile();
        		directoryPath = directoryFile.getAbsolutePath();
        		
        		try {
					CallBash.callBashVoid("mv ./.soundFile/audio.mp3 "+ directoryPath );
					//mv /home/jeffmc/Uni/audio.mp3 /home/jeffmc/Uni/aaa
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        saveMP3Button.setEnabled(false);
        controlsPane.add(saveMP3Button);
        
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
			mediaPlayerComponent.getMediaPlayer().mute(false);
			mediaPlayerComponent.getMediaPlayer().setVolume(voice.getValue() * 2);
			playTime = "00:00:00";
			stdoutBuffered = CallBash.callBashBuffer("ffmpeg -i " + videoFile.getAbsolutePath() +  " 2>&1 | grep \"Duration\"");
			setTotalTime(stdoutBuffered .readLine());
			
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
    
    
}
