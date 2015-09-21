package Function;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class FileOperation {
	public static File chooseFile(JFileChooser fileSelector, FileFilter filter,
			int mode, String fileType) {
		fileSelector.setDialogTitle("Please select a " + fileType);
		fileSelector.setFileFilter(filter);
		fileSelector.setSelectedFile(null);
		fileSelector.setFileSelectionMode(mode);
		int returnVal = fileSelector.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fileSelector.getSelectedFile();
		} else {
			return null;
		}

	}

	public static File setCurrentDir(BufferedReader stdoutBuffered)
			throws IOException {
		stdoutBuffered = CallBash.callBashBuffer("pwd");

		return new File(stdoutBuffered.readLine());

	}

	public static void initialPath(BufferedReader stdoutBuffered)
			throws IOException {
		stdoutBuffered = CallBash
				.callBashBuffer("test -d ./.soundFile; echo $?");

		if (!stdoutBuffered.readLine().equals("0")) {
			CallBash.callBashVoid("mkdir ./.soundFile");

		}

		stdoutBuffered = CallBash
				.callBashBuffer("test -d ./.videoFile; echo $?");
		if (!stdoutBuffered.readLine().equals("0")) {
			CallBash.callBashVoid("mkdir ./.videoFile");
		}
	}

}
