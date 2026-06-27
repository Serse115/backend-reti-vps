package lib.guiComponents.LowBasicComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import lib.dbComponents.*;

/******** Selected table panel class ********/
public class SelectedTablePanel extends JPanel implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton insertOrderButton;                            // Button to insert an order
    private final JButton setOrderAsServedButton;                       // Button to serve the order as served
    private final JButton getTheOrdersListButton;                       // Button to get the orders list
    private final String tableNum;                                      // Number of the chosen table
    private final DBConnectionClassInterface connector;                 // Connector object
    private final JTable selectedTablesOrders;                          // Table to show the selected table's current orders


    /**** Constructors ****/
    // Main constructor
    public SelectedTablePanel(final String tableNum, final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setBackground(new Color(238, 238, 238));                  // Setting the panel's background color
        super.setOpaque(true);                                                  // Making the background visible
        super.setLayout(new BorderLayout());                                    // Setting the layout

        // Initializing the connector object
        this.connector = conn;

        // Initializing the orders operations connector
        DBOrderOperationsInterface ordersOperationsConnector = new DBOrderOperations(conn);

        // Setting the table number
        this.tableNum = tableNum;

        // Defining the button to insert the order
        this.insertOrderButton = new JButton("Insert a new order");
        this.insertOrderButton.setFocusable(false);
        this.insertOrderButton.addActionListener(this);

        // Defining the button to set an order as "already served"
        this.setOrderAsServedButton = new JButton("Set as served");
        this.setOrderAsServedButton.setFocusable(false);
        this.setOrderAsServedButton.addActionListener(this);

        // Defining the button to set an order as "already served"
        this.getTheOrdersListButton = new JButton("Get the full orders list");
        this.getTheOrdersListButton.setFocusable(false);
        this.getTheOrdersListButton.addActionListener(this);

        // Setting the panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Adding the buttons to the panel
        buttonPanel.add(this.insertOrderButton);
        buttonPanel.add(this.setOrderAsServedButton);
        buttonPanel.add(this.getTheOrdersListButton);

        // Creating the tableModel field to hold the data
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Order Number", "Table Number", "Meal Code", "Amount", "Special Requests", "Service"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve table's and order's data from the database
        Object[][] tableOrdersData = ordersOperationsConnector.getTableOrdersData(tableNum);

        // Add the order's data to the table model
        for (Object[] rowData : tableOrdersData) {
            tableModel.addRow(rowData);
        }

        // Create the JTable using the table model to show the current state of the selected table's orders
        this.selectedTablesOrders = new JTable(tableModel);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(selectedTablesOrders);

        // Adding the components
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.PAGE_END);
    }

    
    
    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.insertOrderButton) {                                      // If the insert order button is pressed
            new InsertNewOrderFrame(this.tableNum, this.connector, this);         // open a new insertion order frame
        }
        else if (e.getSource() == this.setOrderAsServedButton) {            // If the set as served order button is pressed
            new SelectOrderAsServedFrame(this.tableNum, this.connector, this);    // open a new set as served frame
        }
        else if (e.getSource() == this.getTheOrdersListButton) {            // If the set as served order button is pressed
            GetAndDownloadDataInterface getAndDownloadData = new GetAndDownloadData(this.connector);
            getAndDownloadData.getOrdersThroughTableNum(this.tableNum, "C:/Users/user/Desktop/ordini_tavolo5.txt");
        }
    }

    // Method to return the panel's table mode (for the dynamic refresh)
    public JTable returnParentsTable() {
        return this.selectedTablesOrders;
    }

    // Method to refresh the table data
    public void refreshTableData() {
        // Get the current table model
        DefaultTableModel tableModel = (DefaultTableModel) this.selectedTablesOrders.getModel();

        // Clear all existing rows
        tableModel.setRowCount(0);

        // Create a new DB operations connector to fetch fresh data
        DBOrderOperationsInterface ordersOperationsConnector = new DBOrderOperations(this.connector);

        // Retrieve updated table's and order's data from the database
        Object[][] tableOrdersData = ordersOperationsConnector.getTableOrdersData(this.tableNum);

        // Add the updated order's data to the table model
        for (Object[] rowData : tableOrdersData) {
            tableModel.addRow(rowData);
        }

        // Refresh the table view (though addRow() usually triggers this automatically)
        tableModel.fireTableDataChanged();
        this.selectedTablesOrders.repaint();
    }
}