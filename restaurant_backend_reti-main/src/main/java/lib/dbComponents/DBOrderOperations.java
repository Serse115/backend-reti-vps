package lib.dbComponents;

import java.sql.*;
import java.util.ArrayList;
import lib.guiComponents.LowBasicComponents.MyOptionPane;

/******** CLASS FOR ORDERS DATABASE OPERATIONS ********/
public final class DBOrderOperations implements DBOrderOperationsInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;         // Connector object



    /**** Constructors ****/
    public DBOrderOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }



    /**** Methods ****/
    /********************************* Orders operations *********************************/
    // Method to retrieve the orders' data from the database
    public Object[][] getOrdersData(final String sqlQuery) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];                // Returns an empty 2d Objects array if operation failed
        }

        // Using the established connection to retrieve the orders data
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);

            // List to hold the orders data
            ArrayList<Object[]> data = new ArrayList<>();

            // Iterate over the result set and collect the data
            while (rs.next()) {
                int orderNumber = rs.getInt("order_num");
                int tableNum = rs.getInt("table_num");
                String mealCode = rs.getString("meal_code");
                String mealName = rs.getString("name");
                int mealAmount = rs.getInt("amount");
                String specialRequests = rs.getString("special_requests");
                String serviceStatus = rs.getString("service");

                // Create an array of objects representing a row of data
                Object[] rowData = {orderNumber, tableNum, mealCode, mealName, mealAmount, specialRequests, serviceStatus};
                data.add(rowData);
            }

            // Convert the list of data to a 2D array and return it
            Object[][] dataArray = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }
            return dataArray;

        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the data requested is not available!", 0, "Error");
        }

        return new Object[0][0];                    // Returns an empty 2D array if there was an error
    }

    // Method to get only the yet-to-serve orders from the database
    public Object[][] getToServeOrdersData() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];
        }

        // Sql command for the method
        final String sqlCommand = """
                                  SELECT O.*, M.name
                                  FROM order_management AS O, menu AS M
                                  WHERE service = ? AND O.meal_code = M.meal_code""";

        // Using the established connection to retrieve the orders data
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, "N");
            ResultSet rs = pstmt.executeQuery();

            // List to hold the orders data
            ArrayList<Object[]> data = new ArrayList<>();

            // Iterate over the result set and collect the data
            while (rs.next()) {
                int orderNumber = rs.getInt("order_num");
                int tableNum = rs.getInt("table_num");
                String mealCode = rs.getString("meal_code");
                String mealName = rs.getString("name");
                int mealAmount = rs.getInt("amount");
                String specialRequests = rs.getString("special_requests");
                String serviceStatus = rs.getString("service");
                
                // Create an array of objects representing a row of data
                Object[] rowData = {orderNumber, tableNum, mealCode, mealName, mealAmount, specialRequests, serviceStatus};
                data.add(rowData);
            }

            // Convert the list of data to a 2D array and return it
            Object[][] dataArray = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }
            return dataArray;

        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the data requested is not available!", 0, "Error");
        }

        return new Object[0][0];                    // Returns an empty 2D array if there was an error
    }

    // Method to get only the chosen table's orders data from the database
    public Object[][] getTableOrdersData(final String tableNumber) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];
        }

        // Sql command for the method
        final String sqlCommand = "SELECT * FROM order_management WHERE table_num = ?";

        // Using the established connection to retrieve the orders data
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, tableNumber);
            ResultSet rs = pstmt.executeQuery();

            // List to hold the orders data
            ArrayList<Object[]> data = new ArrayList<>();

            // Iterate over the result set and collect the data
            while (rs.next()) {
                int orderNumber = rs.getInt("order_num");
                int tableNum = rs.getInt("table_num");
                String mealCode = rs.getString("meal_code");
                int mealAmount = rs.getInt("amount");
                String specialRequests = rs.getString("special_requests");
                String serviceStatus = rs.getString("service");
                
                // Create an array of objects representing a row of data
                Object[] rowData = {orderNumber, tableNum, mealCode, mealAmount, specialRequests, serviceStatus};
                data.add(rowData);
            }

            // Convert the list of data to a 2D array and return it
            Object[][] dataArray = new Object[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }
            return dataArray;

        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the data requested is not available!", 0, "Error");
        }

        return new Object[0][0];                    // Returns an empty 2D array if there was an error
    }

    // Method to insert a new order into the database
    public void insertNewOrder(final int tableNum, final String mealCode, final int amount, final String specialReq) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        // The placeholders ? are used to avoid sqlInjections
        final String sqlCommand = "INSERT INTO order_management (table_num, meal_code, amount, special_requests) " + 
                                  "VALUES (?, ?, ?, ?)";

        // Using the established connection to insert the new order into the orders table
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setInt(1, tableNum);
            pstmt.setString(2, mealCode);
            pstmt.setInt(3, amount);
            pstmt.setString(4, specialReq);
            pstmt.executeUpdate();
            new MyOptionPane("Order added successfully!", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to retrieve all order numbers for the specified table
    public Integer[] getOrderNumbers(final int tableNum) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Integer[0];
        }

        // Sql command for the method
        final String sqlQuery = "SELECT order_num FROM order_management WHERE table_num = ?";

        // ArrayList for the order numbers
        ArrayList<Integer> numberCodes = new ArrayList<>();

        // Using the established connection to get the order numbers
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
            pstmt.setInt(1, tableNum);

            // Execute the SQL query to retrieve the order numbers
            ResultSet resultSet = pstmt.executeQuery();

            // Process the result set and add codes to the list
            while (resultSet.next()) {
                int tableNumbersList = resultSet.getInt("order_num");
                numberCodes.add(tableNumbersList);
            }

        } catch (SQLException e) {
            new MyOptionPane("Oops, the order numbers could not be acquired!", 0, "Error");
        }

        // Convert the list of reservations to an array and return it
        return numberCodes.toArray(new Integer[0]);
    }

    // Method to update the status of the service for the selected order
    public void updateStatusService(final Integer orderNumber, final String updatedStatus) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "UPDATE order_management SET service = ? WHERE order_num = ?";

        // Using the established connection to update the service status
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, updatedStatus);
            pstmt.setInt(2, orderNumber);
            pstmt.executeUpdate();
            new MyOptionPane("Service status updated successfully", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to retrieve data from a row to enable the dynamic refresh of the orders table (this time with generic values to work it with both int and char)
    public <G> G getDataFromRows(final String columnName, final int tableNumber, final String mealCode, final int amount, Class<G> returnType) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return null;
        }

        // Sql command for the method
        final String sqlCommand = "SELECT " + columnName + " FROM order_management WHERE table_num = ? AND meal_code = ? AND amount = ?";

        // Using the established connection to get the data
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setInt(1, tableNumber);
            pstmt.setString(2, mealCode);
            pstmt.setInt(3, amount);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                if (returnType.equals(Integer.class)) {
                    return returnType.cast(rs.getInt(1));
                }
                if (returnType.equals(Character.class)) {
                    String charValue = rs.getString(1);             // Reads a character from the string result set (the default N for the service status)
                    if (charValue != null && !charValue.isEmpty()) {          // Check if the character obtained is valid
                        return returnType.cast(charValue.charAt(0));
                    }
                }
            }
        }
        catch (SQLException e) {
            new MyOptionPane("Oops, the data could not be retrieved!", 0, "Error");
        }
        return null;
    }
    /********************************* End of orders operations *********************************/
}