package restaurantsystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Operation extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    User user = null;
					Operation frame = new Operation(user);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Operation(User user) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 662, 391);
        setTitle("Choose the Operation");
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);
        
    

        ImageIcon icon = new ImageIcon("C:\\\\Users\\\\ASUS\\\\OneDrive\\\\Desktop\\\\projects\\\\RestaurantManagementSystemProject\\\\RestaurantManegementSystem\\\\resources\\\\images\\\\chef.jpeg"); 
        Image image = icon.getImage();
        setIconImage(image);
        
        Font headerFont = new Font("Arial", Font.BOLD, 23);
        
        JLabel headerLabel = new JLabel("Please Choose the Operation");
        headerLabel.setFont(headerFont); 
        headerLabel.setBounds(200, 20, 400, 40);
        contentPane.add(headerLabel);
        
        // User Manager Icon and Button
        if (user != null && "admin".equalsIgnoreCase(user.getUserRole())) {
            // Only allow admin users to access the User Manager feature
            createMenuOption("User Manager", "C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\user-icon.png", 68, 77, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    UserManagerGUI userManagerGUI = new UserManagerGUI();
                    userManagerGUI.setVisible(true);
                }
            });
        }

        // Report Manager Icon and Button
        createMenuOption("Report Manager", "C:\\\\RestaurantSystem\\\\RestaurantManegementSystem\\\\resources\\\\images\\\\analysis.png", 184, 77, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	ReportViewerGUI report = new ReportViewerGUI();
            	report.setVisible(true);
               
            }
        });

        // Food Menu Icon and Button
        if (user != null && "admin".equalsIgnoreCase(user.getUserRole())) {
            createMenuOption("Food Menu", "C:\\\\RestaurantSystem\\\\RestaurantManegementSystem\\\\resources\\\\images\\menu.png", 300, 77, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    MenuGUI menu = new MenuGUI();
                    menu.refreshTableData(); // Add this line to populate the table initially
                    menu.setVisible(true);
                }
            });
        }

        // Order Icon and Button
        createMenuOption("Order", "C:\\\\RestaurantSystem\\\\RestaurantManegementSystem\\\\resources\\\\images\\\\add.png", 416, 77, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	OrderGUI orderGUI = new OrderGUI();
               orderGUI.setVisible(true);
            }
        });
        
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBackground(Color.WHITE);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add code to log out and redirect to the login frame
                Login loginFrame = new Login();
                loginFrame.setVisible(true);
                dispose(); // Close the current frame
            }
        });
        logoutButton.setBounds(515, 300, 100, 30);
        contentPane.add(logoutButton);
    }
    

    // Helper method to create menu options
    private void createMenuOption(String label, String iconPath, int x, int y, ActionListener actionListener) {
        JLabel optionIcon = new JLabel("");
        ImageIcon originalIcon = new ImageIcon(iconPath);
        Image image = originalIcon.getImage();
        int newWidth = 100;
        int newHeight = (newWidth * image.getHeight(null)) / image.getWidth(null);
        Image resizedImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);
        optionIcon.setIcon(resizedIcon);
        optionIcon.setBounds(x, y, newWidth, newHeight);
        contentPane.add(optionIcon);

        JButton optionButton = new JButton("<html><center>" + label + "</center></html>");
        optionButton.setBackground(Color.WHITE);
        optionButton.addActionListener(actionListener);
        optionButton.setBounds(x, y + newHeight + 8, newWidth, 42); 
        contentPane.add(optionButton);
    }
}
