package restaurantsystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnector;
import lombok.Data;


@Data
public class OrderItemsManager {

    public boolean createOrderDetails(OrderItems orderItems) {
        String query = "INSERT INTO order_details (order_id, food_id, food_name, food_price, quantity, isEdited, order_date, editedBy) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, orderItems.getOrderId());
            preparedStatement.setString(2, orderItems.getFoodId());
            preparedStatement.setString(3, orderItems.getFoodName());
            preparedStatement.setDouble(4, orderItems.getFoodPrice());
            preparedStatement.setInt(5, orderItems.getQuantity());
            preparedStatement.setBoolean(6, orderItems.isEdited());
            preparedStatement.setTimestamp(7, new java.sql.Timestamp(orderItems.getOrderDate().getTime()));
            preparedStatement.setString(8, orderItems.getEditedBy());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<OrderItems> getOrderItems(String orderId) {
        List<OrderItems> orderItemsList = new ArrayList<>();
        String query = "SELECT * FROM order_details WHERE order_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        	preparedStatement.setString(1, orderId);
        	
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    OrderItems orderItems = new OrderItems(
                        resultSet.getString("order_id"),
                        resultSet.getString("food_id"),
                        resultSet.getString("food_name"),
                        resultSet.getDouble("food_price"),
                        resultSet.getInt("quantity"),
                        resultSet.getBoolean("isEdited"),
                        resultSet.getDate("order_date"),
                        resultSet.getString("editedBy")
                    );

                    orderItemsList.add(orderItems);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderItemsList;
    }
    
    public boolean updateOrderDetails(OrderItems orderItems) {
        String query = "UPDATE order_details SET food_name = ?, food_price = ?, quantity = ?, isEdited = ?, order_date = ?, editedBy = ? WHERE order_id = ? AND food_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, orderItems.getFoodName());
            preparedStatement.setDouble(2, orderItems.getFoodPrice());
            preparedStatement.setInt(3, orderItems.getQuantity());
            preparedStatement.setBoolean(4, orderItems.isEdited());
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(orderItems.getOrderDate().getTime()));
            preparedStatement.setString(6, orderItems.getEditedBy());
            preparedStatement.setString(7, orderItems.getOrderId());
            preparedStatement.setString(8, orderItems.getFoodId());

            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean orderItemExists(String orderId, String foodId) {
        String query = "SELECT COUNT(*) FROM order_details WHERE order_id = ? AND food_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, orderId);
            preparedStatement.setString(2, foodId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count is greater than 0, the order item exists
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean createOrUpdateOrderDetails(OrderItems orderItems) {
        if (orderItemExists(orderItems.getOrderId(), orderItems.getFoodId())) {
            return updateOrderDetails(orderItems);
        } else {
            return createOrderDetails(orderItems);
        }
    }
    
    public boolean deleteOrderItem(String foodId) {
        String query = "DELETE FROM order_details WHERE food_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodId);
            int affectedRows = preparedStatement.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<OrderItems> getOrderDetailsForToday() {
        List<OrderItems> orderDetailsList = new ArrayList<>();
        String query = "SELECT	 order_id, food_id, food_name, food_price,quantity, isEdited, order_date FROM order_details WHERE DATE(order_date) = CURDATE()";


        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	OrderItems orderDetails = extractOrderDetailsFromResultSet(resultSet);
                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsList;
    }

    public List<OrderItems> getOrderDetailsForYesterday() {
        List<OrderItems> orderDetailsList = new ArrayList<>();
        String query = "SELECT * FROM order_details WHERE DATE(order_date) = DATE_SUB(CURDATE(), INTERVAL 1 DAY)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	OrderItems orderDetails = extractOrderDetailsFromResultSet(resultSet);
                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsList;
    }

    public List<OrderItems> getOrderDetailsForThisWeek() {
        List<OrderItems> orderDetailsList = new ArrayList<>();
        String query = "SELECT * FROM order_details WHERE YEARWEEK(order_date) = YEARWEEK(CURDATE())";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	OrderItems orderDetails = extractOrderDetailsFromResultSet(resultSet);
                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsList;
    }

    public List<OrderItems> getOrderDetailsForThisMonth() {
        List<OrderItems> orderDetailsList = new ArrayList<>();
        String query = "SELECT * FROM order_details WHERE MONTH(order_date) = MONTH(CURDATE()) AND YEAR(order_date) = YEAR(CURDATE())";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	OrderItems orderDetails = extractOrderDetailsFromResultSet(resultSet);
                orderDetailsList.add(orderDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetailsList;
    }

    private OrderItems extractOrderDetailsFromResultSet(ResultSet resultSet) throws SQLException {
    	    String order_id = resultSet.getString("order_id");
    	    String food_id = resultSet.getString("food_id");
    	    String food_name = resultSet.getString("food_name");
    	    double food_price = resultSet.getDouble("food_price");
    	    int quantity = resultSet.getInt("quantity");
    	    boolean isEdited = resultSet.getBoolean("isEdited"); 
    	    Date order_date = resultSet.getDate("order_date");
    	   
    	    return new OrderItems(order_id, food_id, food_name, food_price, quantity, isEdited, order_date,null);
    }

}
