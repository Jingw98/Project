package main;
import java.awt.EventQueue;


public class PlayerMain {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					new Player();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
