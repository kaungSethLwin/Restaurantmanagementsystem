package restaurantsystem;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPasswordField userPassword;
    private JTextField userName;
    private String enteredUsername;
    private User currentUser; // Store the current user

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\OneDrive\\Desktop\\projects\\RestaurantManagementSystemProject\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
        setBackground(new Color(153, 153, 204));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 430, 342);
        setSize(379, 275);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(102, 102, 153));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("User Login");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel.setBounds(156, 10, 134, 33);
        contentPane.add(lblNewLabel);

        userName = new JTextField();
        userName.setBounds(142, 70, 148, 21);
        contentPane.add(userName);

        userPassword = new JPasswordField();
        userPassword.setBounds(142, 113, 148, 21);
        contentPane.add(userPassword);

        JButton userLogin = new JButton("Login");
        userLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
        userLogin.setBackground(Color.WHITE);
        userLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String enteredUsername = userName.getText();
                char[] enteredPassword = userPassword.getPassword();
                String enteredPasswordStr = new String(enteredPassword);

                // Use UserManager to validate the login
                UserManager userManager = new UserManager();
                User user = userManager.getUserByUsername(enteredUsername);

                if (user != null) {
                    // User found, check the password
                    if (enteredPasswordStr.equals(user.getPassword())) {
                        currentUser = user; // Set the current user
                        Session.setUser(currentUser);

                        Operation menuFrame = new Operation(currentUser);
                        menuFrame.setVisible(true);

                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid password. Please try again.", "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username not found. Please try again.", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        userLogin.setBounds(205, 152, 85, 21);
        contentPane.add(userLogin);
        
        JLabel lblNewLabel_1 = new JLabel("User Name");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_1.setBounds(28, 70, 90, 21);
        contentPane.add(lblNewLabel_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("User Password");
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel_1_1.setBounds(28, 117, 90, 21);
        contentPane.add(lblNewLabel_1_1);
    }

    public String getEnteredUsername() {
        return enteredUsername;
    }

    public String getUserName() {
        return userName.getText();
    }

 // Add a method to get the current user name
    public String getCurrentUsername() {
        User currentUser = Session.getUser(); // Retrieve the logged-in user from the session

        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }

}
