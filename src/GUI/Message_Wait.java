package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Message_Wait extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	// A pop-up window to show the message while generating video file
	public Message_Wait() {
		setBounds(300, 300, 300, 100);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lblNewLabel = new JLabel("<html>Generating video file"
				+ "<p>   Please wait</html>");
		lblNewLabel.setBounds(0, 11, 284, 62);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblNewLabel);

		setContentPane(contentPane);
		setVisible(true);
	}

}
