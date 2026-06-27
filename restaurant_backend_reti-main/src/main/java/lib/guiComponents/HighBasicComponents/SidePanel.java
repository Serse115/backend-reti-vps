package lib.guiComponents.HighBasicComponents;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

/******** Side panel class ********/
public class SidePanel extends JPanel {

    /**** Fields ****/
    // Variables
    private final JButton disconnectButton;                             // Disconnect button
    private final JButton reconnectButton;                              // Reconnect button
    private final JButton exitButton;                                   // Exit button
    private final JButton menuButton;                                   // Menu button
    private final JButton waitersButton;                                // Waiters button
    private final JButton reservationsButton;                           // Reservations button
    private final JButton serviceStatusButton;                          // Service status button
    private final JButton refreshButton;                                // Refresh button
    private final JButton homeButton;                                   // Home button
    private final JButton deliveryServiceButton;                        // Delivery service orders and status button


    /**** Constructors ****/
    // Main constructor
    public SidePanel() {

        // Setting the Panel
        super.setBackground(Color.WHITE);                                           // Setting the background color
        super.setOpaque(true);                                                      // Setting the color as visible
        super.setPreferredSize(new Dimension(120, 150));                // Preferred size for the panel
        super.setLayout(new GridLayout(10, 1, 0, 15));         // Layout for the buttons (10 buttons)

        // Defining the buttons
        // Disconnect button
        this.disconnectButton = new JButton("Disconnect");
        this.disconnectButton.setFocusable(false);

        // Reconnect button
        this.reconnectButton = new JButton("Reconnect");
        this.reconnectButton.setFocusable(false);

        // Exit button
        this.exitButton = new JButton("Exit");
        this.exitButton.setFocusable(false);

        // Show menu button
        this.menuButton = new JButton("Menu");
        this.menuButton.setFocusable(false);

        // Waiters button
        this.waitersButton = new JButton("Waiters");
        this.waitersButton.setFocusable(false);

        // Tables button
        this.reservationsButton = new JButton("Reservations");
        this.reservationsButton.setFocusable(false);

        // Service status button
        this.serviceStatusButton = new JButton("Service Status");
        this.serviceStatusButton.setFocusable(false);

        // Refresh button
        this.refreshButton = new JButton("Refresh");
        this.refreshButton.setFocusable(false);

        // Home button
        this.homeButton = new JButton("Home Page");
        this.homeButton.setFocusable(false);

        // Delivery service button
        this.deliveryServiceButton = new JButton("Delivery");
        this.deliveryServiceButton.setFocusable(false);

        // Adding the buttons to the layout
        super.add(this.homeButton);
        super.add(this.reservationsButton);
        super.add(this.waitersButton);
        super.add(this.serviceStatusButton);
        super.add(this.deliveryServiceButton);
        super.add(this.menuButton);
        super.add(this.refreshButton);
        super.add(this.disconnectButton);
        super.add(this.reconnectButton);
        super.add(this.exitButton);
    }

    
    /**** Methods ****/
    // Get the buttons methods
    // Get the home button
    public JButton getHomeButton() {
        return this.homeButton;
    }

    // Get the reservations button
    public JButton getReservationsButton() {
        return this.reservationsButton;
    }

    // Get the waiters button
    public JButton getWaitersButton() {
        return this.waitersButton;
    }

    // Get the service status button
    public JButton getServiceStatusButton() {
        return this.serviceStatusButton;
    }

    // Get the menu button
    public JButton getMenuButton() {
        return this.menuButton;
    }

    // Get the refresh button
    public JButton getRefreshButton() {
        return this.refreshButton;
    }

    // Get the disconnect button
    public JButton getDisconnectButton() {
        return this.disconnectButton;
    }

    // Get the reconnect button
    public JButton getReconnectButton() {
        return this.reconnectButton;
    }

    // Get the exit button
    public JButton getExitButton() {
        return this.exitButton;
    }

    // Get the delivery service button
    public JButton getDeliveryServiceButton() {
        return this.deliveryServiceButton;
    }
}