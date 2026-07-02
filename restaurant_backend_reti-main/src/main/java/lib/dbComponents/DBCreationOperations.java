package lib.dbComponents;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lib.guiComponents.LowBasicComponents.MyOptionPane;

/******** CLASS FOR DATABASE CREATION OPERATIONS ********/
public final class DBCreationOperations implements DBCreationOperationsInterface {

    /**** Fields ****/
    // Constants
    //private static final String DB_URL = "jdbc:mysql://localhost:3306/restaurant";
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/restaurant";

    // Variables
    private final DBConnectionClassInterface connector;       // Connection field for the first connection (to the MySQL server to create the DB)
    private Connection connection;                            // Connection field for the second connection (to the DB to create the tables, triggers and populate them)


    /**** Constructors ****/
    public DBCreationOperations(final DBConnectionClassInterface connector) {
        this.connector = connector;
    }


    /**** Methods ****/
    /********************************* DB creating operations *********************************/
    /**** It is first needed to connect to the server to create the DB, to then connect to the DB to create, populate and handle the tables ****/
    // Create the DB method
    private void createDB() {

        // Sql command for the method
        final String sqlCommand = "CREATE DATABASE restaurant";

        // Connecting to the server and creating the database
        try {                                                             // The type of connection needed is the one to the server, not to the DB
            Connection conn = this.connector.returnConnection();          // Gets the previously established connection to the server to create the DB in it
            Statement statement = conn.createStatement();                 // Creates the statement
            statement.executeUpdate(sqlCommand);                          // And executes it
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the Database in the chosen mysql server!", 0, "Error");
            }
        }
    }

    /**** Now that the DB has been created, it is necessary to connect to it to create the tables and the triggers ****/
    // Create reservation table method
    private void createReservationTable(final String user, final String password) {

        // Sql string for the method
        final String sqlCommand = """
                CREATE TABLE reservation_management (\r
                reservation_num smallint unsigned auto_increment,\r
                reservation_holder_name varchar(25),\r
                n_of_seats tinyint unsigned not null default 1 check (n_of_seats > 0),\r
                reservation_date date not null,\r
                primary key (reservation_num, reservation_holder_name));""";

        // Connecting to the DB and creating the reservations' table
        // It also needs to instantiate the "connection" field with the value obtained by the connection to the freshly-created DB for future references
        try {                                                                          // This time it connects to the freshly-created DB,
            this.connection = DriverManager.getConnection(DB_URL, user, password);     // Instantiating the "connection" field with the value of the connection to the DB method
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the reservations table in the DB!", 0, "Error");
            }
        }
    }

    // Create order table method
    private void createOrderTable() {

        // Sql string for the method
        final String sqlCommand = """
                CREATE TABLE order_management (\r
                order_num smallint unsigned primary key auto_increment,\r
                table_num tinyint unsigned not null,\r
                meal_code char(4) not null,\r
                amount tinyint unsigned not null default 1 check (amount > 0),\r
                special_requests varchar(115),\r
                service char(1) not null default 'N',\r
                foreign key (table_num) references table_management(table_num)\r
                on delete cascade,\r
                foreign key (meal_code) references menu(meal_code));""";

        // The connection field has already been instantiated with the connection to the DB, so now it can be freely used
        // Creating the orders table
        try {
            Statement statement = this.connection.createStatement();       // Creating the statement with the previously established "connection" field
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the orders table in the DB!", 0, "Error");
            }
        }
    }

    // Create menu table method
    private void createMenuTable() {

        // Sql strings for the method
        final String sqlCommand = """
                CREATE TABLE menu (\r
                position tinyint unsigned auto_increment,\r
                meal_code char(4),\r
                category varchar(30) not null,
                name varchar(45) not null,\r
                price decimal(7,2) not null default 0.0 check (price >= 0.0),\r
                description varchar(100) not null,\r
                availability tinyint unsigned not null default 0 check (availability>= 0),\r
                primary key (position, meal_code));""";

        // Index for the foreign key
        final String sqlCommand1 = "CREATE INDEX order_management_ibfk_2\r\n" +
                                   "ON menu (meal_code);";

        // Creating the menu table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);                           // Executing the first command
            statement.executeUpdate(sqlCommand1);                          // And the second command
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Create waiter table method
    private void createWaiterTable() {

        // Sql strings for the method
        final String sqlCommand = """
                CREATE TABLE waiter (\r
                waiter_code char(4) primary key,\r
                first_name varchar(15) not null,\r
                last_name varchar(25) not null,\r
                age tinyint unsigned not null check (age >= 18 and age <= 68));""";
        
        // Creating the waiter table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the waiter table in the DB!", 0, "Error");
            }
        }
    }

    // Create table management method
    private void createTableManagementTable() {

        // Sql strings for the method
        final String sqlCommand = """
                CREATE TABLE table_management (\r
                table_num tinyint unsigned auto_increment primary key,\r
                reservation_holder_name varchar(20),\r
                waiter_code char(4));""";

        // Creating the index for the foreign key
        final String sqlCommand1 = "CREATE INDEX idx_reservation_holder_name\n" +
                                   "ON reservation_management (reservation_holder_name);";

        // Foreign key for the table_management towards the reservation management table
        final String sqlCommand2 = """
                ALTER TABLE table_management\r
                add constraint res_h_n_fk\r
                foreign key(reservation_holder_name)\r
                references reservation_management(reservation_holder_name)\r
                on delete cascade;""";

        // Foreign key for the table_management towards the waiter table
        final String sqlCommand3 = """
                ALTER TABLE table_management\r
                add constraint waiter_fk\r
                foreign key (waiter_code)\r
                references waiter(waiter_code);""";
        
        // Creating the table management table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
            statement.executeUpdate(sqlCommand1);
            statement.executeUpdate(sqlCommand2);
            statement.executeUpdate(sqlCommand3);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the table management table in the DB!", 0, "Error");
            }
        }
    }

    // Method to create the table for the orders details info table
    private void createDeliveryOrderDetailsInfoTable() {

        // Sql string
        final String sqlCommand = """
                              create table delivery_order_details (
                              order_code char(8) not null primary key,
                              order_name char(20) not null,
                              address char(30) not null,
                              cap char(5) not null,
                              city char(20) not null,
                              email char(50) not null,
                              phone char(10) not null,
                              order_status char(8) not null default 'awaiting'
                              );
                              """;

        // Creating the table for the orders details info table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);                           // Executing the command
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Method to create the table for the orders details info table
    private void createDeliveryOrdersListTable() {

        // Sql string
        final String sqlCommand = """
                            create table delivery_orders_list (
                            index_code tinyint unsigned not null primary key auto_increment,
                            order_code char(8) not null,
                            meal_code char(4) not null,
                            quantity tinyint unsigned not null check (quantity > 0),
                            foreign key (order_code) references delivery_order_details(order_code) on delete cascade
                            );
                            """;

        // Creating the table for the orders details info table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);                           // Executing the command
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Create the OTP data table
    private void createOTPDataTable() {

        // Sql String
        final String sqlCommand = """
                CREATE TABLE otp_codes (
                email VARCHAR(75) PRIMARY KEY,
                otp_code CHAR(6) NOT NULL,
                expiry_time DATETIME NOT NULL
                );""";

        // Creating the table for the orders details info table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);                           // Executing the command
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Create triggers method
    // Trigger 1
    private void createTrigger1() {

        // Sql strings for the method
        // Trigger to update the menu's availability
        final String sqlCommand = """
                create trigger update_menu\r
                after insert on order_management\r
                for each row\r
                begin\r
                 update menu \r
                 set availability = availability - NEW.amount\r
                 where meal_code = NEW.meal_code;\r
                end;""";

        // Creating the trigger
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the trigger 1 in the DB!", 0, "Error");
            }
        }
    }

    // Trigger 2
    private void createTrigger2() {

        // Sql strings for the method
        // Trigger to update the list of tables
        final String sqlCommand1 = """
                create trigger reservation_management_insert\r
                after insert on reservation_management\r
                for each row\r
                begin\r
                 if NEW.reservation_date = CURDATE() then\r
                  insert into table_management(reservation_holder_name)\r
                  values (NEW.reservation_holder_name);\r
                 end if;\r
                end;""";

        // Creating the trigger
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand1);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the trigger 2 in the DB!", 0, "Error");
            }
        }
    }

    // Trigger 3
    private void createTrigger3() {

        // Sql strings for the method
        // Trigger to update the menu's availability after the insertion in the delivery orders list table
        final String sqlCommand = """
                create trigger update_menu1\r
                after insert on delivery_orders_list\r
                for each row\r
                begin\r
                 update menu \r
                 set availability = availability - NEW.quantity\r
                 where meal_code = NEW.meal_code;\r
                end;""";

        // Creating the trigger
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not create the trigger 3 in the DB!", 0, "Error");
            }
        }
    }

    // Populating the tables methods
    // Populating the waiter table
    private void populateWaiterTable() {

        // Sql string for the method
        final String sqlCommand = """
                INSERT INTO waiter (waiter_code, first_name, last_name, age)\r
                VALUES ('MS12', 'Orazio', 'Grinzosi', 31),\r
                 ('MT21', 'Marco', 'Talebano', 21),\r
                 ('NT14', 'Natalia', 'Natale', 25);""";

        // Populating the table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not populate the waiter table in the DB!", 0, "Error");
            }
        }
    }

    // Populating the menu table
    private void populateMenuTable() {

        // Sql string for the method
        final String sqlCommand = """
                INSERT INTO menu (meal_code, category, name, price, description, availability)\r
                VALUES ('ST01', 'Primi', 'Spaghetti al baioioiab', 12.99, 'spaghetti al saas', 20),\r
                 ('ST02', 'Antipasti', 'Involtini al siis', 10.99, 'involtini con carne di siis', 20),\r
                 ('FC01', 'Primi', 'Carbonara al carbone', 12.99, 'carbonara cotta al carbone', 20),\r
                 ('SC01', 'Secondi', 'Arrosto di soos', 15.99, 'carne di soos arrosto', 20),\r
                 ('DR01', 'Bevande', 'Acqua lezza', 1.99, 'acqua lezza direttamente dalle fonti sudicie', 20),\r
                 ('DE01', 'Dolci', 'Torta al leel', 9.99, 'torta al leel con zuuz', 20),\r
                 ('DE02', 'Dolci', 'Crema jamaicana', 11.99, 'crema jamaicana con paap', 20);""";

        // Populating the table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not populate the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Method to populate the delivery orders data info table
    private void populateTheDeliveryOrderDetailInfoTable() {

        // Sql string for the method
        final String sqlCommand = """
                INSERT INTO delivery_order_details (order_code, order_name, address, cap, city, email, phone, order_status)
                VALUES ('HKRPK115', 'Mario Rossi', 'Via Roma 10', '20100', 'Milano', 'mario@email.it', '0123456789', 'awaiting'),
                 ('SVUAS987', 'Sandro Sterchi', 'Via Culo 22', '36187', 'Padova', 'sandro85@email.it', '3348516504', 'ongoing'),
                 ('ADDRD500', 'Fabio Fabigli', 'Via Molise 12', '12122', 'Saponarola', 'faby@email.it', '8745126532', 'ongoing'),
                 ('PANED832', 'Sergio Raschioni', 'Via Tonno 15', '88152', 'Poggibonzi', 'sergio@email.it', '1135726142', 'ongoing'),
                 ('TARTD832', 'Enrico Feci', 'Via Mulo 15', '99122', 'Poggibonzi', 'enri@email.it', '3135582142', 'awaiting');
                 """;

        // Populating the table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not populate the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Method to populate the delivery orders list table
    private void populateTheDeliveryOrderListTable() {

        // Sql string for the method
        final String sqlCommand = """
                INSERT INTO delivery_orders_list (order_code, meal_code, quantity)\r
                VALUES ('HKRPK115', 'ST02', 2),
                 ('HKRPK115', 'SC01', 1),
                 ('SVUAS987', 'ST01', 3),
                 ('SVUAS987', 'DE01', 5),
                 ('ADDRD500', 'ST02', 4),
                 ('ADDRD500', 'DE01', 2),
                 ('ADDRD500', 'DE02', 3),
                 ('PANED832', 'ST01', 4),
                 ('PANED832', 'FC01', 2),
                 ('PANED832', 'SC01', 3),
                 ('TARTD832', 'ST02', 4),
                 ('TARTD832', 'FC01', 2),
                 ('TARTD832', 'DE02', 3);
                 """;

        // Populating the table
        try {
            Statement statement = this.connection.createStatement();       // Using the usual "connection" field to create the statement
            statement.executeUpdate(sqlCommand);
            new MyOptionPane("Database created successfully!", 1, "Info");
        }
        catch (SQLException e) {
            if (!GraphicsEnvironment.isHeadless()) {
                new MyOptionPane("Could not populate the menu table in the DB!", 0, "Error");
            }
        }
    }

    // Only public method, used for the entire creation of the DB
    // Full DB creation method
    public void fullDBCreation(final String user, final String password) {

        // Creating the DB
        this.createDB();

        // Switching the connection from the server to the freshly-created DB and creating the tables
        this.createReservationTable(user, password);      // Connecting to the freshly-created DB and creating the reservations table
        this.createWaiterTable();                         // Creating the waiters table
        this.createTableManagementTable();                // Creating the table management table
        this.createMenuTable();                           // Creating the menu table
        this.createOrderTable();                          // Creating the orders table
        this.createDeliveryOrderDetailsInfoTable();       // Create the delivery order details info table
        this.createDeliveryOrdersListTable();             // Create the delivery orders list table
        this.createOTPDataTable();                        // Create the OTP data table for the OTP verification

        // Adding the triggers
        this.createTrigger1();                            // Creating the trigger to update the menu's availability
        this.createTrigger2();                            // Creating the trigger to add to the table management table the new reservations with current date
        this.createTrigger3();                            // Creating the trigger to update the menu's availability after the delivery order

        // Populating the tables
        this.populateWaiterTable();                       // Populating the waiter table with default values
        this.populateMenuTable();                         // Populating the menu table with default values
        this.populateTheDeliveryOrderDetailInfoTable();   // Populating the delivery orders details info table
        this.populateTheDeliveryOrderListTable();         // Populating the delivery orders list table
    }
}