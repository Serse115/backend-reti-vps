package lib.dbComponents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import lib.guiComponents.LowBasicComponents.MyOptionPane;

/******** CLASS FOR MENU BAR DATABASE OPERATIONS ********/
public final class DBMenuBarDeletion implements DBMenuBarDeletionInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;         // Connector object



    /**** Constructors ****/
    public DBMenuBarDeletion(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }



    /**** Methods ****/
    /********************************* Menu bar deletion operations *********************************/
    // Method to delete all reservations
    public void deleteAllReservations() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM reservation_management";

        // Using the established connection to delete all the reservations in the DB
        try {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            new MyOptionPane("Could not delete all the reservation!", 0, "Error");
        }
    }

    // Method to delete all orders
    public void deleteAllOrders() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM order_management";

        // Using the established connection to delete all the orders in the DB
        try {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            new MyOptionPane("Could not delete all the orders!", 0, "Error");
        }
    }

    // Method to delete all waiters data
    public void deleteAllWaiters() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM waiter";

        // Using the established connection to delete all the waiters' data in the DB
        try {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            new MyOptionPane("Could not delete all the waiters data!", 0, "Error");
        }
    }

    // Method to delete all menu data
    public void deleteAllMenu() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM menu";

        // Using the established connection to delete all the menu data in the DB
        try {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            new MyOptionPane("The Could not delete all the menu data!", 0, "Error");
        }
    }

    // Method to delete all the delivery orders from the DB
    public void deleteAllDeliveryOrders() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM delivery_order_details";

        // Using the established connection to delete all the delivery orders
        try {
            assert conn != null;
            PreparedStatement pstmt1 = conn.prepareStatement(sqlCommand);
            pstmt1.executeUpdate();
            new MyOptionPane("All delivery orders deleted successfully!", 1, "Info");
        }
        catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to reset the table's auto incrementing values
    public void resetTable(final String tableName) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "ALTER TABLE " + tableName + " AUTO_INCREMENT = 1";

        // Using the established connection to update the table's auto increment value
        try {
            assert conn != null;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
        } catch (SQLException e) {
            new MyOptionPane("The operation requested is not available!", 0, "Error");
        }
    }
    /********************************* End of menu bar deletion operations *********************************/
}