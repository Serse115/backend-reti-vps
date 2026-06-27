package lib.guiComponents.LowBasicComponents;

import java.awt.*;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBOrderOperations;
import lib.dbComponents.DBOrderOperationsInterface;
import lib.dbComponents.DBDeliveryOperationsInterface;
import lib.dbComponents.DBDeliveryOperations;


/******** Show all orders frame class ********/
public class ShowAllOrdersFrame extends JFrame {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor for the normal orders panel functionality
    public ShowAllOrdersFrame(final String sqlQuery, final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setTitle("All orders");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();     // Get the screen size

        super.setMinimumSize(new Dimension((int) (screen.width  * 0.75), (int) (screen.height  * 0.65)));

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Initializing the orders operations connector
        DBOrderOperationsInterface ordersOperationsConnector = new DBOrderOperations(conn);

        // Create a DefaultTableModel to hold the table data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Order Number", "Table Number", "Meal Code", "Meal Name", "Amount", "Special Requests", "Service"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve all orders data from the database
        Object[][] ordersData = ordersOperationsConnector.getOrdersData(sqlQuery);

        // Add the orders' data to the table model
        for (Object[] rowData : ordersData) {
            tableModel.addRow(rowData);
        }

        // Create the JTable using the table model for the orders
        JTable ordersTable = new JTable(tableModel);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        // Add the scroll pane to the frame's content pane
        super.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Pack the frame to adjust its size
        super.pack();

        // Setting the frame as visible
        super.setVisible(true);
    }

    // Secondary constructor for the delivery orders panel functionality
    public ShowAllOrdersFrame(final DBConnectionClassInterface conn, final int deliveryTag) {

        // Setting the panel
        super.setTitle("All delivery orders");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();     // Get the screen size

        super.setMinimumSize(new Dimension((int) (screen.width  * 0.55), (int) (screen.height  * 0.65)));

        // Initializing the orders operations connector
        DBDeliveryOperationsInterface deliveryOrdersOperationsConnector = new DBDeliveryOperations(conn);

        // Create a DefaultTableModel to hold the table data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Order Code", "Meal Code", "Meal Name", "Quantity", "Order status"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve all orders data from the database
        Object[][] ordersData = deliveryOrdersOperationsConnector.getAllTheDeliveryOrderDataWithStatus();

        // Add the orders' data to the table model
        for (Object[] rowData : ordersData) {
            tableModel.addRow(rowData);
        }

        // Create the JTable using the table model for the orders
        JTable ordersTable = new JTable(tableModel);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        // Add the scroll pane to the frame's content pane
        super.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Pack the frame to adjust its size
        super.pack();

        // Setting the frame as visible
        super.setVisible(true);
    }

    /**** Methods ****/
    // Empty
}