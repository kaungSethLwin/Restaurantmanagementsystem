package restaurantsystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserManagerGUI extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTable userTable;
	protected User user;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserManagerGUI frame = new UserManagerGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JTable getUserTable() {
        return userTable;
    }

    public UserManagerGUI() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\OneDrive\\Desktop\\projects\\Restaurant\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
    	
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 725, 481);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(0, 0, 51));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);

        // Create the table model with column names
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Name", "User Name", "Password", "Role", "Branch", "Address"}
        );

        // Create the table and associate it with the tableModel
        userTable = new JTable(tableModel);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBounds(55, 51, 578, 283);
        contentPane.add(scrollPane);

        // Make the entire table non-editable
        userTable.setDefaultEditor(Object.class, null);

        // Fetch user data and populate the table
        UserManager userManager = new UserManager();
        List<User> userList = userManager.getAllUsers();
        for (User user : userList) {
            tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getName(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getUserRole(),
                    user.getBranch(),
                    user.getAddress()
            });
        }

        JLabel lblNewLabel = new JLabel("User Details ");
        lblNewLabel.setForeground(new Color(192, 192, 192));
        lblNewLabel.setBounds(273, 10, 234, 31);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
        contentPane.add(lblNewLabel);

        JButton addUserBtn = new JButton("Add new user");
        addUserBtn.setBackground(Color.WHITE);
        addUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	UserForm userForm = new UserForm(userTable, UserManagerGUI.this, user);

                userForm.setVisible(true);
            }
        });
        addUserBtn.setBounds(393, 365, 114, 21);
        contentPane.add(addUserBtn);

        JButton editUserBtn = new JButton("Edit User");
        editUserBtn.setBackground(Color.WHITE);
        editUserBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    
                    int userId = (int) userTable.getValueAt(selectedRow, 0);
                    String name = (String) userTable.getValueAt(selectedRow, 1);
                    String username = (String) userTable.getValueAt(selectedRow, 2);
                    String password = (String) userTable.getValueAt(selectedRow, 3);
                    String userRole = (String) userTable.getValueAt(selectedRow, 4);
                    String branch = (String) userTable.getValueAt(selectedRow, 5);
                    String address = (String) userTable.getValueAt(selectedRow, 6);

                    // Create a User object with the selected user's data
                    User selectedUser = new User(userId, name, username, password, userRole, branch, address);

                    // Open the UserForm for editing with the selected user's data
                    UserForm userForm = new UserForm(userTable, UserManagerGUI.this, selectedUser);
                    userForm.setVisible(true);
                }
            }
        });
        editUserBtn.setBounds(519, 365, 114, 21);
        contentPane.add(editUserBtn);
        
        JButton btnNewButton = new JButton("Go Back to Main Menu");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Operation operation = new Operation(null);
        		operation.setVisible(true);
        	}
        });
        btnNewButton.setBounds(467, 396, 147, 21);
        contentPane.add(btnNewButton);
     
        userTable.getSelectionModel().addListSelectionListener(e -> {
            
            editUserBtn.setEnabled(userTable.getSelectedRow() != -1);
        });

    }
}
