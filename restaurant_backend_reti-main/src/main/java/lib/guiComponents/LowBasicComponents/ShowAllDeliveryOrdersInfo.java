package lib.guiComponents.LowBasicComponents;

import lib.dbComponents.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class ShowAllDeliveryOrdersInfo extends JFrame {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor for the normal orders panel functionality
    public ShowAllDeliveryOrdersInfo(final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setTitle("All delivery orders with info");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();     // Get the screen size

        super.setMinimumSize(new Dimension((int) (screen.width  * 0.75), (int) (screen.height  * 0.65)));

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Initializing the orders operations connector
        DBDeliveryOperationsInterface ordersOperationsConnector = new DBDeliveryOperations(conn);

        // Create a DefaultTableModel to hold the table data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Order Code", "Order Name", "Address", "CAP", "City", "Email", "Phone", "Order Status"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve all orders data from the database
        Object[][] ordersData = ordersOperationsConnector.getTheDeliveryOrderCodesAndTheirInfo();

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
