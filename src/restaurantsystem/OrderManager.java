package restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import database.DatabaseConnector;
import lombok.Data;


@Data
public class OrderManager {
	

	 public boolean createOrder(Order order, String createdByUsername) {
	        String query = "INSERT INTO orders (order_id, total_bill, edited, order_date, created_by) VALUES (?, ?, ?, ?, ?)";

	        try (Connection connection = DatabaseConnector.connect();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setString(1, order.getOrderId());
	            preparedStatement.setDouble(2, order.calculateTotal(order.calculateSubtotal()));
	            preparedStatement.setBoolean(3, order.isEdited());
	            preparedStatement.setTimestamp(4, new java.sql.Timestamp(order.getDate().getTime()));
	            preparedStatement.setString(5, createdByUsername);
	            int affectedRows = preparedStatement.executeUpdate();

	            return affectedRows > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
    
    public boolean orderExists(String orderId) {
        String query = "SELECT COUNT(*) FROM orders WHERE order_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the order ID exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; 
    }
    
    
    public List<Order> getOrderForToday() {
        List<Order> orderItemsList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE DATE(order_date) = CURRENT_DATE";
        System.out.println("SQL Query: " + query);

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String orderId = resultSet.getString("order_id");
                double totalBill = resultSet.getDouble("total_bill");
                boolean edited = resultSet.getBoolean("edited");
                Date orderDate = resultSet.getDate("order_date");
                String createdBy = resultSet.getString("created_by");

                Order order = new Order(orderId, totalBill, edited, orderDate, createdBy);
                orderItemsList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItemsList;
    }
  
    public boolean updateOrders(Order order) {
        String query = "UPDATE orders SET total_bill = ?, edited = ?, order_date = ? WHERE order_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Calculate the total bill and round it to two decimal places
            double roundedTotalBill = Math.round(order.calculateTotal(order.calculateSubtotal()) * 100.0) / 100.0;

            preparedStatement.setDouble(1, roundedTotalBill);
            preparedStatement.setBoolean(2, true);
            preparedStatement.setTimestamp(3, new java.sql.Timestamp(order.getDate().getTime()));
            preparedStatement.setString(4, order.getOrderId()); // Assuming order.getOrderId() is the ID to match

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }

    
    public boolean createOrUpdateOrder(Order order, String createdByUsername) {
        if (orderExists(order.getOrderId())) {
            return updateOrders(order);
        } else {
            return createOrder(order, createdByUsername);
        }
    }
    
    public boolean deleteOrder(String orderId) {
        // First, delete related records in the order_details table
        String deleteOrderDetailsQuery = "DELETE FROM order_details WHERE order_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteOrderDetailsQuery)) {

            preparedStatement.setString(1, orderId);
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                // Related records in order_details were deleted, now delete the order
                String deleteOrderQuery = "DELETE FROM orders WHERE order_id = ?";

                try (PreparedStatement deleteOrderStatement = connection.prepareStatement(deleteOrderQuery)) {
                    deleteOrderStatement.setString(1, orderId);
                    int rowsDeleted = deleteOrderStatement.executeUpdate();

                    return rowsDeleted > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Order> getOrdersForToday() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE DATE(order_date) = CURDATE()";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Order order = extractOrderFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }
    
    public List<Order> getOrdersForYesterday() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE DATE(order_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Order order = extractOrderFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }
    public List<Order> getOrdersForThisWeek() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE YEARWEEK(order_date) = YEARWEEK(CURDATE())";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Order order = extractOrderFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

    public List<Order> getOrdersForThisMonth() {
        List<Order> orderList = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Order order = extractOrderFromResultSet(resultSet);
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderList;
    }

   
    
    private Order extractOrderFromResultSet(ResultSet resultSet) throws SQLException {
        String orderId = resultSet.getString("order_id");
        double totalBill = resultSet.getDouble("total_bill");
        boolean edited = resultSet.getBoolean("edited");
        Date orderDate = resultSet.getDate("order_date");
        String createdBy = resultSet.getString("created_by");

        return new Order(orderId, totalBill, edited, orderDate, createdBy);
    }




}
