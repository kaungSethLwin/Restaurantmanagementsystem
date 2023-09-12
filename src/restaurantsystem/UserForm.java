package restaurantsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class UserForm extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField userRoleField;
    private JTextField branchField;
    private JTextField addressField;
    private User userToEdit;



    public UserForm(JTable userTable, UserManagerGUI userManagerGUI, User user) {
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\OneDrive\\Desktop\\projects\\Restaurant\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
        getContentPane().setBackground(new Color(102, 0, 153));
        setTitle("User Form");
        setSize(677, 458);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        userToEdit = user;
        
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        // Create and arrange input fields and labels here
        getContentPane().add(panel);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        nameLabel.setBounds(65, 49, 216, 44);
        nameField = new JTextField();
        nameField.setBounds(124, 60, 216, 27);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        usernameLabel.setBounds(178, 87, 216, 44);
        usernameField = new JTextField();
        usernameField.setBounds(268, 98, 216, 27);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        passwordLabel.setBounds(55, 144, 216, 44);
        passwordField = new JPasswordField();
        passwordField.setBounds(139, 155, 216, 27);

        JLabel userRoleLabel = new JLabel("User Role:");
        userRoleLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        userRoleLabel.setBounds(105, 198, 216, 44);
        userRoleField = new JTextField();
        userRoleField.setBounds(190, 209, 216, 27);

        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        branchLabel.setBounds(242, 246, 216, 44);
        branchField = new JTextField();
        branchField.setBounds(312, 252, 216, 27);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        addressLabel.setBounds(124, 289, 216, 44);
        addressField = new JTextField();
        addressField.setBounds(205, 300, 216, 27);
        panel.setLayout(null);

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(userRoleLabel);
        panel.add(userRoleField);
        panel.add(branchLabel);
        panel.add(branchField);
        panel.add(addressLabel);
        panel.add(addressField);
        
        if (userToEdit != null) {
            nameField.setText(userToEdit.getName());
            usernameField.setText(userToEdit.getUsername());
            passwordField.setText(userToEdit.getPassword());
            userRoleField.setText(userToEdit.getUserRole());
            branchField.setText(userToEdit.getBranch());
            addressField.setText(userToEdit.getAddress());
        }

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.setBackground(Color.WHITE);
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String userRole = userRoleField.getText();
                String branch = branchField.getText();
                String address = addressField.getText();

                // Check if userToEdit is not null (indicating an edit)
                if (userToEdit != null) {
                    // Update the original user with the new data
                    userToEdit.setName(name);
                    userToEdit.setUsername(username);
                    userToEdit.setPassword(password);
                    userToEdit.setUserRole(userRole);
                    userToEdit.setBranch(branch);
                    userToEdit.setAddress(address);

                    // Update the corresponding row in the table's model
                    int selectedRow = userTable.getSelectedRow();
                    DefaultTableModel tableModel = (DefaultTableModel) userTable.getModel();
                    tableModel.setValueAt(name, selectedRow, 1);
                    tableModel.setValueAt(username, selectedRow, 2);
                    tableModel.setValueAt(password, selectedRow, 3);
                    tableModel.setValueAt(userRole, selectedRow, 4);
                    tableModel.setValueAt(branch, selectedRow, 5);
                    tableModel.setValueAt(address, selectedRow, 6);

                    // Optionally, update the user in data source (e.g., database)
                    UserManager userManager = new UserManager();
                    userManager.updateUser(userToEdit);
                } else {
                    // Create a new User object
                    User newUser = new User(0, name, username, password, userRole, branch, address); // 0 for userId, as it's auto-generated

                    // Add the new user to the table using UserManager
                    UserManager userManager = new UserManager();
                    userManager.createUser(newUser);

                    // Add the new user to the table's model
                    DefaultTableModel tableModel = (DefaultTableModel) userTable.getModel();
                    tableModel.addRow(new Object[]{
                            newUser.getUserId(),
                            newUser.getName(),
                            newUser.getUsername(),
                            newUser.getPassword(),
                            newUser.getUserRole(),
                            newUser.getBranch(),
                            newUser.getAddress()
                    });
                }

                // Close the form or reset input fields as needed
                // dispose(); // Uncomment this line to close the form
                nameField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                userRoleField.setText("");
                branchField.setText("");
                addressField.setText("");

                setVisible(false);

                // Show the UserManagerGUI frame
                userManagerGUI.setVisible(true);
            }
        });


        
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnConfirm.setBounds(84, 353, 132, 27);
        panel.add(btnConfirm);
        

        //Delete the user
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(Color.WHITE);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int userId = (int) userTable.getValueAt(selectedRow, 0);

                    // Create a UserManager instance
                    UserManager userManager = new UserManager();

                    // Retrieve the user to be deleted from the database
                    User userToDelete = userManager.getUserById(userId);

                    // Check if the user exists before deleting
                    if (userToDelete != null) {
                        // Ask for confirmation
                        int confirmDialog = JOptionPane.showConfirmDialog(UserForm.this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                        
                        if (confirmDialog == JOptionPane.YES_OPTION) {
                            // User confirmed, delete the user from the database
                            userManager.deleteUser(userToDelete);

                            // Remove the selected row from the table
                            DefaultTableModel tableModel = (DefaultTableModel) userTable.getModel();
                            tableModel.removeRow(selectedRow);
                            dispose();
                            
                        }
                    } else {
                        JOptionPane.showMessageDialog(UserForm.this, "User not found.");
                    }
                } else {
                    JOptionPane.showMessageDialog(UserForm.this, "Please select a user to delete.");
                }
            }
        });
        btnDelete.setBounds(352, 353, 132, 27);
        panel.add(btnDelete);


     

        JLabel lblNewLabel = new JLabel("Fill/Delete User Form");
        lblNewLabel.setForeground(new Color(51, 0, 0));
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        lblNewLabel.setBounds(184, 11, 423, 28);
        panel.add(lblNewLabel);

        
    }
}
