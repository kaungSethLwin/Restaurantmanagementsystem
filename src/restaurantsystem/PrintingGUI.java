package restaurantsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.*;
import java.util.List;

interface OrderListener {
    void onOrderCreated(Order order);
}

public class PrintingGUI extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderID;
    private List<MenuDetails> orderItems;
    private double totalBill;
    private  double changes;
    private JTextArea receiptTextArea;
    private JButton btnPrint;

    public PrintingGUI(String orderID, List<MenuDetails> orderItems, double totalBill, double changes) {
        this.orderID = orderID;
        this.orderItems = orderItems;
        this.totalBill = totalBill;
        this.changes = changes;

        setTitle("Receipt Preview");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a JTextArea to display receipt content
        receiptTextArea = new JTextArea();
        receiptTextArea.setEditable(false);

        // Create a JScrollPane to allow scrolling if the receipt is long
        JScrollPane scrollPane = new JScrollPane(receiptTextArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(380, 400));

        // Create a "Print" button
        btnPrint = new JButton("Print");
        btnPrint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printContent();
            }
        });

        // Add components to the frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(btnPrint, BorderLayout.SOUTH);
        
        // Populate the receipt content
        populateReceipt();
    }

    private void populateReceipt() {
        StringBuilder receiptContent = new StringBuilder();

        // Header
        receiptContent.append("Order Receipt\n");
        receiptContent.append("=================\n");
        receiptContent.append("Order ID: ").append(orderID).append("\n");
        receiptContent.append("=================\n");

        // Order items and prices
        receiptContent.append("Items:\n");
        for (MenuDetails item : orderItems) {
            receiptContent.append(item.getFoodName()).append(" (Quantity: ").append(item.getQuantity())
                .append(") - $").append(item.getPrice() * item.getQuantity()).append("\n");
        }

        // Separator
        receiptContent.append("=================\n");

        // Total Bill
        receiptContent.append("Total Bill: $").append(totalBill).append("\n");

        // Change
        receiptContent.append("Change: $").append(changes).append("\n"); 
        
        
        receiptContent.append("=================\n");
        receiptContent.append("Developed by Kaung Sett Lwin");

        // Set the content in the JTextArea
        receiptTextArea.setText(receiptContent.toString());
    }

    private void printContent() {
        // Create a PrinterJob
        PrinterJob job = PrinterJob.getPrinterJob();

        PageFormat format = new PageFormat();
        Paper paper = new Paper();

        
        paper.setSize(200, 500); 
        paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());

        format.setPaper(paper);
        format.setOrientation(PageFormat.PORTRAIT);

        // Create a Printable object to define what to print
        Printable printable = new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                // Print the JTextArea content
                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setFont(new Font("Monospaced", Font.PLAIN, 12));

                int x = 50;
                int y = 50;

                String[] lines = receiptTextArea.getText().split("\n");
                for (String line : lines) {
                    g2d.drawString(line, x, y);
                    y += 20;
                }

                return Printable.PAGE_EXISTS;
            }
        };

        // Set the Printable object and PageFormat to the PrinterJob
        job.setPrintable(printable, format);

        // Show a print dialog to the user
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this, "Error printing: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    OrderGUI frame = new OrderGUI();

                    // Add this code to pass order data to PrintingGUI
                    frame.addOrderListener(new OrderListener() {
                        public void onOrderCreated(Order order) {
                            // Create an instance of PrintingGUI and pass order data
                            PrintingGUI printingGUI = new PrintingGUI(order.getOrderId(), order.getItems(),
                                    order.calculateTotal(frame.getChanges()), 0.0);
                            printingGUI.setVisible(true);
                        }
                    });

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}