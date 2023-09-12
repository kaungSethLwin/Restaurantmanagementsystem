package restaurantsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import restaurantsystem.MenuDetails.FoodType;

public class MenuSearchApp extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField searchField;
    private JButton searchButton;
    private JTable menuTable;
    private MenuManager menuManager;
    private DefaultTableModel tableModel;
    private JTextField menuSearch;

    public MenuSearchApp(String searchTerm, OrderGUI orderGUI) {
        // Initialize the MenuManager (assuming it's properly configured)
        menuManager = new MenuManager();

        // Set up the JFrame
        setTitle("Menu Search");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        menuSearch = new JTextField(20);
        menuSearch.setText(searchTerm);

        searchField = new JTextField(20);
        searchField.setText(searchTerm);

        searchButton = new JButton("Search");

        // Create a table model and table
        tableModel = new DefaultTableModel() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        menuTable = new JTable(tableModel);
        tableModel.addColumn("Food ID");
        tableModel.addColumn("Food Name");
        tableModel.addColumn("Details");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Price");
        tableModel.addColumn("Food Type");

        // Create a scroll pane for the table
        JScrollPane scrollPane = new JScrollPane(menuTable);

        // Create a panel for search components
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add action listener to the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMenuItems();
            }
        });

        // Add a mouse listener to the table for double-clicking
        menuTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = menuTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Get the selected item details
                        String foodId = (String) menuTable.getValueAt(selectedRow, 0);
                        String foodName = (String) menuTable.getValueAt(selectedRow, 1);
                        String details = (String) menuTable.getValueAt(selectedRow, 2);
                        int quantity = (int) menuTable.getValueAt(selectedRow, 3);
                        double price = Double.parseDouble(((String) menuTable.getValueAt(selectedRow, 4)).replace("$", ""));
                        MenuDetails.FoodType foodType = (FoodType) menuTable.getValueAt(selectedRow, 5);

                        // Create a MenuDetails object with the selected item details
                        MenuDetails selectedItem = new MenuDetails(foodId, foodName, details, quantity, price, foodType);
                        orderGUI.addItemToOrder(selectedItem);
                    }
                }
            }
        });

        // Add components to the JFrame using BorderLayout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(searchPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Make the JFrame visible
        setVisible(true);
    }

    public void searchMenuItems() {
        String searchTerm = searchField.getText().trim();
        if (!searchTerm.isEmpty()) {
            List<MenuDetails> menuItems = menuManager.searchMenuItems(searchTerm);
            displayMenuItems(menuItems);
        } else {
            clearTable();
        }
    }

    private void displayMenuItems(List<MenuDetails> menuItems) {
        clearTable();
        for (MenuDetails item : menuItems) {
            tableModel.addRow(new Object[]{
                    item.getFood_id(),
                    item.getFoodName(),
                    item.getItemDetails(),
                    item.getQuantity(),
                    "$" + item.getPrice(),
                    item.getType()
            });
        }
    }

    private void clearTable() {
        tableModel.setRowCount(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                OrderGUI orderGUI = new OrderGUI(); 
                new MenuSearchApp("", orderGUI);
            }
        });
    }


    public void setSearchTerm(String searchTerm) {
        menuSearch.setText(searchTerm);
    }
}
