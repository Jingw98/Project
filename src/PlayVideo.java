package se206_a3;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PlayVideo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
    private void playVideo() throws IOException{
    	File selectedFile = null;
    	
    	//newwwwwwwwwwwwwwwww
    	FileFilter filter = new FileNameExtensionFilter("Video File", "avi", "mp4"); 
    	fileSelector.setFileFilter(filter);
    	//
    	
    	
    	
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

}
