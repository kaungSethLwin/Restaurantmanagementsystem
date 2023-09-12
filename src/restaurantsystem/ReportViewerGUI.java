package restaurantsystem;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ReportViewerGUI extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
    private JTextField totalOrders;
    private JTextField totalBills;
    private JTable table;
    private DefaultTableModel tableModel;
    private OrderManager orderManager;
    private OrderItemsManager orderItemsManager;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ReportViewerGUI frame = new ReportViewerGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ReportViewerGUI() {
        orderManager = new OrderManager();
        orderItemsManager = new OrderItemsManager();

        setIconImage(
            Toolkit.getDefaultToolkit().getImage("C:\\RestaurantSystem\\RestaurantManegementSystem\\resources\\images\\chef.jpeg")
        );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 872, 482);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(153, 51, 153));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(110, 47, 738, 319);
        contentPane.add(scrollPane);

        // Create the table model and add columns
        List<String> columnNames = new ArrayList<>();
        columnNames.add("Order ID");
        columnNames.add("Food ID");
        columnNames.add("Food Name");
        columnNames.add("Quantity");
        columnNames.add("Price");
        columnNames.add("Date");

        tableModel = new DefaultTableModel(new Object[][] {}, columnNames.toArray());

        // Create the table and set the model
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set the table as the view for the scroll pane
        scrollPane.setViewportView(table);

        JLabel lblNewLabel = new JLabel("Total Orders");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel.setBounds(245, 376, 121, 25);
        contentPane.add(lblNewLabel);

        JLabel lblTotalBills = new JLabel("Total Bills");
        lblTotalBills.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblTotalBills.setBounds(540, 376, 121, 25);
        contentPane.add(lblTotalBills);

        totalOrders = new JTextField();
        totalOrders.setBounds(331, 380, 121, 19);
        contentPane.add(totalOrders);
        totalOrders.setColumns(10);

        totalBills = new JTextField();
        totalBills.setColumns(10);
        totalBills.setBounds(619, 376, 121, 19);
        contentPane.add(totalBills);

        JButton btnPrintButton = new JButton("PrintBill");
        btnPrintButton.setBounds(478, 411, 85, 21);
        contentPane.add(btnPrintButton);


        btnPrintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                printTable("Report Viwer");
            }
        });


        // Create a ButtonGroup for the radio buttons
        ButtonGroup radioButtonGroup = new ButtonGroup();

        // Create and add four radio buttons with improved appearance
        JRadioButton todayRadioButton = new JRadioButton("Today");
        todayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<OrderItems> orderDetailsList = orderItemsManager.getOrderDetailsForToday();
                updateJTable(orderDetailsList);

                List<Order> orderList = orderManager.getOrdersForToday();
                updateTotalOrdersAndBills(orderList);
                printTable("Report for Today");

                System.out.println("Order Details List Size: " + orderDetailsList.size());
                System.out.println("Order List Size: " + orderList.size());
            }
        });
        todayRadioButton.setBounds(10, 50, 80, 24);
        todayRadioButton.setBackground(new Color(204, 0, 51)); // Set the background color
        todayRadioButton.setForeground(Color.WHITE); // Set the text color
        todayRadioButton.setFont(new Font("Tahoma", Font.BOLD, 12)); // Set font and size
        contentPane.add(todayRadioButton);
        radioButtonGroup.add(todayRadioButton);

        JRadioButton yesterdayRadioButton = new JRadioButton("Yesterday");
        yesterdayRadioButton.setBounds(10, 80, 100, 24);
        yesterdayRadioButton.setBackground(new Color(204, 0, 51));
        yesterdayRadioButton.setForeground(Color.WHITE);
        yesterdayRadioButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(yesterdayRadioButton);
        radioButtonGroup.add(yesterdayRadioButton);

        yesterdayRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<OrderItems> orderDetailsList = orderItemsManager.getOrderDetailsForYesterday();
                updateJTable(orderDetailsList);

                List<Order> orderList = orderManager.getOrdersForYesterday();
                updateTotalOrdersAndBills(orderList);
                printTable("Report for Yesterday");

                System.out.println("Order Details List Size: " + orderDetailsList.size());
                System.out.println("Order List Size: " + orderList.size());
            }
        });


        JRadioButton thisWeekRadioButton = new JRadioButton("This Week");
        thisWeekRadioButton.setBounds(10, 110, 100, 24);
        thisWeekRadioButton.setBackground(new Color(204, 0, 51));
        thisWeekRadioButton.setForeground(Color.WHITE);
        thisWeekRadioButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(thisWeekRadioButton);
        radioButtonGroup.add(thisWeekRadioButton);

        thisWeekRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<OrderItems> orderDetailsList = orderItemsManager.getOrderDetailsForThisWeek();
                updateJTable(orderDetailsList);

                List<Order> orderList = orderManager.getOrdersForThisWeek();
                updateTotalOrdersAndBills(orderList);
                printTable("Report for This week");

                System.out.println("Order Details List Size: " + orderDetailsList.size());
                System.out.println("Order List Size: " + orderList.size());
            }
        });


        JRadioButton thisMonthRadioButton = new JRadioButton("This Month");
        thisMonthRadioButton.setBounds(10, 140, 100, 24);
        thisMonthRadioButton.setBackground(new Color(204, 0, 51));
        thisMonthRadioButton.setForeground(Color.WHITE);
        thisMonthRadioButton.setFont(new Font("Tahoma", Font.BOLD, 12));
        contentPane.add(thisMonthRadioButton);
        radioButtonGroup.add(thisMonthRadioButton);
        
        JButton btnNewButton = new JButton("Go Back to MainMenu");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Operation operation = new Operation(null);
        		operation.setVisible(true);
        		dispose();
        	}
        });
        btnNewButton.setBounds(688, 411, 160, 21);
        contentPane.add(btnNewButton);
        
        JLabel lblReportViewing = new JLabel("Report Viewing");
        lblReportViewing.setFont(new Font("Tahoma", Font.BOLD, 15));
        lblReportViewing.setBounds(368, 10, 232, 25);
        contentPane.add(lblReportViewing);

        thisMonthRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<OrderItems> orderDetailsList = orderItemsManager.getOrderDetailsForThisMonth();
                updateJTable(orderDetailsList);

                List<Order> orderList = orderManager.getOrdersForThisMonth();
                updateTotalOrdersAndBills(orderList);
                printTable("Report for This Month");

                System.out.println("Order Details List Size: " + orderDetailsList.size());
                System.out.println("Order List Size: " + orderList.size());
            }
        });

    }

    private void updateJTable(List<OrderItems> orderDetailsList) {
        tableModel.setRowCount(0); // Clear existing data

        for (OrderItems orderDetails : orderDetailsList) {
            Object[] rowData = {
                orderDetails.getOrderId(),
                orderDetails.getFoodId(),
                orderDetails.getFoodName(),
                orderDetails.getQuantity(),
                orderDetails.getFoodPrice(),
                orderDetails.getOrderDate()
            };
            tableModel.addRow(rowData);
        }
    }

    private void updateTotalOrdersAndBills(List<Order> orderList) {
        int totalOrderCount = orderList.size();
        double totalBillAmount = 0.0;

        for (Order order : orderList) {
            totalBillAmount += order.getTotalBill();
        }

        // Round the total bill amount to two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        totalBillAmount = Double.parseDouble(decimalFormat.format(totalBillAmount));

        totalOrders.setText(Integer.toString(totalOrderCount));
        totalBills.setText(Double.toString(totalBillAmount));
    }
    private void printTable(String heading) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();

        if (printerJob.printDialog()) {
            printerJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
                    if (pageIndex > 0) {
                        return NO_SUCH_PAGE;
                    }

                    Graphics2D g2d = (Graphics2D) graphics;
                    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                    // Calculate the total width and height of the table for printing
                    int totalWidth = 0;
                    int totalHeight = 0;
                    for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                        totalWidth += table.getColumnModel().getColumn(i).getWidth();
                    }
                    totalHeight = table.getHeight();

                    double pageWidth = pageFormat.getImageableWidth();
                    double pageHeight = pageFormat.getImageableHeight();

                    double scaleX = pageWidth / totalWidth;
                    double scaleY = pageHeight / totalHeight;
                    double scale = Math.min(scaleX, scaleY);

                    g2d.scale(scale, scale);

                    // Print the heading at the top of the page
                    Font headingFont = new Font("Arial", Font.BOLD, 14);
                    g2d.setFont(headingFont);
                    g2d.drawString(heading, 10, 20);

                    // Move the printing position down for the column headers
                    g2d.translate(0, 30);

                    // Print column headers
                    Font headerFont = new Font("Arial", Font.BOLD, 12);
                    g2d.setFont(headerFont);
                    int headerX = 10;
                    int headerY = 0;
                    for (int i = 0; i < table.getColumnCount(); i++) {
                        String columnName = table.getColumnName(i);
                        g2d.drawString(columnName, headerX, headerY);
                        headerX += table.getColumnModel().getColumn(i).getWidth();
                    }

                    // Move the printing position down for the table data
                    g2d.translate(0, 20);

                    // Print the table data
                    Font dataFont = new Font("Arial", Font.PLAIN, 12);
                    g2d.setFont(dataFont);
                    table.printAll(g2d);
                    return PAGE_EXISTS;
                }
            }, printerJob.defaultPage());

            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    }

