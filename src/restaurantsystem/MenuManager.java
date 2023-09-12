package restaurantsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnector;

public class MenuManager {
    // Create Menu Item
    public void createItem(MenuDetails menuDetails) {
        String query = "INSERT INTO menu (food_id, food_name, details, quantity, price, food_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
          
            int nextFoodId = getNextFoodIdFromDatabase();
            String foodId = menuDetails.generateFoodId(nextFoodId);
            
            MenuDetails.FoodType foodType = menuDetails.getType();
            String foodTypeStr = foodType.toString();

            preparedStatement.setString(1, foodId);
            preparedStatement.setString(2, menuDetails.getFoodName());
            preparedStatement.setString(3, menuDetails.getItemDetails());
            preparedStatement.setLong(4, menuDetails.getQuantity());
            preparedStatement.setDouble(5, menuDetails.getPrice());
            preparedStatement.setString(6, foodTypeStr);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating menu item failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    menuDetails.setId(id); // Update the MenuDetails object with the generated ID
                } else {
                    throw new SQLException("Creating menu item failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Menu Item
    public void deleteItem(int itemId) {
        String query = "DELETE FROM menu WHERE id=?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, itemId); // Use the provided itemId
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Menu Item
    public void updateItem(MenuDetails menuDetails) {
        String query = "UPDATE menu SET food_name=?, details=?, quantity=?, price=?, food_type=? WHERE id=?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            MenuDetails.FoodType foodType = menuDetails.getType();
            String foodTypeStr = foodType.toString();

            preparedStatement.setString(1, menuDetails.getFoodName());
            preparedStatement.setString(2, menuDetails.getItemDetails());
            preparedStatement.setLong(3, menuDetails.getQuantity());
            preparedStatement.setDouble(4, menuDetails.getPrice());
            preparedStatement.setString(5, foodTypeStr);
            preparedStatement.setInt(6, menuDetails.getId()); 

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating menu item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve and return all menu items from the database
    public List<MenuDetails> getAllMenuItems() {
        List<MenuDetails> menuItems = new ArrayList<>();

        String query = "SELECT id, food_id, food_name, details, quantity, price, food_type FROM menu";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String food_id = resultSet.getString("food_id");
                String foodName = resultSet.getString("food_name");
                String itemDetails = resultSet.getString("details");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                String foodTypeStr = resultSet.getString("food_type");
                MenuDetails.FoodType foodType = getMenuDetailsFoodTypeFromString(foodTypeStr);
                
                MenuDetails menuDetails = new MenuDetails(id, food_id, foodName, itemDetails, quantity, price, foodType);
                menuItems.add(menuDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }

    // Food_id duplicate check
    public int getNextFoodIdFromDatabase() {
        int nextFoodId = 1; // Default starting value

        try (Connection connection = DatabaseConnector.connect();
             Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery("SELECT MAX(food_id) FROM menu");

            // Check if there are existing records
            if (resultSet.next()) {
                String highestFoodId = resultSet.getString(1);
                if (highestFoodId != null) {
                    // Extract the numeric part and increment it
                    int numericPart = Integer.parseInt(highestFoodId.substring(1));
                    nextFoodId = numericPart + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextFoodId;
    }

    public static MenuDetails.FoodType getMenuDetailsFoodTypeFromString(String foodTypeStr) {
        switch (foodTypeStr) {
            case "TEA":
                return MenuDetails.FoodType.TEA;
            case "COFFEE":
                return MenuDetails.FoodType.COFFEE;
            case "SHAN_FOOD":
                return MenuDetails.FoodType.SHAN_FOOD;
            case "INDIAN":
                return MenuDetails.FoodType.INDIAN;
            case "BURMESE":
                return MenuDetails.FoodType.BURMESE;
            case "CHINESE":
                return MenuDetails.FoodType.CHINESE;
            case "FAST":
                return MenuDetails.FoodType.FAST;
            case "WATER_AND_DRINKS":
                return MenuDetails.FoodType.WATER_AND_DRINKS;
            default:
                // Handle unknown values or return a default enum value
                return MenuDetails.FoodType.UNKNOWN;
        }
    }
    
    public MenuDetails getMenuDetailsByFoodId(String foodId) {
        MenuDetails menuDetails = null;

        String query = "SELECT id, food_id, food_name, details, quantity, price, food_type FROM menu WHERE food_id = ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, foodId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String food_id = resultSet.getString("food_id");
                String foodName = resultSet.getString("food_name");
                String itemDetails = resultSet.getString("details");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                String foodTypeStr = resultSet.getString("food_type");
                MenuDetails.FoodType foodType = getMenuDetailsFoodTypeFromString(foodTypeStr);

                menuDetails = new MenuDetails(id, food_id, foodName, itemDetails, quantity, price, foodType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuDetails;
    }

    public List<MenuDetails> searchMenuItems(String searchTerm) {
        List<MenuDetails> menuItems = new ArrayList<>();

        // SQL query to search for menu items by name or type
        String query = "SELECT id, food_id, food_name, details, quantity, price, food_type " +
                       "FROM menu " +
                       "WHERE food_name LIKE ? OR food_type LIKE ?";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the search term for both name and type (matching partially)
            String searchPattern = "%" + searchTerm + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String food_id = resultSet.getString("food_id");
                String foodName = resultSet.getString("food_name");
                String itemDetails = resultSet.getString("details");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");

                String foodTypeStr = resultSet.getString("food_type");
                MenuDetails.FoodType foodType = getMenuDetailsFoodTypeFromString(foodTypeStr);

                MenuDetails menuDetails = new MenuDetails(id, food_id, foodName, itemDetails, quantity, price, foodType);
                menuItems.add(menuDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuItems;
    }
}
