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
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

public class AddMP3 {
	private final JFrame frame;
	//Fields
	private static BufferedReader stdoutBuffered;
	private String directoryPath = "";
	private static String mediapath="";
	private static JFileChooser fileSelector ;
	private static File videoFile;
	private static File currentDir;
    private static  EmbeddedMediaPlayerComponent mediaPlayerComponent;
    JPanel contentPane = new JPanel(new BorderLayout());
    JPanel controlsPane = new JPanel();
    JButton  openButton = new JButton("Open");
    //newwwwwwwwwwwwwwwww
    JButton addMP3_overlap =  new JButton("Overlap");
    JButton addMP3_overlay = new JButton("Overlay");
    static JFrame messageWait;
    //

    
    
    

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
					new AddMP3(args);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }

    public AddMP3(String[] args) throws IOException, InterruptedException {    	
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
		
		//newwwwwwwwwwwwwwwwwwwwwwww
		stdoutBuffered = CallBash.callBashBuffer("test -d ./.videoFile; echo $?");
		if (!stdoutBuffered.readLine().equals("0")){
			CallBash.callBashVoid("mkdir ./.videoFile");
		}
		//
        
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
        
        
        
        //Open file
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		//newwwwwwwwwwwwwwwwwwwwwwwwwwwww chaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaange
					playVideo(chooseFile(new FileNameExtensionFilter("Video File", "avi", "mp4"),  JFileChooser.FILES_ONLY, "video file"));
					//
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	mediaPlayerComponent.getMediaPlayer().mute(false);
            }
        });
        controlsPane.add(openButton);
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
        //Overlap
        addMP3_overlap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String videoPath, audioPath;
            	File videoFile = chooseFile(new FileNameExtensionFilter("Video File", "avi", "mp4"),  JFileChooser.FILES_ONLY, "video file");
            	File audioFile = chooseFile(new FileNameExtensionFilter("MP3", "mp3"),  JFileChooser.FILES_ONLY, "audio file");
            	
            	if(videoFile.exists() && audioFile.exists()){
            		videoPath = videoFile.getAbsolutePath();
                	audioPath = audioFile.getAbsolutePath();
                		messageWait = new Message_Wait();
                		messageWait.setVisible(true);
                		VideoProcesser vp = new VideoProcesser("ffmpeg -y -i " + videoPath + " -i " + audioPath + " -map 0:v -map 1:a ./.videoFile/video.mp4 ");
                		vp.execute();

            	}
            	
            	
            	
            	//“ffmpeg -y -i sample_video_big_buck_bunny_1_minute.AVI -i Wind_Scene.mp3 -map 0:v -map 1:a out.mp4
            }
        });
        controlsPane.add(addMP3_overlap);
        //
        
        //newwwwwwwwwwwwwwwwwwwwwwwwwwww
        //Overlay
        addMP3_overlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String videoPath, audioPath;
            	File videoFile = chooseFile(new FileNameExtensionFilter("Video File", "avi", "mp4"),  JFileChooser.FILES_ONLY, "video file");
            	File audioFile = chooseFile(new FileNameExtensionFilter("MP3", "mp3"),  JFileChooser.FILES_ONLY, "audio file");
            	videoPath = videoFile.getAbsolutePath();
            	audioPath = audioFile.getAbsolutePath();
            	
            	if(videoFile.exists() && audioFile.exists()){
            		videoPath = videoFile.getAbsolutePath();
                	audioPath = audioFile.getAbsolutePath();
                		messageWait = new Message_Wait();
                		messageWait.setVisible(true);
                		VideoProcesser vp = new VideoProcesser("ffmpeg -y -i " + videoPath + " -i " + audioPath + " -filter_complex amix=inputs=2 ./.videoFile/video.mp4 ");
                		vp.execute();

            	}

            }
        });
        controlsPane.add(addMP3_overlay);
        //
        
        
        contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);
        contentPane.add(controlsPane, BorderLayout.SOUTH);
        
        frame.setContentPane(contentPane);
        frame.setVisible(true);
        

    }
    //newwwwwwwwwwwwwwwwwwwwwwwwwwwwwww
    private File chooseFile(FileFilter filter, int mode, String fileType){
    	fileSelector.setDialogTitle("Please select a " + fileType);
    	fileSelector.setFileFilter(filter);
    	fileSelector.setSelectedFile(null);
    	fileSelector.setFileSelectionMode(mode);
		fileSelector.showSaveDialog(null);
		return fileSelector.getSelectedFile();
		
    }
    //
    
    private static void playVideo(File selectedFile) throws IOException{
    	//Deleteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
    	/*
    	//newwwwwwwwwwwwwwwww
    	FileFilter filter = new FileNameExtensionFilter("Video File", "avi", "mp4"); 
    	fileSelector.setFileFilter(filter);
    	//
    	fileSelector.setSelectedFile(null);
    	fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileSelector.showSaveDialog(null);
		selectedFile = fileSelector.getSelectedFile();*/
    	//
		if (selectedFile.exists() ){
			videoFile = selectedFile;
			mediapath = videoFile.getAbsolutePath();
			mediaPlayerComponent.getMediaPlayer().startMedia(mediapath);
			mediaPlayerComponent.getMediaPlayer().mute(false);
			stdoutBuffered = CallBash.callBashBuffer("ffmpeg -i " + videoFile.getAbsolutePath() +  " 2>&1 | grep \"Duration\"");
			
			enableButtons();
		}
		
		
    }
    
    public static void generateDone() throws IOException{
    	messageWait.setVisible(false);
		messageWait.dispose();
    	playVideo(new File("./.videoFile/video.mp4"));
    }
    
    private static void enableButtons(){
    }
    
    private void setCurentDir() throws IOException{
    	stdoutBuffered = CallBash.callBashBuffer("pwd");
    	currentDir = new File (stdoutBuffered .readLine());
    }
    

}
//newwwwwwwwwwwwwwwwwwwwwwwwwww
class VideoProcesser extends SwingWorker<Void, Integer>{
	String command;
	public VideoProcesser(String cmd){
		command = cmd;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		CallBash.callBashVoidWait(command);
		return null;
	}
	
	@Override
	protected void done() {
		
		try {
			AddMP3.generateDone();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	}
//
