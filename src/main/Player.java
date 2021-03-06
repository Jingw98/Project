package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import Function.*;
import GUI.AddAudio;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalSliderUI;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

public class Player {
	static JFrame frame;
	JPanel contentPane;
	static JMenuBar menuBar;
	public static JFileChooser fileSelector;
	BufferedReader stdoutBuffered;
	public static File currentDir;
	static File videoFile;
	static String mediaPath="";
	JPanel panelFestival;
	JPanel panelSouth;
	static JSlider move;
	static JSlider voice;
	
	JPanel panelFunction, panelVolume;
	static JButton stop;
	static JButton backward;
	static JButton forward;
	static JButton play;
	static JButton mute;
	JButton festival;
	Timer timer, forwardTimer, backwardTimer;
	static Timer videoTimer;
	int forwardSpeed = 500, backwardSpeed = -500, moveValue;
	static JLabel videoTime;
	JTextField textField;

	boolean sliderSkip = true;

	int timerCondition = 1;
	static String totalTime = "00:00:00";
	static String playTime = "00:00:00";
	private static EmbeddedMediaPlayerComponent mediaPlayerComponent;

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
		FileOperation.initialPath(stdoutBuffered);
		
		//Initialize working directory
		try {
			currentDir = FileOperation.setCurrentDir(stdoutBuffered);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Initialize window size
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
		// Time label
		videoTime = time();
		panelFunction.add(videoTime);
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

		// volume panel in south
		panelVolume = volumePanel();
		panelSouth.add(panelVolume, BorderLayout.EAST);

		contentPane.add(panelSouth, BorderLayout.SOUTH);
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}
	
