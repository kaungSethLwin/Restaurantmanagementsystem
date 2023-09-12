package database;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImportSQLDumps {

    public static void importDumps() {
        String directoryPath = "C:\\RestaurantSystem\\dumps\\Dump20230912";

        // Get a list of files in the directory
        File directory = new File(directoryPath);
        File[] dumpFiles = directory.listFiles();

        if (dumpFiles != null) {
            for (File dumpFile : dumpFiles) {
                if (dumpFile.isFile() && dumpFile.getName().endsWith(".sql")) {
                    String fileName = dumpFile.getName();
                    String dbName = "restaurantmanagementdb";
                    String dbUser = "root";
                    String dbPass = "rooT123!@#";

                    // Construct the command to import the SQL dump file
                    List<String> command = List.of(
                        "mysql",
                        "-u" + dbUser,
                        "-p" + dbPass,
                        dbName,
                        "<",
                        dumpFile.getAbsolutePath()
                    );

                    ProcessBuilder processBuilder = new ProcessBuilder(command);
                    processBuilder.redirectErrorStream(true);

                    try {
                        Process process = processBuilder.start();
                        int exitCode = process.waitFor();

                        if (exitCode == 0) {
                            System.out.println("Imported " + fileName + " successfully");
                        } else {
                            System.out.println("Failed to import " + fileName);
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
