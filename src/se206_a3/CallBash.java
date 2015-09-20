package se206_a3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JFrame;

public class CallBash {
	
	// Execute bash command and return the standard output
	public static BufferedReader callBashBuffer(String cmd) throws IOException{
		ProcessBuilder builder =new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		InputStream stdout = process.getInputStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		return stdoutBuffered;
	}
	
	// Execute bash command and return the process
	public static Process bashProcess(String cmd) throws IOException{
		ProcessBuilder builder =new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		InputStream stdout = process.getInputStream();
		InputStream stderr = process.getErrorStream();
		BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
		return process;
	}
	
	public static void callBashVoid(String cmd) throws IOException{
		ProcessBuilder builder =new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
	}
	
	//newwwwwwwwwwwwwwwwwwwwwwwww
	public static void callBashVoidWait(String cmd) throws IOException, InterruptedException{
		
		ProcessBuilder builder =new ProcessBuilder("/bin/bash", "-c", cmd);
		Process process = builder.start();
		process.waitFor();
		
	}
	
	

}
