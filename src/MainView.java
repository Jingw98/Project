#GUI
package projectGUI;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSlider;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JSplitPane;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JInternalFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTextField;

public class MainView {

	private JFrame frame;
	private JButton snapshot, play, stop, forward, backward, mute, edit,
			download, openFile, openFile2, edit2, festival;
	private JSlider move, voice;
	private JLabel time, length;
	private JPanel openView, panelSouth,panelVideo;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		
		
		
		//frame.getContentPane().add(panel1, BorderLayout.SOUTH);
		
		// video

		
		 panelVideo = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				ImageIcon icon = new ImageIcon(System.getProperty("user.dir")
						+ "/res/test.PNG");
				g.drawImage(icon.getImage(), 0, 0, panelVideo.getSize().width,
						panelVideo.getSize().height, panelVideo);
			}
		};
		panelVideo.setBackground(Color.DARK_GRAY);
		// panelVideo.setOpaque(false);
		frame.getContentPane().add(panelVideo, BorderLayout.CENTER);

		// list

		String[] test = { "A            ", "B", "B", "B", "B", "B", "B", "B",
				"B", "B", "B", "B", "B", "B", "B", "a" };
	
		JList list = new JList(test);

		
		JScrollPane scp = new JScrollPane(list);
		frame.getContentPane().add(scp, BorderLayout.EAST);
		
		JPanel panelSouth = new JPanel();
	
		frame.getContentPane().add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BorderLayout());
		panelSouth.setOpaque(false);

		
		
		JPanel panelFestival = new JPanel();
		textField = new JTextField();
		textField.setColumns(10);
		textField.setPreferredSize(new Dimension(500,30));
		festival = new JButton();
		festival.setEnabled(false);
		panelFestival.setLayout(new BorderLayout());
		panelFestival.add(textField);
		panelFestival.add(festival,BorderLayout.AFTER_LINE_ENDS);
		panelFestival.setOpaque(false);
        
		// SLIDER
		JPanel panelFunction = new JPanel();
		
		JSlider slider = new JSlider();
		
		JPanel component = new JPanel();
		component.setLayout(new BorderLayout());
		component.add(panelFestival, BorderLayout.NORTH);
		component.add(slider, BorderLayout.SOUTH);
		component.setOpaque(false);
		panelSouth.add(component, BorderLayout.NORTH);

		
		
		stop = new JButton();
		stop.setEnabled(false);
		panelFunction.add(stop);
		
		backward = new JButton();
		backward.setEnabled(false);
		panelFunction.add(backward);

		play = new JButton();
		play.setEnabled(false);
		panelFunction.add(play);

		forward = new JButton();
		forward.setEnabled(false);
		panelFunction.add(forward);

		
		panelFunction.setOpaque(false);
		panelSouth.add(panelFunction, BorderLayout.WEST);

		setButton(play, "12.png");
		setButton(forward, "1.png");
		setButton(backward, "9.png");
		setButton(stop, "11.png");

		JPanel panelVoice = new JPanel();
		panelVoice.setOpaque(false);
		// panel5.setPreferredSize(new Dimension(290,20));
		mute = new JButton();
		mute.setEnabled(false);
		panelVoice.add(mute);
		setButton(mute, "6.png");
		voice = new JSlider(0, 100, 0); 
		// voice.setOpaque(false);
		voice.setPreferredSize(new Dimension(120, 20));
		voice.setValue(100);
		//voice.setUI(new SliderUI());
		//!!!!!!!!!
		panelVoice.add(voice);
		panelSouth.add(panelVoice, BorderLayout.EAST);
		
		
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

}
