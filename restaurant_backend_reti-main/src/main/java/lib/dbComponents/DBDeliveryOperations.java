package lib.dbComponents;

import lib.guiComponents.LowBasicComponents.MyOptionPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/******** CLASS FOR DATABASE DELIVERY OPERATIONS ********/
public class DBDeliveryOperations implements DBDeliveryOperationsInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;         // Connector object


    /**** Constructors ****/
    public DBDeliveryOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }


    /**** Methods ****/
    //
    public Object[][] getTheDeliveryOrderCodesAndTheirInfo() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];
        }

        // Sql command for the method
        final String sqlCommand = "SELECT * FROM delivery_order_details;";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);        // Prepared statement with the sql command chosen
            ResultSet rs = pstmt.executeQuery();                                // Execute the query and get the result set

            // List to hold the orders data before organizing it in a 2d Array
            ArrayList<Object[]> data = new ArrayList<>();

            // "Order Code", "Order Name", "Address", "CAP", "City", "Email", "Phone", "Order Status"
            while (rs.next()) {
                String orderCode = rs.getString("order_code");
                String orderName = rs.getString("order_name");
                String orderAddress = rs.getString("address");
                String orderCap = rs.getString("cap");
                String orderCity = rs.getString("city");
                String orderEmail = rs.getString("email");
                String orderPhone = rs.getString("phone");
                String orderStatus = rs.getString("order_status");

                // Create an array of objects representing a row of data
                Object[] rowData = {orderCode, orderName, orderAddress, orderCap, orderCity, orderEmail, orderPhone, orderStatus};
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
        return new Object[0][0];
    }

    // Method to retrieve and return all the delivery order data of all order codes plus their status through the all orders info table
    public Object[][] getAllTheDeliveryOrderDataWithStatus() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];
        }

        // Sql command for the method
        final String sqlCommand = """
                SELECT L.order_code, L.meal_code, L.quantity, D.order_status, G.name\r
                FROM delivery_orders_list AS L, delivery_order_details AS D, menu AS G\r
                WHERE L.order_code = D.order_code AND L.meal_code = G.meal_code\r
                ORDER BY L.index_code ASC;""";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);        // Prepared statement with the sql command chosen
            ResultSet rs = pstmt.executeQuery();                                // Execute the query and get the result set

            // List to hold the orders data before organizing it in a 2d Array
            ArrayList<Object[]> data = new ArrayList<>();

            while (rs.next()) {
                String orderCode = rs.getString("order_code");
                String mealCode = rs.getString("meal_code");
                String mealName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                String orderStatus = rs.getString("order_status");

                // Create an array of objects representing a row of data
                Object[] rowData = {orderCode, mealCode, mealName, quantity, orderStatus};
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

    // Method overloading
    // Method to retrieve and return the delivery order data of a specific order code (in the main panel to display the orders)
    public Object[][] getTheDeliveryOrderData(String order_code) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new Object[0][0];
        }

        // Sql command for the method
        final String sqlCommand = """
                SELECT L.order_code, L.meal_code, D.name, L.quantity
                FROM delivery_orders_list AS L, menu AS D
                WHERE L.order_code = ? AND L.meal_code = D.meal_code
                ORDER BY L.index_code ASC;""";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);        // Prepared statement with the sql command chosen
            pstmt.setString(1, order_code);
            ResultSet rs = pstmt.executeQuery();                                // Execute the query and get the result set

            // List to hold the orders data before organizing it in a 2d Array
            ArrayList<Object[]> data = new ArrayList<>();

            while (rs.next()) {
                //String orderCode = rs.getString("order_code");
                String mealCode = rs.getString("meal_code");
                String mealName = rs.getString("name");
                int quantity = rs.getInt("quantity");

                // Create an array of objects representing a row of data
                Object[] rowData = {mealCode, mealName, quantity};
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

    // Method to retrieve all the delivery order codes
    public String[][] getTheDeliveryOrderCodesAndStatus() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new String[0][0];
        }

        // Sql statement
        final String sqlCommand = "SELECT order_code, order_status FROM delivery_order_details;";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            ResultSet rs = pstmt.executeQuery();

            // List to hold the orders data before organizing it in an Array
            ArrayList<String[]> data = new ArrayList<>();

            // Getting the data
            while (rs.next()) {
                String orderCode = rs.getString("order_code");
                String orderStatus = rs.getString("order_status");

                // Create an array of objects representing a row of data
                String[] rowData = {orderCode, orderStatus};
                data.add(rowData);
            }

            // Convert the list of data to a 2D array and return it
            String[][] dataArray = new String[data.size()][];
            for (int i = 0; i < data.size(); i++) {
                dataArray[i] = data.get(i);
            }
            return dataArray;

        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the data requested is not available!", 0, "Error");
        }

        // Return nothing if the process fails
        return new String[0][0];
    }

    // Method to get all the order codes for the order status change
    public String[] getTheDeliveryOrderCodes() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
            return new String[0];
        }

        // Sql statement
        final String sqlCommand = "SELECT order_code FROM delivery_order_details;";

        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            ResultSet rs = pstmt.executeQuery();

            // List to hold the orders data before organizing it in an Array
            ArrayList<String> data = new ArrayList<>();

            // Getting the data
            while (rs.next()) {
                // 2. Prendi la stringa pulita e aggiungila alla lista
                data.add(rs.getString("order_code"));
            }

            // 3. Converti la lista direttamente in un array di String
            return data.toArray(new String[0]);

        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the data requested is not available!", 0, "Error");
        }

        return new String[0];
    }

    // Method to update the status of the service for the selected order
    public void updateStatusService(final String orderCode, final String updatedStatus) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "UPDATE delivery_order_details SET order_status = ? WHERE order_code = ?";

        // Using the established connection to update the service status
        try {
            assert conn != null;
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, updatedStatus);
            pstmt.setString(2, orderCode);
            pstmt.executeUpdate();
            new MyOptionPane("Service status updated successfully", 1, "Info");
        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to delete a delivery order from the database based on its order code
    public void deleteDeliveryOrder(final String deliveryOrderCode) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            new MyOptionPane("Please connect to the database first!", 0, "Error");
        }

        // Sql command for the method
        final String sqlCommand = "DELETE FROM delivery_order_details WHERE order_code = ?";

        // Using the established connection to delete the delivery order
        try {
            assert conn != null;
            PreparedStatement pstmt1 = conn.prepareStatement(sqlCommand);
            pstmt1.setString(1, deliveryOrderCode);
            pstmt1.executeUpdate();
            new MyOptionPane("Delivery order deleted successfully!", 1, "Info");
        } catch (SQLException e) {
            new MyOptionPane("The Database is not connected or the operation requested is not available!", 0, "Error");
        }
    }

    // Method to insert the delivery order data info
    public boolean insertDeliveryOrderDataInfo(final String orderCode, final String orderName, final String address,
                                               final String cap, final String city, final String email, final String phone,
                                               final String orderStatus) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) {
            return false;
        }

        // Sql command for the method
        // The placeholders ? are used to avoid sqlInjections
        final String sqlCommand = """
                INSERT INTO delivery_order_details
                (order_code, order_name, address, cap, city, email, phone, order_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)""";

        // Using the established connection to insert the new orders data into the orders info data table
        try {
            PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
            pstmt.setString(1, orderCode);
            pstmt.setString(2, orderName);
            pstmt.setString(3, address);
            pstmt.setString(4, cap);
            pstmt.setString(5, city);
            pstmt.setString(6, email);
            pstmt.setString(7, phone);
            pstmt.setString(8, orderStatus);
            pstmt.executeUpdate();
            System.out.println("Delivery Order inserito con successo (From DB)");
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }

    }

    // Method to insert the orders list
    public boolean insertDeliveryOrderList(String orderCode, List<Object[]> items) {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn == null) return false;

        // Sql command for the method
        String sql = "INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES (?, ?, ?)";

        // Using the established connection to delete the delivery order
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Disattiviamo l'auto-commit per gestire tutto come un'unica operazione (Transazione)
            conn.setAutoCommit(false);

            for (Object[] item : items) {
                pstmt.setString(1, orderCode);
                pstmt.setString(2, (String) item[0]); // meal_code
                pstmt.setInt(3, (Integer) item[1]);    // quantity
                pstmt.addBatch(); // Aggiunge al lotto
            }

            pstmt.executeBatch(); // Invia tutto al DB in un colpo solo
            conn.commit();        // Conferma l'operazione

            System.out.println("DEBUG: Lista piatti inserita per l'ordine " + orderCode);
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to count the number of orders
    public int countOrders() {

        // Connection status check
        Connection conn = this.connector.returnConnection();
        if (conn != null) {

            // Sql command for the method
            String sqlCommand = "SELECT COUNT(*) AS total FROM delivery_order_details";

            // Il try-with-resources chiude automaticamente pstmt e rs
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCommand);
                 ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total");
                }
            } catch (SQLException e) {
                // Logghiamo l'errore ma senza bloccare l'interfaccia con troppi alert
                System.err.println("Errore nel conteggio ordini: " + e.getMessage());
            }
        }
        return 0;
    }
}





