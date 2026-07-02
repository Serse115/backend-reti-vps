package lib.guiComponents.LowBasicComponents;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import lib.dbComponents.*;
import lib.guiComponents.HighBasicComponents.DeliveryServicePanel;
import lib.guiComponents.HighBasicComponents.ReservationPanel;

/******** Delete the reservations frame class ********/
public class DeleteDeliveryOrderFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton closeButton;                                      // Button to confirm the operation and close
    private final JButton[] ordersListButton;                               // Buttons representing the reservations
    private final DBDeliveryOperationsInterface deliveryConnector;   // Reservations operations connector object
    private final DeliveryServicePanel parentPanelReference;                    // Reference to the parent panel (the reservation panel)


    /**** Constructor ****/
    // Main constructor
    public DeleteDeliveryOrderFrame(final DBConnectionClassInterface conn, final DeliveryServicePanel parentPanel) {

        // Setting the frame
        super("Delete reservation");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                          // Creating the image
        super.setIconImage(appLogo);

        // Initializing the reservations operations connector
        this.deliveryConnector = new DBDeliveryOperations(conn);

        // Initializing the parent panel field
        this.parentPanelReference = parentPanel;

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel deliveryOrdersPanel = new JPanel(new FlowLayout());
        deliveryOrdersPanel.setPreferredSize(new Dimension(750, 250));

        // Retrieve the reservations available for deletion from the database
        String[] reservations = this.deliveryConnector.getTheDeliveryOrderCodes();

        // Create reservation buttons
        this.ordersListButton = new JButton[reservations.length];
        for (int i = 0; i < reservations.length; i++) {
            this.ordersListButton[i] = new JButton(reservations[i]);
            this.ordersListButton[i].addActionListener(this);
            this.ordersListButton[i].setFocusable(false);
            deliveryOrdersPanel.add(this.ordersListButton[i]);
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();

        // Setting the confirm button
        this.closeButton = new JButton("Close");
        this.closeButton.setFocusable(false);
        this.closeButton.addActionListener(this);

        buttonPanel.add(this.closeButton);              // Adding the cancel button

        // Add reservation panel and button panel to the main panel
        mainPanel.add(deliveryOrdersPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // Set main panel as the content pane
        super.setContentPane(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        for (JButton jButton : ordersListButton) {

            if (e.getSource() == jButton) {

                // If one of the buttons is pressed, get the code of the relative reservation and its index and delete it
                String selectedDeliveryOrder = jButton.getText();
                //int indexData = this.findDataRowIndex(this.parentPanelReference.returnPanelTable(), selectedDeliveryOrder, 1);    // Column index 1 in the display table

                // Deleting from the database
                this.deliveryConnector.deleteDeliveryOrder(selectedDeliveryOrder);

                // Deleting the row from the current showing table (dynamic refresh)
                //if (indexData != -1) {
                this.parentPanelReference.refreshPanel();      // Dynamic refresh of the table
                //}

                // Dispose of the button of the already deleted reservation (dynamic refresh)
                JPanel reservationPanel = (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.CENTER);
                reservationPanel.remove(jButton);                                   // Remove the clicked button from the panel
                reservationPanel.revalidate();                                      // Recalculate the layout
                reservationPanel.repaint();                                         // Redraw the panel

                // Closes the panel if the reservations number is 0
                if (reservationPanel.getComponentCount() == 0) {
                    new MyOptionPane("All reservations have been deleted!", 1, "Info");
                    super.dispose();                                                // Close the panel
                }
            }
        }

        if (e.getSource() == this.closeButton) {      // Confirm and close the panel
            super.dispose();
        }
    }
}