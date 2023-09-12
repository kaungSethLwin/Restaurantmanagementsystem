	package restaurantsystem;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable menuTable;
    private DefaultTableModel tableModel;
    private MenuManager menu;

    public MenuGUI() {
    	getContentPane().setBackground(new Color(0, 0, 51));
    	setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
    	setTitle("Restaurant Menu");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        menu = new MenuManager();
        
        
        // Create the table with columns
        String[] columns = {"ID", "Food ID", "Food Name", "Details", "Quantity", "PriceMMK", "Food Type"};
        tableModel = new DefaultTableModel(columns, 0);
        menuTable = new JTable(tableModel);
        
        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(menuTable);
        getContentPane().add(scrollPane, BorderLayout.NORTH);
        
        // Create buttons
        JButton addButton = new JButton("Add New Item");
        JButton editButton = new JButton("Edit Item");
        JButton deleteButton = new JButton("Delete Item");
        
        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH	);
        
        JButton btnNewButton = new JButton("Go Back to MainMenu");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Operation operation = new Operation(null);
        		operation.setVisible(true);
        		dispose();
        	}
        });
        buttonPanel.add(btnNewButton);
        
        // Add action listeners for the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the MenuFormGUI for adding a new item with null details
                MenuFormGUI addItemForm = new MenuFormGUI(null, MenuGUI.this);
                addItemForm.setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow != -1) {
                    menuTable.getValueAt(selectedRow, 0);

                    // Retrieve the selected MenuDetails from the table model
                    MenuDetails selectedMenuDetails = getMenuDetailsFromTable(selectedRow);

                    // Open the MenuFormGUI with pre-filled data for editing
                    MenuFormGUI editItemForm = new MenuFormGUI(selectedMenuDetails, MenuGUI.this);
                    editItemForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(MenuGUI.this, "Select a row to edit.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = menuTable.getSelectedRow();
                if (selectedRow != -1) {
                    int itemId = (int) menuTable.getValueAt(selectedRow, 0); // Get the ID of the selected item

                    // Confirm deletion
                    int option = JOptionPane.showConfirmDialog(MenuGUI.this,
                            "Are you sure you want to delete this item?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        // Delete the item and refresh the table
                        deleteMenuItem(itemId);
                        refreshTableData();
                    }
                } else {
                    JOptionPane.showMessageDialog(MenuGUI.this, "Select a row to delete.");
                }
            }
        });
    }
    
    public void refreshTableData() {
        // Clear the table and reload data from the database
        tableModel.setRowCount(0);
        List<MenuDetails> menuItems = menu.getAllMenuItems();
        for (MenuDetails menuItem : menuItems) {
            tableModel.addRow(new Object[]{
                    menuItem.getId(),
                    menuItem.getFood_id(),
                    menuItem.getFoodName(),
                    menuItem.getItemDetails(),
                    menuItem.getQuantity(),
                    menuItem.getPrice(),
                    menuItem.getType()
            });
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuGUI menuGUI = new MenuGUI();
                menuGUI.setVisible(true);
                menuGUI.refreshTableData();
            }
        });
    }
    
    public void deleteMenuItem(int itemId) {
        // Implement the logic to delete the menu item by its ID
        MenuManager menu = new MenuManager();
        menu.deleteItem(itemId);
    }

    public MenuDetails getMenuDetailsFromTable(int rowIndex) {
        // Get the MenuDetails from the table model based on the row index
        int itemId = (int) menuTable.getValueAt(rowIndex, 0);
        String foodId = (String) menuTable.getValueAt(rowIndex, 1);
        String foodName = (String) menuTable.getValueAt(rowIndex, 2);
        String itemDetails = (String) menuTable.getValueAt(rowIndex, 3);
        int quantity = (int) menuTable.getValueAt(rowIndex, 4);
        double price = (double) menuTable.getValueAt(rowIndex, 5);
        MenuDetails.FoodType foodType = (MenuDetails.FoodType) tableModel.getValueAt(rowIndex, 6);

        return new MenuDetails(itemId, foodId, foodName, itemDetails, quantity, price, foodType);
    }
}
