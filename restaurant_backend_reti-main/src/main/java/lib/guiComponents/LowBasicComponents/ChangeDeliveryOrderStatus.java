package lib.guiComponents.LowBasicComponents;

import lib.dbComponents.*;
import lib.guiComponents.HighBasicComponents.DeliveryServicePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/******** Select order as served frame class ********/
public class ChangeDeliveryOrderStatus extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton confirmButton;                                    // Button to confirm the operation and close
    private final JComboBox<String> ordersComboBox;                        // Combo box for orders selection
    private final JRadioButton orderReadyButton;                            // RadioButton to set as already served
    private final JRadioButton orderAwaitingButton;                         // RadioButton to set as not served yet
    private final JRadioButton orderOngoingButton;                          // RadioButton to set as not served yet
    private final DBDeliveryOperationsInterface ordersOperationsConnector;  // Orders operations connector object
    private final DeliveryServicePanel selectedTablePanelReference;         // Reference to the selected table panel for the refresh


    /**** Constructors ****/
    // Main constructor
    public ChangeDeliveryOrderStatus(final DBConnectionClassInterface conn, final DeliveryServicePanel selectedTablePanel) {

        // Setting the frame
        super("Select the delivery order to set as served");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setResizable(false);

        // Initializing the orders operations connector object
        this.ordersOperationsConnector = new DBDeliveryOperations(conn);

        // Initializing the selected table panel reference
        this.selectedTablePanelReference = selectedTablePanel;

        // Setting the reference to the table number of the chosen table
        //int tableNum1 = Integer.parseInt(tableNum);

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(400, 150));        // Set the preferred size of the main panel

        // Setting the radio button as ready to be served
        this.orderReadyButton = new JRadioButton("ready");
        this.orderReadyButton.setFocusable(false);
        this.orderReadyButton.addActionListener(this);

        // Setting the radio button as awaiting to be prepared
        this.orderAwaitingButton = new JRadioButton("awaiting");
        this.orderAwaitingButton.setFocusable(false);
        this.orderAwaitingButton.addActionListener(this);

        // Setting the radio button as ongoing in preparation
        this.orderOngoingButton = new JRadioButton("ongoing");
        this.orderOngoingButton.setFocusable(false);
        this.orderOngoingButton.addActionListener(this);

        // Defining the insertion tage to tell the user what to choose
        JLabel insertionTag = new JLabel("Service status:");

        // Create form panel
        JPanel orderPanel = new JPanel(new FlowLayout());

        // Retrieve the delivery order codes available from the database
        String[] ordersList = this.ordersOperationsConnector.getTheDeliveryOrderCodes();

        // Create orders combo box
        this.ordersComboBox = new JComboBox<>(ordersList);
        orderPanel.add(this.ordersComboBox);

        // Create form panel
        JPanel formPanel = new JPanel(new FlowLayout());

        // Add labels and fields to the form panel
        formPanel.add(insertionTag);
        formPanel.add(this.orderReadyButton);
        formPanel.add(this.orderAwaitingButton);
        formPanel.add(this.orderOngoingButton);

        // Setting the confirmation button
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.setFocusable(false);
        this.confirmButton.addActionListener(this);

        // Adding the components to the frame
        mainPanel.add(orderPanel, BorderLayout.PAGE_START);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(this.confirmButton, BorderLayout.PAGE_END);
        super.setContentPane(mainPanel);

        // Pack the frame to adjust its size
        super.pack();

        // Setting the frame as visible
        super.setVisible(true);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.confirmButton) {

            String serviceStatus;                // Service status to choose
            String orderCodeChosen;              // Order number chosen

            // Obtaining the data
            try {
                orderCodeChosen = (String) ordersComboBox.getSelectedItem();     // Getting the chosen order number

                // Updating the order's status
                if (orderCodeChosen != null) {
                    if (this.orderAwaitingButton.isSelected()) {                                                // If the already served button is pressed
                        serviceStatus = this.orderAwaitingButton.getText();                                     // use the text of the already served button (Y)
                        this.ordersOperationsConnector.updateStatusService(orderCodeChosen, serviceStatus);   // to update the service status of that order

                    }
                    else if (this.orderOngoingButton.isSelected()) {                                            // If the not yet served button is pressed
                        serviceStatus = this.orderOngoingButton.getText();                                      // use the text of the already served button (N)
                        this.ordersOperationsConnector.updateStatusService(orderCodeChosen, serviceStatus);   // to update the service status of that order
                    }
                    else if (this.orderReadyButton.isSelected()) {                                            // If the not yet served button is pressed
                        serviceStatus = this.orderReadyButton.getText();                                      // use the text of the already served button (N)
                        this.ordersOperationsConnector.updateStatusService(orderCodeChosen, serviceStatus);   // to update the service status of that order
                    }
                }
                else {
                    new MyOptionPane("Please select an order code and a status service!", 0, "Error");
                }

                // Resets the radio button for the service status
                if (this.orderAwaitingButton.isSelected()) {                // If the order awaiting status button is selected
                    this.orderAwaitingButton.setSelected(false);            // Resets the order awaiting status for the ready to be served button
                }
                else if (this.orderOngoingButton.isSelected()) {           // If the ongoing preparation status button is selected
                    this.orderOngoingButton.setSelected(false);            // Resets the radio button for the ongoing preparation status button
                }
                else if (this.orderReadyButton.isSelected()) {           // If the ready to be served button is selected
                    this.orderReadyButton.setSelected(false);            // Resets the ready to be served button
                }

                // Updates the table of the served or not served orders
                this.selectedTablePanelReference.refreshPanel();
                //this.selectedTablePanelReference.repaint();

            }
            catch (NumberFormatException | NullPointerException h) {
                new MyOptionPane("The data format you chose is invalid!", 0, "Error");
            }
        }
    }
}

    // Method to refresh the table data
    /*
    public void refreshTableData(final JPanel selectedTablePanelReference) {
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
    */


