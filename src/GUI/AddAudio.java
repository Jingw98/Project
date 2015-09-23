package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;

import main.Player;
import Function.CallBash;
import Function.FileOperation;

public class AddAudio extends JFrame {

	private JPanel contentPane;
private File videoFile;
private File audioFile;
JTextField videoPath,mp3Path;
 JFrame wait;
 File defaultPath;
 JFileChooser save ;

	
	//A pop-up window for "Merge video and audio, generate new file" feature
	public AddAudio() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 200, 450, 150);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		
		save =new JFileChooser();
    	defaultPath= new File(Player.currentDir+"/video.mp4");
    	save.setSelectedFile(defaultPath);
    	
		
		
		JPanel butPanel = new JPanel();
		JButton btnOverlay = new JButton("Overlay");
		JButton btnOverlap = new JButton("Overlap");
		JButton cancel = new JButton("Cancel");
		
		//Cancel button, close the window
		cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	dispose();
            }
            });
		
		//Merge the video and audio file without deleting origin audio of video 
		btnOverlap.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (videoPath.getText().length() == 0
						|| mp3Path.getText().length() == 0) {
					JOptionPane.showMessageDialog(contentPane,
							"File Path Can Not Be Empty!", null,
							JOptionPane.WARNING_MESSAGE);

				} else {
					if (videoFile.exists() && audioFile.exists()) {
						String videoPath = videoFile.getAbsolutePath();
						System.out.println(videoPath);
						String audioPath = audioFile.getAbsolutePath();
						System.out.println(audioPath);
						int returnVal = save.showSaveDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							wait = new Message_Wait();
							String path = save.getSelectedFile().getPath();
							System.out.println(path);
							VideoProcesser vp = new VideoProcesser(
									"ffmpeg -y -i " + videoPath + " -i "
											+ audioPath + " -map 0:v -map 1:a "
											+ path);
							vp.execute();
							setVisible(false);
						}
					}

				}

			}
        });
		
		//Merge the video and audio file with origin audio of video deleted 
		btnOverlay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	if (videoPath.getText().length() == 0
						|| mp3Path.getText().length() == 0) {
					JOptionPane.showMessageDialog(contentPane,
							"File Path Can Not Be Empty!", null,
							JOptionPane.WARNING_MESSAGE);

				} else {
					if (videoFile.exists() && audioFile.exists()) {
						String videoPath = videoFile.getAbsolutePath();
						System.out.println(videoPath);
						String audioPath = audioFile.getAbsolutePath();
						System.out.println(audioPath);

						int returnVal = save.showSaveDialog(null);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							String path = save.getSelectedFile().getPath();
							wait = new Message_Wait();
							VideoProcesser vp = new VideoProcesser(
									"ffmpeg -y -i " + videoPath + " -i "
											+ audioPath
											+ " -filter_complex amix=inputs=2 "
											+ path);
							vp.execute();
							setVisible(false);

						}
					}
				}

			}
        });
		
		butPanel.add(btnOverlay);
		butPanel.add(btnOverlap);
		butPanel.add(cancel, BorderLayout.AFTER_LAST_LINE);
		contentPane.add(butPanel,BorderLayout.SOUTH);
		
		//Choose the video file
		JPanel videoPanel = new JPanel();
		JButton change =new JButton("Change");
		
		 videoPath = new JTextField();
		JLabel vpath=new JLabel(" Video Path:    ");
		if(Player.getMediaPath().length()!=0){
			videoPath.setText(Player.getMediaPath()+"");
			videoFile=Player.getVideoFile();
		}
		
		videoPath.setPreferredSize(new Dimension(200,20));
		videoPanel.add(vpath);
		videoPanel.add(videoPath);
		videoPanel.add(change);
		change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	videoFile = FileOperation.chooseFile(Player.fileSelector,new FileNameExtensionFilter("Video File", "avi", "mp4"),  JFileChooser.FILES_ONLY, "video file");
             videoPath.setText( videoFile.getAbsolutePath());
            }	
            	
            });
		
		
		//Choose the MP3 audio file
		JButton change1 =new JButton("Change");
		JLabel mpath=new JLabel("MP3 File Path: ");
		 mp3Path = new JTextField();
		mp3Path.setPreferredSize(new Dimension(200,20));
		JPanel mp3Panel = new JPanel();
		mp3Panel.add(mpath);
		mp3Panel.add(mp3Path);
		mp3Panel.add(change1);
		change1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	 audioFile = FileOperation.chooseFile(Player.fileSelector,new FileNameExtensionFilter("MP3", "mp3"),  JFileChooser.FILES_ONLY, "audio file");
            	mp3Path.setText( audioFile.getAbsolutePath());
            }	
            	
            });
		

		contentPane.add(mp3Panel,BorderLayout.CENTER);
		contentPane.add(videoPanel,BorderLayout.NORTH);
				
		setContentPane(contentPane);
		setVisible(true);
	}
	
	//SwingWorker used to generate the video file in background
	class VideoProcesser extends SwingWorker<Void, Integer> {
		String command;

		public VideoProcesser(String cmd) {
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
				generateDone();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	 private void generateDone() throws IOException{
	    	wait.setVisible(false);
			wait.dispose();
			JOptionPane.showMessageDialog(contentPane,
					"Combination Finished!", null,
					JOptionPane.INFORMATION_MESSAGE);
	    }
	
}
