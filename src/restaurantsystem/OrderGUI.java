package restaurantsystem;

import javax.swing.*;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField orderID;
    private JTextField menuSearchField;
    private JTable table;
    private DefaultTableModel tableModel;
    private Order currentOrder = null;
    private JTextField subTotalField;
    private JTextField totalBillField;
    private PrintingGUI printingGUI;
    private JTextField changes;
    private JLabel dateLabel;
    private int selectedRow = -1;

    public OrderGUI() {
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\chef.jpeg"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 475);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(102, 51, 153));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);

        orderID = new JTextField();
        orderID.setBounds(25, 109, 96, 19);
        contentPane.add(orderID);
        orderID.setColumns(10);
        orderID.setEditable(false);
        contentPane.add(orderID);

        menuSearchField = new JTextField();
        menuSearchField.setEditable(false);
        menuSearchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentOrder == null) {
                    JOptionPane.showMessageDialog(OrderGUI.this, "Please create an order first.", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    String searchTerm = menuSearchField.getText().trim();
                    MenuSearchApp menuSearchApp = new MenuSearchApp(searchTerm, OrderGUI.this);
                    menuSearchApp.setVisible(true);
                }
            }
        });
        menuSearchField.setBounds(327, 109, 96, 19);
        contentPane.add(menuSearchField);
        menuSearchField.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(25, 156, 556, 226);
        contentPane.add(scrollPane);

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Food Name", "Details", "Quantity", "Price", "Food Type"}
        );
        table = new JTable(tableModel);
        scrollPane.setViewportView(table);
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
        table.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                if (column == 2) {
                    try {
                        int newQuantity = Integer.parseInt(table.getValueAt(row, column).toString());
                        MenuDetails selectedItem = currentOrder.getItems().get(row);
                        selectedItem.setQuantity(newQuantity);
                        double newPrice = newQuantity * selectedItem.getPrice();
                        tableModel.setValueAt("$" + String.format("%.2f", newPrice), row, 3);
                        updateTable();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(OrderGUI.this, "Invalid quantity input.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JLabel lblSubTotal = new JLabel("Sub Total");
        lblSubTotal.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblSubTotal.setBounds(617, 143, 106, 13);
        contentPane.add(lblSubTotal);

        subTotalField = new JTextField();
        subTotalField.setEditable(false);
        subTotalField.setColumns(10);
        subTotalField.setBounds(617, 166, 96, 19);
        contentPane.add(subTotalField);

        JLabel lblPaymentByCus = new JLabel("Payment by Customer");
        lblPaymentByCus.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblPaymentByCus.setBounds(617, 196, 150, 13);
        contentPane.add(lblPaymentByCus);

        JTextField payByCus = new JTextField();
        payByCus.setBounds(617, 216, 96, 19);
        contentPane.add(payByCus);
        payByCus.setColumns(10);

        JLabel lblTotalBill = new JLabel("Total Bill");
        lblTotalBill.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblTotalBill.setBounds(617, 244, 106, 13);
        contentPane.add(lblTotalBill);

        totalBillField = new JTextField();
        totalBillField.setEditable(false);
        totalBillField.setColumns(10);
        totalBillField.setBounds(617, 260, 96, 19);
        contentPane.add(totalBillField);

        dateLabel = new JLabel("Date: ");
        dateLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        dateLabel.setBounds(26, 56, 213, 20);
        contentPane.add(dateLabel);
        updateDateLabel();

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double amountPaid = Double.parseDouble(payByCus.getText());
                double subtotal = currentOrder.calculateSubtotal();
                double total = currentOrder.calculateTotal(subtotal);
                double change = amountPaid - total;
                changes.setText("MMK" + String.format("%.2f", change));
                menuSearchField.setEditable(false);
                menuSearchField.setEnabled(false);
                menuSearchField.setText("");

                String currentUsername = getCurrentUsername();

                // Create an instance of OrderManager
                OrderManager orderManager = new OrderManager();

                // Attempt to create the order in the database
                if (orderManager.createOrder(currentOrder, currentUsername)) {
                    
                    OrderItemsManager orderItemsManager = new OrderItemsManager();

                    // Iterate through the items in the currentOrder and save them as order details
                    for (MenuDetails item : currentOrder.getItems()) {
                        OrderItems orderItems = new OrderItems(
                            currentOrder.getOrderId(),
                            item.getFood_id(),
                            item.getFoodName(),
                            item.getPrice(),
                            item.getQuantity(),
                            currentOrder.isEdited(),
                            currentOrder.getDate(),
                            currentUsername
                        );
                        orderItemsManager.createOrderDetails(orderItems);
                    }

                    showPrintingGUI(currentOrder.getOrderId(), currentOrder.getItems(), total, change);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create the order in the database. Please try again or check for errors.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        btnConfirm.setBounds(591, 341, 83, 21);
        contentPane.add(btnConfirm);

        JButton btnNewOrder = new JButton("Create New Bill");
        btnNewOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createNewOrder();
            }
        });

        btnNewOrder.setBounds(10, 407, 114, 21);
        contentPane.add(btnNewOrder);

        JButton btnEdit = new JButton("Edit Bill");
        btnEdit.setBounds(135, 407, 114, 21);
        contentPane.add(btnEdit);

        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuSearchField.setEditable(true);
                menuSearchField.setEnabled(true);
                menuSearchField.setText("");
                currentOrder.setEdited(true); // Add this line
            }
        });

        JButton btnDeletetiems = new JButton("Delete Items");
        btnDeletetiems.setBounds(288, 407, 114, 21);
        contentPane.add(btnDeletetiems);

        btnDeletetiems.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                OrderItemsManager orderItemsManager = new OrderItemsManager();                
                if (selectedRows.length > 0) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int selectedRow = selectedRows[i];
                        // Delete the item from the database using its food ID
                        boolean deleted = orderItemsManager.deleteOrderItem(currentOrder.getItems().get(selectedRow).getFood_id());
                        if (!deleted) {
                            // Handle deletion failure if needed
                            JOptionPane.showMessageDialog(OrderGUI.this, "Failed to delete item from the database.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        currentOrder.removeItem(selectedRow);
                    }
                    updateTable(); // Update the table to reflect the deletion
                } else {
                    JOptionPane.showMessageDialog(OrderGUI.this, "Please select items to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(703, 341, 83, 21);
        contentPane.add(btnDelete);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	
            	OrderManager orderManager = new OrderManager();
            	   if (selectedRow >= 0) {
                       // Ensure a row is selected
                       String orderIdToDelete = currentOrder.getItems().get(selectedRow).getFood_id();
                       
                       // Delete the order
                       if (orderManager.deleteOrder(orderIdToDelete)) {
                           // Update the currentOrder reference after successful deletion
                           currentOrder = null;
                           
                           // Reset the GUI or perform any necessary actions
                           resetGUI(); 
                       } else {
                           JOptionPane.showMessageDialog(OrderGUI.this, "Failed to delete order.", "Error", JOptionPane.ERROR_MESSAGE);
                       }
                   } else {
                       JOptionPane.showMessageDialog(OrderGUI.this, "Please select an order to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                   }
                
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                selectedRow = table.getSelectedRow();
            }
        });

        JLabel lblChanges = new JLabel("Changes");
        lblChanges.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblChanges.setBounds(617, 289, 106, 13);
        contentPane.add(lblChanges);

        changes = new JTextField();
        changes.setBounds(617, 312, 96, 19);
        contentPane.add(changes);
        changes.setEditable(false);
        changes.setColumns(10);

        JLabel lblNewLabel = new JLabel("Search Food");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel.setBounds(250, 109, 83, 19);
        contentPane.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Order ID");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
        lblNewLabel_1.setBounds(25, 86, 96, 13);
        contentPane.add(lblNewLabel_1);

        JLabel lblNewLabel_1_1 = new JLabel("Food Ordering");
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblNewLabel_1_1.setBounds(341, 10, 278, 29);
        contentPane.add(lblNewLabel_1_1);

        JButton btnPrint = new JButton("Print");
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double subtotal = currentOrder.calculateSubtotal();
                double total = currentOrder.calculateTotal(subtotal);
                double change = getChanges();
                showPrintingGUI(currentOrder.getOrderId(), currentOrder.getItems(), total, change);
            }
        });
        btnPrint.setBounds(617, 372, 114, 21);
        contentPane.add(btnPrint);

        JButton btnNewButton = new JButton("Go Back to Main Menu");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Operation operation = new Operation(null);
                operation.setVisible(true);
                dispose(); // Close the current OrderGUI
            }
        });
        btnNewButton.setBounds(617, 17, 159, 21);
        contentPane.add(btnNewButton);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Check if the current order is being edited
                if (currentOrder != null && currentOrder.isEdited()) {
                    String currentUsername = getCurrentUsername();
                    OrderManager orderManager = new OrderManager();
                    OrderItemsManager orderItemsManager = new OrderItemsManager();

                    for (MenuDetails item : currentOrder.getItems()) {
                        OrderItems orderItem = new OrderItems(
                            currentOrder.getOrderId(),
                            item.getFood_id(),
                            item.getFoodName(),
                            item.getPrice(),
                            item.getQuantity(),
                            currentOrder.isEdited(),
                            currentOrder.getDate(),
                            currentUsername
                        );

                        // Check if the order item already exists in the database
                        if (orderItemsManager.orderItemExists(currentOrder.getOrderId(), item.getFood_id())) {
                            // Update the existing order item
                            if (!orderItemsManager.updateOrderDetails(orderItem)) {
                                JOptionPane.showMessageDialog(null, "Failed to update the order item.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        } else {
                            // Insert a new order item
                            if (!orderItemsManager.createOrderDetails(orderItem)) {
                                JOptionPane.showMessageDialog(null, "Failed to create the order item.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }

                    // update the order in the "orders" table
                    if (orderManager.updateOrders(currentOrder)) {
                        // Recalculate and update the GUI (subtotal, total, etc.)
                        updateTable();
                        // Clear edit mode
                        currentOrder.setEdited(false);
                        menuSearchField.setEditable(false);
                        // Optionally, display a message indicating successful update
                        JOptionPane.showMessageDialog(null, "Order updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update the order.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Display a message indicating that the order is not being edited
                    JOptionPane.showMessageDialog(null, "No changes to update.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });


        btnUpdate.setBounds(448, 407, 114, 21);
        contentPane.add(btnUpdate);
    }
       
       
    

    public void addItemToOrder(MenuDetails item) {
        // Add the item to the order and update the table
        if (currentOrder.containsItem(item)) {
            currentOrder.updateQuantity(item, item.getQuantity());
        } else {
            currentOrder.addItem(item);
        }
        updateTable();
    }

    private void updateTable() {
        // Update the table with order details
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        double subtotal = 0.0;

        for (MenuDetails item : currentOrder.getItems()) {
            double itemTotal = item.getQuantity() * item.getPrice();
            subtotal += itemTotal;

            model.addRow(new Object[]{
                    item.getFoodName(),
                    item.getItemDetails(),
                    item.getQuantity(),
                    "$" + String.format("%.2f", itemTotal),
                    item.getType()
            });
        }

        subTotalField.setText("$" + String.format("%.2f", subtotal));
        double total = currentOrder.calculateTotal(subtotal);
        totalBillField.setText("$" + String.format("%.2f", total));
    }

    private List<OrderListener> orderListeners = new ArrayList<>();

    public void addOrderListener(OrderListener listener) {
        orderListeners.add(listener);
    }
    


    private void updateDateLabel() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(new Date(System.currentTimeMillis()));

        dateLabel.setText("Date: " + formattedDate);
    }

    private void showPrintingGUI(String orderID, List<MenuDetails> orderItems, double total, double change) {
        if (currentOrder != null) {
            // Format the change to display it with 2 decimal places
            String formattedChange = String.format("%.2f", change);
            double changes = Double.parseDouble(formattedChange);
            printingGUI = new PrintingGUI(orderID, orderItems, total, changes);
            printingGUI.setVisible(true);
        }
    }

    private void clearTable() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }

    
   
    public double getChanges() {
        String changesText = changes.getText();
        try {
            return Double.parseDouble(changesText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Add the createNewOrder method
    private void createNewOrder() {
        String currentUsername = getCurrentUsername();

        currentOrder = new Order(currentUsername, 0.0, false, new Date(System.currentTimeMillis()), "");

        OrderManager orderManager = new OrderManager();

        while (orderManager.orderExists(currentOrder.getOrderId())) {
            currentOrder.setOrderId(currentOrder.generateOrderId()); // Generate a new unique order ID
        }

        orderID.setText(currentOrder.getOrderId());
        clearTable();
        menuSearchField.setEditable(true);
    }

    // Add a method to get the current username
    private String getCurrentUsername() {
        User currentUser = Session.getUser(); // Retrieve the logged-in user from the session

        if (currentUser != null) {
            return currentUser.getUsername();
        }
        return null;
    }
    
    public void updateOrderItemsList(String orderId, List<OrderItems> orderItems) {
        orderID.setText(orderId);

        // Clear existing data from the table
        tableModel.setRowCount(0);

        for (OrderItems orderItem : orderItems) {
            // Add menu items to the table
            tableModel.addRow(new Object[]{
                orderItem.getFoodName(),
                orderItem.getQuantity(),
                "$" + String.format("%.2f", orderItem.getFoodPrice())
            });
        }
    }
    
    private void resetGUI() {
        // Reset GUI components here, such as clearing tables, updating labels, etc.
        // For example, if you have a JTable displaying order items, you can clear it:
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        
        // Update any labels or other components as needed
        
        // Ensure that the currentOrder reference is null
        currentOrder = null;
    }


    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrderGUI frame = new OrderGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }	
}