package restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnector;

public class UserManager {
	public void createUser(User user) {
	    String query = "INSERT INTO user (name, username, password, user_role, branch, address) VALUES (?, ?, ?, ?, ?, ?)";
	    
	    try (Connection connection = DatabaseConnector.connect();
	         PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        preparedStatement.setString(1, user.getName());
	        preparedStatement.setString(2, user.getUsername());
	        preparedStatement.setString(3, user.getPassword());
	        preparedStatement.setString(4, user.getUserRole());
	        preparedStatement.setString(5, user.getBranch());
	        preparedStatement.setString(6, user.getAddress());

	        int affectedRows = preparedStatement.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int userId = generatedKeys.getInt(1);
	                user.setUserId(userId); // Update the user object with the generated ID
	            } else {
	                throw new SQLException("Creating user failed, no ID obtained.");
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


    // Delete User
    public void deleteUser(User user) {
        String query = "DELETE FROM user WHERE user_id=?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update User
    public void updateUser(User user) {
        String query = "UPDATE user SET name = ?, username = ?, password = ?, user_role = ?, branch = ?, address = ? WHERE user_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getUserRole());
            preparedStatement.setString(5, user.getBranch());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setInt(7, user.getUserId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userRole = resultSet.getString("user_role");
                String branch = resultSet.getString("branch");
                String address = resultSet.getString("address");

                User user = new User(userId, name, userName, password, userRole, branch, address);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }

    public User getUserByUsername(String username) {
        String query = "SELECT * FROM user WHERE username = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String userRole = resultSet.getString("user_role");
                String branch = resultSet.getString("branch");
                String address = resultSet.getString("address");

                return new User(userId, name, username, password, userRole, branch, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM user WHERE user_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String userRole = resultSet.getString("user_role");
                String branch = resultSet.getString("branch");
                String address = resultSet.getString("address");

                return new User(userId, name, username, password, userRole, branch, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
