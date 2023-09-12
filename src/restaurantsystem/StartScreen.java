package restaurantsystem;

import java.awt.Color;


import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class StartScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartScreen frame = new StartScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartScreen() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 715, 479);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(153, 153, 102));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		setLocationRelativeTo(null);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 153, 102));
		panel.setBounds(10, 10, 718, 476);
		contentPane.add(panel);
		
		contentPane.setLayout(new GridLayout());
		panel.setLayout(null);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		contentPane.add(panel, gbc);
		
		JPanel backgroundPanel = new JPanel();
		backgroundPanel.setBackground(new Color(204, 153, 102)); 
		backgroundPanel.setBounds(0, 0, 800, 54); // Set the size and position

		// Create a JLabel for the text
		JLabel lblNewLabel = new JLabel("Food Management System");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Verdana", Font.BOLD, 30));
		lblNewLabel.setForeground(new Color(255, 0, 0));
		lblNewLabel.setBounds(10, 10, 671, 39); // Adjust the position within the panel

		// Add the JLabel to the background panel
		backgroundPanel.add(lblNewLabel);

		
		panel.add(backgroundPanel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setIcon(new ImageIcon("C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
		lblNewLabel_1.setBounds(36, 28, 681, 394);
		panel.add(lblNewLabel_1);
		
		

		
		  // Set up a timer to transition to the login frame after 3 seconds
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the current frame
                dispose();

                // Create and show the login frame
                Login login = new Login();
                login.setVisible(true);
                
                ((Timer) e.getSource()).stop();
                
            }
        });

        // Start the timer
        timer.start();
	}
}
