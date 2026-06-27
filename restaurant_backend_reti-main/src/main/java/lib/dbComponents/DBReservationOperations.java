package lib.dbComponents;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import lib.guiComponents.LowBasicComponents.MyOptionPane;

/******** CLASS FOR RESERVATION DATABASE OPERATIONS ********/
public final class DBReservationOperations implements DBReservationOperationsInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;         // Connector object


    /**** Constructors ****/
    public DBReservationOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }


    /**** Methods ****/
    /********************************* Reservations operations *********************************/
    // Method to collect the total number of customers
    public int getTotalNumberOfCustomers(LocalDate date) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return 0;
        }

        // SQL command to retrieve the total number of customers
        final String sqlCommand = "SELECT SUM(n_of_seats) AS totalCustomers FROM reservation_management WHERE reservation_date = ?";

        // Using the established connection to get the total number of customers
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("totalCustomers");         // Return the number of total customers
            }

        } catch (SQLException e) {
            new MyOptionPane("Oops, the total number of customers could not be acquired!", 0, "Error");
        }

        return 0;
    }


    // Method to insert a new reservation into the database
    public void insertReservation(final String reservationName, final int nSeats, final String date) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        // The placeholders ? are used to avoid sqlInjections
        final String sqlCommand = "INSERT INTO reservation_management (reservation_holder_name, n_of_seats, reservation_date) " + 
                                  "VALUES (?, ?, ?)";

        // Using the established connection to set the reservation's data and insert it
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, reservationName);
            pstmt.setInt(2, nSeats);
            pstmt.setString(3, date);
            pstmt.executeUpdate();
            new MyOptionPane("Reservation added successfully!", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to delete a reservation from the database based on its reservation name
    public void deleteReservation(final String reservationName) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM reservation_management WHERE reservation_holder_name = ?";

        // Using the established connection to delete the reservation
        try {
            assert conn != null;
            PreparedStatement pstmt1 = conn.prepareStatement(sqlCommand);
            pstmt1.setString(1, reservationName);
            pstmt1.executeUpdate();
            new MyOptionPane("Reservation deleted successfully!", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to retrieve all the reservations names
    public String[] getReservations() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new String[0];
        }

        // ArrayList for the reservations names
        ArrayList<String> reservations = new ArrayList<>();

        // Using the established connection to get the reservation names
        try {
            Statement statement = conn.createStatement();

            // Execute the SQL query to retrieve reservations
            String sqlQuery = "SELECT reservation_holder_name FROM reservation_management";
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Process the result set and add reservations to the list
            while (resultSet.next()) {
                String reservationName = resultSet.getString("reservation_holder_name");
                reservations.add(reservationName);
            }

        } catch (SQLException e) {
            new MyOptionPane("Oops, the reservations could not be acquired!", 0, "Error");
        }

        // Convert the list of reservation names to an array and return it
        return reservations.toArray(new String[0]);
    }

    // Method to collect the total number of reservations
    public int getTotalReservationsNumber(LocalDate date) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return 0;
        }

        // Sql command for the method
        final String sqlCommand = "SELECT COUNT(*) FROM reservation_management WHERE reservation_date = ?";

        // Using the established connection to get the total number of reservations
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException e) {
            new MyOptionPane("Oops, the total number of reservations could not be acquired!", 0, "Error");
        }
        return 0;
    }

    // Method to retrieve all reservations data from the database
    public Object[][] getReservationData() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];            // Returns an empty 2D array if there was an error
        }

        // Sql command for the method
        final String sqlCommand = "SELECT * FROM reservation_management";

        // Using the established connection to retrieve the reservation data
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlCommand);

            // List to hold the reservation data
            ArrayList<Object[]> data = new ArrayList<>();

            // Iterate over the result set and collect the data
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_num");
                String customerName = rs.getString("reservation_holder_name");
                String numOfSeats = rs.getString("n_of_seats");
                String date = rs.getString("reservation_date");
                
                // Create an array of objects representing a row of data
                Object[] rowData = {reservationId, customerName, numOfSeats, date};
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

    // Method to retrieve the current data's reservations from the database
    public Object[][] getReservationOnCurrentData() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];            // Returns an empty 2D array if there was an error
        }

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        // Sql command for the method
        final String sqlCommand = "SELECT reservation_num, reservation_management.reservation_holder_name, n_of_seats, reservation_date, table_num " +
                                  "FROM reservation_management, table_management " + 
                                  "WHERE reservation_date = ? AND reservation_management.reservation_holder_name = table_management.reservation_holder_name";
    
        // Using the established connection to retrieve the reservation data
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setDate(1, sqlDate);
            ResultSet rs = pstmt.executeQuery();
    
            // List to hold the reservation data
            ArrayList<Object[]> data = new ArrayList<>();

            // Iterate over the result set and collect the data
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_num");
                String customerName = rs.getString("reservation_holder_name");
                String time = rs.getString("n_of_seats");
                String date = rs.getString("reservation_date");
                int tableNum = rs.getInt("table_num");
                
                // Create an array of objects representing a row of data
                Object[] rowData = {reservationId, customerName, date, time, tableNum};
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

    // Method to retrieve data from a row to enable the dynamic refresh of the reservations table (adding a reservation)
    public int getDataFromRows(final String columnName, final String tableName, final String reservationName, final Date sqlDate) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return 0;
        }

        // Sql command for the method
        final String sqlCommand = "SELECT " + columnName + " FROM " + tableName + " WHERE reservation_holder_name = ? AND reservation_date = ?";

        // Using the established connection to get the data
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, reservationName);
            pstmt.setDate(2, sqlDate);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException e) {
            new MyOptionPane("Oops, the data could not be retrieved!", 0, "Error");
        }
        return 0;
    }
    /********************************* End of the reservations operations *********************************/
}