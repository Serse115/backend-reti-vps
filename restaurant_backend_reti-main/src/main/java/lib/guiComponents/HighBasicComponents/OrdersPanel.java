package lib.guiComponents.HighBasicComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBOrderOperations;
import lib.dbComponents.DBOrderOperationsInterface;
import lib.guiComponents.LowBasicComponents.OrdersToolBar;

/******** Orders panel class ********/
public class OrdersPanel extends JPanel {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor
    public OrdersPanel(final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setBackground(new Color(238, 238, 238));                  // Setting the panel's background color
        super.setOpaque(true);                                                  // Making the background visible
        super.setLayout(new BorderLayout());

        // Initializing the orders operations connection object
        DBOrderOperationsInterface ordersConnector = new DBOrderOperations(conn);

        // Setting the toolbar for more options
        OrdersToolBar toolBar = new OrdersToolBar(conn);

        // Create a Default table model to hold the table data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Order Number", "Table Number", "Meal Code", "Meal name", "Amount", "Special Requests", "Service"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve orders data from the database
        Object[][] ordersData = ordersConnector.getToServeOrdersData();

        // Add the orders' data to the table model
        for (Object[] rowData : ordersData) {
            tableModel.addRow(rowData);
        }

        // Create the JTable using the table model for the orders
        JTable ordersTable = new JTable(tableModel);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(ordersTable);

        // Add the scroll pane to the panel
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(toolBar, BorderLayout.PAGE_END);
    }


    /**** Methods ****/
    // Empty
}