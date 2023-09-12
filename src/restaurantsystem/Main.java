package restaurantsystem;

import java.sql.Connection;
import java.sql.SQLException;

import database.DatabaseConnector;
import database.ImportSQLDumps;

public class Main {

    public static void main(String[] args) {
        // Database Connection
        DatabaseConnector databaseConnector = new DatabaseConnector();

        try {
            Connection connection = DatabaseConnector.connect();
            if (connection != null) {
                System.out.println("Connected Successfully");

                // Close Connection
                databaseConnector.closeConnection(connection);
                
                
                //IMport Dumps
                ImportSQLDumps.importDumps();
            }
        } catch (SQLException e) {
            System.err.println("Connection Failed");
            e.printStackTrace();
        }
        
        StartScreen startScreen = new StartScreen();
        startScreen.setVisible(true);
    }
}