/*
create table delivery_order_details (
order_code char(8) not null primary key,
order_name char(20) not null,
address char(30) not null,
cap char(5) not null,
city char(20) not null,
email char(25) not null,
phone char(10) not null,
order_status char(8) not null default 'awaiting'
);

create table delivery_orders_list (
index_code tinyint unsigned not null primary key auto_increment,
order_code char(8) not null,
meal_code char(4) not null,
quantity tinyint unsigned not null check (quantity > 0),
foreign key (order_code) references delivery_order_details(order_code) on delete cascade
);

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('HKRPK115', 'Mario Rossi', 'Via Roma 10', '20100', 'Milano',  'mario@email.it', '0123456789', 'awaiting');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('HKRPK115', 'ST02', 2); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('HKRPK115', 'SC01', 1); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('SVUAS987', 'Sandro Sterchi', 'Via Culo 22', '36187', 'Padova',  'sandro85@email.it', '3348516504', 'ongoing');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('SVUAS987', 'ST01', 3); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('SVUAS987', 'DE01', 5); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('ADDRD500', 'Fabio Fabigli', 'Via Molise 12', '12122', 'Saponarola',  'faby@email.it', '8745126532', 'ongoing');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADDRD500', 'ST02', 4); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADDRD500', 'DE01', 2); -- 1 Tiramisù
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADDRD500', 'DE02', 3); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('PANED832', 'Sergio Raschioni', 'Via Tonno 15', '88152', 'Poggibonzi',  'sergio@email.it', '1135726142', 'ongoing');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('PANED832', 'ST01', 4); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('PANED832', 'FC01', 2); -- 1 Tiramisù
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('PANED832', 'SC01', 3); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('TARTD832', 'Enrico Feci', 'Via Mulo 15', '99122', 'Poggibonzi',  'enri@email.it', '3135582142', 'awaiting');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('TARTD832', 'ST02', 4); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('TARTD832', 'FC01', 2); -- 1 Tiramisù
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('TARTD832', 'DE02', 3); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('ADRRD400', 'Tonio Navigli', 'Via Lollo 8', '81222', 'Saponarola',  'falerer@email.it', '1230126212', 'ongoing');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADRRD400', 'ST02', 4); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADRRD400', 'DE01', 2); -- 1 Tiramisù
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ADRRD400', 'DE02', 3); -- 1 Tiramisù

-- Inserisci l'ordine
INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
VALUES ('ZARTD832', 'Bald Tack', 'Via Rex 3', '12523', 'Caltagirone',  'talbald@email.it', '2555882142', 'awaiting');

-- Inserisci i piatti per l'ordine numero 1 (che è appena stato creato)
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ZARTD832', 'ST02', 4); -- 2 Carbonare
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ZARTD832', 'FC01', 2); -- 1 Tiramisù
INSERT INTO delivery_orders_list (order_code, meal_code, quantity) VALUES ('ZARTD832', 'DE02', 3); -- 1 Tiramisù



TO DO
CREARE IL TRIGGER DI AGGIORNAMENTO DELL'AVAILABILITY DEI MEALS DOPO CHE VENGONO AGGIUNTI NEGLI ORDERS
PORTARE IL CODICE DI CREAZIONE DELLE TABELLE DEL DELIVERY NELLA CLASSE DI CREAZIONE DEL DB







* */