	//Volume features
	private JPanel volumePanel() {
		JPanel panelVolume = new JPanel();
		
		//Mute Button
		mute = new JButton();
		mute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mediaPlayerComponent.getMediaPlayer().isMute()) {
					mediaPlayerComponent.getMediaPlayer().mute(true);
					setButton(mute, "14.png");

				} else {
					mediaPlayerComponent.getMediaPlayer().mute(false);
					setButton(mute, "6.png");
				}

			}
		});

		mute.setEnabled(false);
		panelVolume.add(mute);
		setButton(mute, "6.png");
		
		//Volume control
		voice = new JSlider(0, 100, 0);
		voice.setOpaque(false);
		voice.setPreferredSize(new Dimension(120, 20));

		voice.setValue(50);
		voice.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {

				mediaPlayerComponent.getMediaPlayer().setVolume(
						voice.getValue() * 2);

				if (mediaPlayerComponent.getMediaPlayer().getVolume() == 0) {
					setButton(mute, "14.png");
				} else if (mediaPlayerComponent.getMediaPlayer().getVolume() != 0) {
					setButton(mute, "6.png");
				}
			}
		});

		panelVolume.add(voice);
		return panelVolume;
	}
	
	//Playtime features
	private JLabel time() {
		//Show the playtime and total time of the video
		videoTime = new JLabel();
		videoTime.setText(playTime + " / " + totalTime);
		videoTimer = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Time.runtimeToSecond(playTime) >= Time.runtimeToSecond(totalTime)) {
					playTime = "00:00:00";
					videoTimer.stop();
				} else {
					playTime = Time.secondToRuntime(Integer.parseInt(Long
							.toString(mediaPlayerComponent.getMediaPlayer()
									.getTime())) / 1000);
					videoTime.setText(playTime + " / " + totalTime);
					sliderSkip = false;
					move.setValue((int) mediaPlayerComponent.getMediaPlayer()
							.getTime());
					sliderSkip = true;
				}

			}
		});
		return videoTime;
	}
	
	//festival related features
	private JPanel feativalPanel() {
		JPanel panelFestival = new JPanel();
		
		//Textfield for text input
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String input = textField.getText();
					int word = input.length();
					if (word <= 40 && word > 0) {

						CallBash.callBashVoid("echo \"" + input
								+ "\" |  festival --tts");
						CallBash.callBashVoid("rm -rfv ./.soundFile/*");
						CallBash.callBashVoid("echo \""
								+ input
								+ "\" |  text2wave -o ./.soundFile/audio.wav; ffmpeg -i ./.soundFile/audio.wav -f mp3 ./.soundFile/audio.mp3");
						festival.setEnabled(true);
					  
					} else {
						if (word == 0) {
							// show error window
							JOptionPane.showMessageDialog(contentPane,
									"Input can not be empty!", null,
									JOptionPane.INFORMATION_MESSAGE);
						}
						else{
						JOptionPane.showMessageDialog(contentPane,
								"Input can not be over 40 words!", null,
								JOptionPane.INFORMATION_MESSAGE);
						}
						festival.setEnabled(false);
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				textField.setText("");
			}
		});
		textField.setPreferredSize(new Dimension(frame.getWidth() - 120, 20));
		
		//Save the festival audio file as MP3 file
		festival = new JButton();
		festival.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File directoryFile;
				fileSelector.setSelectedFile(null);
				fileSelector.setDialogTitle("Please select the saving directory");
				fileSelector
						.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);  
				
				fileSelector.showSaveDialog(null);
				directoryFile = fileSelector.getSelectedFile();
				String directoryPath = directoryFile.getAbsolutePath();
				
				try {
					CallBash.callBashVoid("mv ./.soundFile/audio.mp3 "
							+ directoryPath);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		festival.setPreferredSize(new Dimension(100, 20));
		festival.setEnabled(false);
		festival.setText("Save");

		panelFestival.add(textField);
		panelFestival.add(festival, BorderLayout.AFTER_LINE_ENDS);
		panelFestival.setOpaque(false);

		return panelFestival;
	}
	
	//Basic features
	private JPanel functionPanel() {
		panelFunction = new JPanel();
		
		//Stop
		stop = new JButton();
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopVideo();
				stop.setEnabled(false);
				play.setEnabled(false);
				backward.setEnabled(false);
				forward.setEnabled(false);
				mute.setEnabled(false);
				festival.setEnabled(false);
				
			}
		});
		stop.setEnabled(false);
		panelFunction.add(stop);
		
		//Fast backward(rewind)
		backward = new JButton();
		backward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (forwardTimer.isRunning()) {
					forwardTimer.stop();
				}
				setButton(play, "12.png");

				mediaPlayerComponent.getMediaPlayer().mute(true);

				if (!backwardTimer.isRunning()) {
					backwardSpeed = -500;
				} else {
					if (backwardSpeed < -3000) {
						backwardSpeed = backwardSpeed - 500;
					} else {
						backwardSpeed = -500;
					}
				}
				backwardTimer.start();
				videoTimer.start();

			}
		});

		backward.setEnabled(false);
		panelFunction.add(backward);
		
		//Play, pause and resume
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
						videoTimer.stop();
					} else {
						mediaPlayerComponent.getMediaPlayer().play();
						setButton(play, "4.png");
						videoTimer.start();
					}
				}
			}

		});
		play.setEnabled(false);
		panelFunction.add(play);
		
		//Fast forward(skip)
		forward = new JButton();
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (backwardTimer.isRunning()) {
					backwardTimer.stop();
				}
				setButton(play, "12.png");
				mediaPlayerComponent.getMediaPlayer().mute(true);

				if (!forwardTimer.isRunning()) {
					forwardSpeed = 500;
				} else {
					if (forwardSpeed < 3000) {
						forwardSpeed = forwardSpeed + 500;
					} else {
						forwardSpeed = 500;
					}
				}
				forwardTimer.start();
				videoTimer.start();
			}
		});
		forward.setEnabled(false);
		panelFunction.add(forward);


		return panelFunction;

	}

	//Progress bar
	private JSlider moveSlider() {
		//Progress bar relate to the playtime
		final JSlider move = new JSlider();
		move.setValue(0);
		move.setEnabled(true);
		 move.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					if (sliderSkip){
						mediaPlayerComponent.getMediaPlayer().setTime(move.getValue());
						playTime = Time.secondToRuntime(Integer.parseInt(Long.toString(mediaPlayerComponent.getMediaPlayer().getTime())) /1000) ;
					}
						
				}
		    });
		
		move.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				move.setUI(new MetalSliderUI() {
					protected void scrollDueToClickInTrack(int direction) {
						if (mediaPlayerComponent.getMediaPlayer() != null) {
							int moveValue = move.getValue();
							if (move.getOrientation() == JSlider.HORIZONTAL) {
								moveValue = this.valueForXPosition(move
										.getMousePosition().x);
							}
							move.setValue(moveValue);
							mediaPlayerComponent.getMediaPlayer().setTime(
									moveValue);
						} 
					}
				});
			}
			

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (mediaPlayerComponent.getMediaPlayer() != null) {
					mediaPlayerComponent.getMediaPlayer().setTime(
							move.getValue());
					
					
					
					
				
				}

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		
		
		

		return move;

	}

	//Menu bars
	private JMenuBar menuBar() {
		//Add menu bars
		JMenuBar menubar = new JMenuBar();

		JMenu menu1 = new JMenu("File");

		JMenu menu2 = new JMenu("Other");
		menubar.add(menu1);

		menubar.add(menu2);
		JMenuItem item1 = new JMenuItem("Open File");
		JMenuItem item2 = new JMenuItem("Add Audio");
		
		//Open video file
		item1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					playVideo(FileOperation.chooseFile(fileSelector,new FileNameExtensionFilter(
							"Video File", "avi", "mp4"),
							JFileChooser.FILES_ONLY, "video file"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				mediaPlayerComponent.getMediaPlayer().mute(false);
			}
		});
		
		//Merge video and audio, generate a new file
		item2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			new AddAudio();
			
		}
	});
		menu1.add(item1);
		menu2.add(item2);
	
		
		return menubar;

	}

	//Play video method
	public static void playVideo(File selectedFile) throws IOException {

		if (selectedFile.exists()) {
			stopVideo();
			videoFile = selectedFile;
			 mediaPath = videoFile.getAbsolutePath();

			mediaPlayerComponent.getMediaPlayer().startMedia(mediaPath);
			mediaPlayerComponent.getMediaPlayer().mute(false);
			mediaPlayerComponent.getMediaPlayer().setVolume(
					voice.getValue() * 2);
			playTime = "00:00:00";
			totalTime=Time.setTotalTime((int) mediaPlayerComponent.getMediaPlayer().getLength());
			move.setMaximum((int) ( mediaPlayerComponent).getMediaPlayer().getLength());
			videoTimer.start();
			enableButtons();
			frame.setTitle(mediaPath);
		}

	}
	
	public static void stopVideo(){
		mediaPlayerComponent.getMediaPlayer().stop();
		totalTime = "00:00:00";
		playTime = "00:00:00";
		videoTime.setText(playTime + " / " + totalTime);
		move.setValue(0);
		
	}

	
	//Set the icon of buttons
	private static void setButton(JButton b, String dir) {
		ImageIcon img = new ImageIcon(System.getProperty("user.dir") + "/res/"
				+ dir);
		b.setIcon(img);
		b.setPreferredSize(new Dimension(40, 40));
		b.setBorderPainted(false);
		b.setContentAreaFilled(false);
		b.setFocusPainted(false);
		b.setOpaque(false);
	}
	
	//Active the buttons's feature
	private static void enableButtons() {
		play.setEnabled(true);
		setButton(play, "4.png");
		backward.setEnabled(true);
		forward.setEnabled(true);
		mute.setEnabled(true);
		stop.setEnabled(true);
	}

	

	
	public static  File getVideoFile(){
		File file;
		file=videoFile;
		return file;
		
	}
	public static  String getMediaPath(){
		String dir="";
		dir=mediaPath;
		return dir;
		
	}
	
	public static void disableMenubar(){
		menuBar.getMenu(0).setEnabled(false);
		menuBar.getMenu(1).setEnabled(false);
	}
	
	public static void activeMenubar(){
		menuBar.getMenu(0).setEnabled(true);
		menuBar.getMenu(1).setEnabled(true);
	}
	
	
	
}
