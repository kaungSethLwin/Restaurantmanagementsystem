package restaurantsystem;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuFormGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTextField itemName;
    private JTextField itemDetails;
    private JSpinner spinner;
    private JTextField itemPrice;
    private JComboBox<MenuDetails.FoodType> comboBox;
    
    
    public MenuFormGUI(MenuDetails menuDetails, MenuGUI menuGUI) {
        setTitle("Add, Update or Delete Items");
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ASUS\\OneDrive\\Desktop\\projects\\RestaurantManagementSystemProject\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(102, 0, 153));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        

        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblNewLabel = new JLabel("Item Name:");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblNewLabel.setBounds(35, 33, 128, 22);
        contentPane.add(lblNewLabel);

        JLabel lblDetails = new JLabel("Details:");
        lblDetails.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblDetails.setBounds(35, 65, 128, 22);
        contentPane.add(lblDetails);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblQuantity.setBounds(35, 97, 128, 22);
        contentPane.add(lblQuantity);

        JLabel lblPrice = new JLabel("Price:");
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblPrice.setBounds(35, 129, 128, 22);
        contentPane.add(lblPrice);

        JLabel lblType = new JLabel("Type:");
        lblType.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblType.setBounds(35, 171, 128, 22);
        contentPane.add(lblType);

        JLabel lblNewLabel_1 = new JLabel("Create, Update or Delete Items");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 20));
        lblNewLabel_1.setBounds(55, 0, 326, 23);
        contentPane.add(lblNewLabel_1);

        itemName = new JTextField();
        itemName.setBounds(163, 37, 198, 19);
        contentPane.add(itemName);
        itemName.setColumns(10);

        itemDetails = new JTextField();
        itemDetails.setColumns(10);
        itemDetails.setBounds(163, 69, 198, 19);
        contentPane.add(itemDetails);

        itemPrice = new JTextField();
        itemPrice.setColumns(10);
        itemPrice.setBounds(163, 135, 198, 19);
        contentPane.add(itemPrice);

        comboBox = new JComboBox<>();
        for (MenuDetails.FoodType foodType : MenuDetails.FoodType.values()) {
            comboBox.addItem(foodType);
        }
        comboBox.setSelectedItem(MenuDetails.FoodType.UNKNOWN);
        comboBox.setBounds(163, 174, 198, 21);
        contentPane.add(comboBox);

        spinner = new JSpinner();
        spinner.setBounds(163, 101, 198, 20);
        contentPane.add(spinner);
       
        
        if (menuDetails != null) {
            // Pre-fill the form fields with existing menuDetails
            itemName.setText(menuDetails.getFoodName());
            itemDetails.setText(menuDetails.getItemDetails());
            spinner.setValue(menuDetails.getQuantity());
            itemPrice.setText(String.valueOf(menuDetails.getPrice()));
            comboBox.setSelectedItem(menuDetails.getType());
        }

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String itemNameText = itemName.getText();
                String itemDetailsText = itemDetails.getText();
                int quantity = (int) spinner.getValue();
                double price = Double.parseDouble(itemPrice.getText());
                MenuDetails.FoodType foodType = (MenuDetails.FoodType) comboBox.getSelectedItem();

                if (menuDetails != null) {
                    // Update the existing item's data (menuDetails)
                    menuDetails.setFoodName(itemNameText);
                    menuDetails.setItemDetails(itemDetailsText);
                    menuDetails.setQuantity(quantity);
                    menuDetails.setPrice(price);
                    menuDetails.setType(foodType);

                    // Call the updateItem method if needed
                    MenuManager menu = new MenuManager();
                    menu.updateItem(menuDetails);
                } else {
                    // Create a new menuDetails object for creating a new item
                    MenuDetails newMenuDetails = new MenuDetails(quantity, itemNameText, itemDetailsText, itemDetailsText, quantity, price, foodType);

                    // Call the method to add the new item to the menu
                    MenuManager menu = new MenuManager();
                    menu.createItem(newMenuDetails);
                }

                menuGUI.refreshTableData(); // Refresh the table in the parent GUI
                dispose(); // Close the form
            }
        });



        btnConfirm.setBackground(Color.WHITE);
        btnConfirm.setFont(new Font("Tahoma", Font.BOLD, 13));
        btnConfirm.setBounds(189, 237, 119, 25);
        contentPane.add(btnConfirm);

        // Pre-fill form fields if editing an item
        if (menuDetails != null) {
            itemName.setText(menuDetails.getFoodName());
            itemDetails.setText(menuDetails.getItemDetails());
            spinner.setValue(menuDetails.getQuantity());
            itemPrice.setText(String.valueOf(menuDetails.getPrice()));
            comboBox.setSelectedItem(menuDetails.getType());
        }
    }
}
