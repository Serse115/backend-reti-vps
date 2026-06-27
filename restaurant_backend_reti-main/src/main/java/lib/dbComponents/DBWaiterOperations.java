package lib.dbComponents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import lib.guiComponents.LowBasicComponents.MyOptionPane;

/******** CLASS FOR WAITER DATABASE OPERATIONS ********/
public final class DBWaiterOperations implements DBWaiterOperationsInterface {

    /**** Fields ****/
    private final DBConnectionClassInterface connector;         // Connector object



    /**** Constructors ****/
    public DBWaiterOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }



    /**** Methods ****/
    /********************************* Waiters operations *********************************/
    // Method to retrieve all the waiter codes to assign reservations to them
    public String[] getWaiterCodes() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new String[0];
        }

        // ArrayList for the waiter codes
        ArrayList<String> reservations = new ArrayList<>();

        // Using the established connection to get the waiters codes
        try {
            Statement statement = conn.createStatement();

            // Execute the SQL query to retrieve the waiter codes
            String sqlQuery = "SELECT waiter_code FROM waiter";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Process the result set and add codes to the list
            while (resultSet.next()) {
                String reservationName = resultSet.getString("waiter_code");
                reservations.add(reservationName);
            }

        } catch (SQLException e) {
            new MyOptionPane("Oops, the waiter codes could not be acquired!", 0, "Error");
        }

        // Convert the list of reservations to an array and return it
        return reservations.toArray(new String[0]);
    }

    // Method to assign a waiter to a reservation
    public void assignWaiterToReservation(final String selectedReservation, final String selectedWaiterCode) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "UPDATE table_management SET waiter_code = ? where reservation_holder_name = ?";

        // Using the established connection to assign the chosen waiter to the chosen reservation
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, selectedWaiterCode);
            pstmt.setString(2, selectedReservation);
            pstmt.executeUpdate();
            new MyOptionPane("The waiter has been successfully assigned!", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("Oops! The waiter chosen could not be assigned to the reservation!", 0, "Error");
        }
    }

    // Get the list of table codes
    public String[] getTheTableCodes(final String waiterCode) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new String[0];
        }

        // Sql command for the method
        final String sqlCommand = "SELECT table_num FROM table_management WHERE waiter_code = ?";

        // ArrayList for the table codes
        ArrayList<String> tablesList = new ArrayList<>();

        // Using the established connection to get the table codes
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, waiterCode);
        
            // Execute the SQL query to retrieve the table codes
            ResultSet resultSet = pstmt.executeQuery();
        
            // Process the result set and add codes to the list
            while (resultSet.next()) {
                String tableCode = resultSet.getString("table_num");
                tablesList.add(tableCode);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            new MyOptionPane("Oops, the table codes could not be acquired!", 0, "Error");
        }

        // Convert the list of table codes to an array
        return tablesList.toArray(new String[0]);
    }
    /********************************* End of waiters operations *********************************/
}