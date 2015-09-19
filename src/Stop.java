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

public class Stop {
	private final JFrame frame;
	//Fields
	private static BufferedReader stdoutBuffered;
	private String directoryPath = "", mediapath="";
	private static JFileChooser fileSelector ;
	private static File videoFile;
	private static File currentDir;
    private  EmbeddedMediaPlayerComponent mediaPlayerComponent;
    JPanel contentPane = new JPanel(new BorderLayout());
    JPanel controlsPane = new JPanel();
    JButton  openButton = new JButton("Open");
    JButton stop = new JButton("Stop");
    

    
    
    

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new Stop(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

    public Stop(String[] args) throws IOException {    	
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
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
        //Stop
        stop.setEnabled(false);
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	mediaPlayerComponent.getMediaPlayer().stop();
            	 stop.setEnabled(false);
            	 pauseButton.setEnabled(false);
             	skipBackwardButton.setEnabled(false);
                 skipForwardButton.setEnabled(false);
                 muteButton.setEnabled(false);
                 saveMP3Button.setEnabled(false);
            }
        });
        controlsPane.add(stop);
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
			enableButtons();
		}
		
		
    }
    
    //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
    private void enableButtons(){
    	//add into
    	stop.setEnabled(true);
    	
    }
    //
    
    private void setCurentDir() throws IOException{
    	stdoutBuffered = CallBash.callBashBuffer("pwd");
    	currentDir = new File (stdoutBuffered .readLine());
    }
    
   
    
    
}
