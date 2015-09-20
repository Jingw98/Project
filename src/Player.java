package se206_a3;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class Player {
	JFrame frame;
	JPanel contentPane;
	JMenuBar menuBar;
	JFileChooser fileSelector;
	BufferedReader stdoutBuffered;
	File currentDir;
	File videoFile;
	JPanel panelFestival;
	JPanel panelSouth;
	JSlider move;
	JPanel panelFunction, panelVolume;
	JButton stop, backward, forward, play, mute;
	Timer timer, forwardTimer, backwardTimer;
	int forwardSpeed = 500, backwardSpeed = -500;
	// int width;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;

	public Player() throws IOException {
		initialize();
	}

	private void initialize() throws IOException {
		frame = new JFrame();

		frame.setBounds(100, 100, 800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mediaPlayerComponent.release();
				System.exit(0);
			}
		});
		initialSound();

		try {
			setCurentDir();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;

		// player
		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		contentPane.add(mediaPlayerComponent, BorderLayout.CENTER);

		// Menu bar
		menuBar = menuBar();
		frame.setJMenuBar(menuBar);

		// file select
		fileSelector = new JFileChooser();
		fileSelector.setCurrentDirectory(currentDir);

		// South function panel
		panelSouth = new JPanel();
		panelSouth.setLayout(new BorderLayout());

		// festival panel in south
		panelFestival = feativalPanel();
		panelSouth.add(panelFestival, BorderLayout.SOUTH);

		// move slider in south
		move = moveSlider();
		panelSouth.add(move, BorderLayout.NORTH);

		// function button panel in south
		panelFunction = functionPanel();
		setButton(play, "12.png");
		setButton(forward, "1.png");
		setButton(backward, "9.png");
		setButton(stop, "11.png");
		panelSouth.add(panelFunction, BorderLayout.WEST);

		// timer
		forwardTimer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().skip(forwardSpeed);
			}
		});
		backwardTimer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaPlayerComponent.getMediaPlayer().skip(backwardSpeed);
			}
		});
		
		     //volume panel in south
		panelVolume=volumePanel();
		panelSouth.add(panelVolume, BorderLayout.EAST);

		contentPane.add(panelSouth, BorderLayout.SOUTH);
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}

	
	private JPanel volumePanel() {
		JPanel panelVolume =new JPanel();
		 mute = new JButton();
		 mute.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if(!mediaPlayerComponent.getMediaPlayer().isMute()){
	            		mediaPlayerComponent.getMediaPlayer().mute(true);
	            		setButton(mute,"14.png");
	            		
	            	}else{
	            		mediaPlayerComponent.getMediaPlayer().mute(false);
	            		setButton(mute,"6.png");
	            	}
	            	
	            }
	        });
		mute.setEnabled(false);
		panelVolume.add(mute);
		setButton(mute, "6.png");
		JSlider voice = new JSlider(0, 100, 0); 
		 voice.setOpaque(false);
		voice.setPreferredSize(new Dimension(120, 20));
		voice.setValue(100);
		panelVolume.add(voice);
		return panelVolume;
	}
	
	private JPanel feativalPanel() {
		// text field and button in festival panel
		JPanel panelFestival = new JPanel();
		JTextField textField = new JTextField();

		textField.setPreferredSize(new Dimension(frame.getWidth() - 85, 20));
		JButton festival = new JButton();
		festival.setPreferredSize(new Dimension(40, 20));

		panelFestival.add(textField);
		panelFestival.add(festival, BorderLayout.AFTER_LINE_ENDS);
		panelFestival.setOpaque(false);

		return panelFestival;
	}

	private JPanel functionPanel() {
		panelFunction = new JPanel();
		stop = new JButton();
stop.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
    	mediaPlayerComponent.getMediaPlayer().stop();
    	 stop.setEnabled(false);
    	play.setEnabled(false);
     	backward.setEnabled(false);
         forward.setEnabled(false);
         mute.setEnabled(false);
        // saveMP3Button.setEnabled(false);
    }
});
		stop.setEnabled(false);
		panelFunction.add(stop);

		backward = new JButton();
		backward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (forwardTimer.isRunning()){
            		forwardTimer.stop();
            	}
            	setButton(play, "12.png");
            	
            	mediaPlayerComponent.getMediaPlayer().mute(true);
            	
            	if(!backwardTimer.isRunning()){
            		backwardSpeed = -500;
            	}else{
            		if(backwardSpeed < -3000){
            			backwardSpeed = backwardSpeed - 500;
            		}else{
            			backwardSpeed = -500;
            		}
            	}
            	backwardTimer.start();
            	
            }
        });
		
		backward.setEnabled(false);
		panelFunction.add(backward);

		play = new JButton();

		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (forwardTimer.isRunning()) {

					forwardTimer.stop();

					mediaPlayerComponent.getMediaPlayer().mute(false);
					mediaPlayerComponent.getMediaPlayer().play();
					setButton(play, "4.png");
				} else if (backwardTimer.isRunning()) {
					backwardTimer.stop();

					mediaPlayerComponent.getMediaPlayer().mute(false);
					mediaPlayerComponent.getMediaPlayer().play();
					setButton(play, "4.png");

				} else {

					if (mediaPlayerComponent.getMediaPlayer().isPlaying()) {
						setButton(play, "12.png");
						mediaPlayerComponent.getMediaPlayer().pause();
					} else {
						mediaPlayerComponent.getMediaPlayer().play();
						setButton(play, "4.png");
					}
				}
			}

		});
		play.setEnabled(false);
		panelFunction.add(play);

		forward = new JButton();
		forward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (backwardTimer.isRunning()){
            		backwardTimer.stop();
            	}
            	setButton(play, "12.png");
            	mediaPlayerComponent.getMediaPlayer().mute(true);
            	
            	if(!forwardTimer.isRunning()){
            		forwardSpeed = 500;
            	}else{
            		if(forwardSpeed < 3000){
            			forwardSpeed = forwardSpeed + 550;
            		}else{
            			forwardSpeed = 500;
            		}
            	}
            	forwardTimer.start();
            }
        });
		forward.setEnabled(false);
		panelFunction.add(forward);

		// panelFunction.setOpaque(false);

		return panelFunction;

	}

	private JSlider moveSlider() {
		JSlider move = new JSlider();
		return move;

	}

	private JMenuBar menuBar() {
		// Menu Bar
		JMenuBar menubar = new JMenuBar();

		JMenu menu1 = new JMenu("File");

		JMenu menu2 = new JMenu("Other");
		menubar.add(menu1);

		menubar.add(menu2);
		JMenuItem item1 = new JMenuItem("Open File");
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				playVideo();
				mediaPlayerComponent.getMediaPlayer().mute(false);
			}
		});

		menu1.add(item1);

		// menu1.addSeparator();
		return menubar;

	}

	private void playVideo() {

		File selectedFile = null;
		fileSelector.setSelectedFile(null);
		fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileSelector.showSaveDialog(null);
		selectedFile = fileSelector.getSelectedFile();
		mediaPlayerComponent.getMediaPlayer().mute(false);
		if (selectedFile.exists()) {
			videoFile = selectedFile;
			String mediapath = videoFile.getAbsolutePath();
			mediaPlayerComponent.getMediaPlayer().playMedia(mediapath);
			enableButtons();
		}

	}

	private void setCurentDir() throws IOException {
		stdoutBuffered = CallBash.callBashBuffer("pwd");

		currentDir = new File(stdoutBuffered.readLine());

	}

	

	private void initialSound() throws IOException {
		stdoutBuffered = CallBash
				.callBashBuffer("test -d ./.soundFile; echo $?");

		if (!stdoutBuffered.readLine().equals("0")) {
			CallBash.callBashVoid("mkdir ./.soundFile");
		}
	}

	private void setButton(JButton b, String dir) {
		ImageIcon img = new ImageIcon(System.getProperty("user.dir") + "/res/"
				+ dir);
		b.setIcon(img);
		b.setPreferredSize(new Dimension(40, 40));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
		b.setOpaque(false);
	}

	private void enableButtons() {
		play.setEnabled(true);
		setButton(play, "4.png");
		backward.setEnabled(true);
		forward.setEnabled(true);
		mute.setEnabled(true);
		stop.setEnabled(true);
	}
}
