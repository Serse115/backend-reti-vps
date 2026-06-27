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
import javax.swing.JRadioButton;
import lib.dbComponents.*;

/******** Frame to assign the waiter class ********/
public class AssignWaiterFrame extends JFrame implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton closeButton;                                      // Button to cancel the operation and close
    private final JRadioButton[] reservationListButton;                     // Buttons representing the reservations
    private final JRadioButton[] waiterCodeRadioButtons;                    // Radio buttons to choose the waiters to assign
    private final JButton confirmButton;                                    // Button to confirm and assign the waiter to the reservation
    private final DBWaiterOperationsInterface waitersOperationsConnector;   // Waiter operations connector object


    /**** Constructors ****/
    // Main constructor
    public AssignWaiterFrame(final DBConnectionClassInterface conn) {

        // Setting the frame
        super("Assign waiter to a reservation");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Initializing the waiters operations connector
        this.waitersOperationsConnector = new DBWaiterOperations(conn);

        // Initializing the reservations operations connector
        DBReservationOperationsInterface reservationsConnector = new DBReservationOperations(conn);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());                  // Main panel to contain the reservations, the waiters and the buttons panel

        // Create form panel
        JPanel reservationPanel = new JPanel(new FlowLayout());                     // Panel for the list of reservations
        reservationPanel.setPreferredSize(new Dimension(800, 350));    // Set size for the reservations panel

        // Retrieve the reservations available from the database
        String[] reservations = reservationsConnector.getReservations();

        // Retrieve the waiter codes available from the database
        String[] waiterCodes = this.waitersOperationsConnector.getWaiterCodes();

        // Create reservation buttons
        this.reservationListButton = new JRadioButton[reservations.length];
        for (int i = 0; i < reservations.length; i++) {
            this.reservationListButton[i] = new JRadioButton(reservations[i]);
            this.reservationListButton[i].addActionListener(this);
            this.reservationListButton[i].setFocusable(false);
            reservationPanel.add(this.reservationListButton[i]);
        }

        // Creating the waiter codes radio buttons
        this.waiterCodeRadioButtons = new JRadioButton[waiterCodes.length];
        for (int i = 0; i < waiterCodes.length; i++) {
            this.waiterCodeRadioButtons[i] = new JRadioButton(waiterCodes[i]);
            this.waiterCodeRadioButtons[i].addActionListener(this);
            this.waiterCodeRadioButtons[i].setFocusable(false);
        }

        // Create choice panel and add waiter code radio buttons to choose and assign the waiter
        JPanel choicePanel = new JPanel();
        for (JRadioButton radioButton : waiterCodeRadioButtons) {
            choicePanel.add(radioButton);
        }
        choicePanel.setVisible(true);                      // Show the choice panel

        // Create button panel
        JPanel buttonPanel = new JPanel();

        // Setting the confirm button
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.setFocusable(false);
        this.confirmButton.addActionListener(this);

        // Setting the close button
        this.closeButton = new JButton("Close");
        this.closeButton.setFocusable(false);
        this.closeButton.addActionListener(this);
        
        // Adding the confirm button
        buttonPanel.add(this.confirmButton);
        
        // Adding the close button
        buttonPanel.add(this.closeButton);

        // Add reservation panel, the choice panel and button panel to the main panel
        mainPanel.add(reservationPanel, BorderLayout.PAGE_START);
        mainPanel.add(choicePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // Set main panel as the content pane
        super.setContentPane(mainPanel);
        super.pack();
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }



    /**** Methods ****/
    // Action performed method
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.closeButton) {                // If the close button is pressed, close the frame 
            super.dispose();

        } else if (e.getSource() == this.confirmButton) {       // If the confirm button is pressed, get the selected reservation and waiter button and update
            String selectedReservation = null;
            String selectedWaiter = null;

            // Get the selected reservation
            for (JRadioButton jRadioButton : reservationListButton) {
                if (jRadioButton.isSelected()) {
                    selectedReservation = jRadioButton.getText();
                    break;
                }
            }

            // Get the selected waiter
            for (JRadioButton waiterCodeRadioButton : waiterCodeRadioButtons) {
                if (waiterCodeRadioButton.getModel().isSelected()) {
                    selectedWaiter = waiterCodeRadioButton.getText();
                    break;
                }
            }

            // Assign the chosen waiter to the chosen reservation
            if (selectedReservation != null && selectedWaiter != null) {
                this.waitersOperationsConnector.assignWaiterToReservation(selectedReservation, selectedWaiter);
            } else {
                new MyOptionPane("Please select a reservation and a waiter.", 0, "Error");
            }

            // Resets the radio buttons after the choice
            for (JRadioButton reservationButton : reservationListButton) {
                reservationButton.setSelected(false);
            }

            // Clear all waiter radio buttons
            for (JRadioButton waiterButton : waiterCodeRadioButtons) {
                waiterButton.setSelected(false);
            }
        }
    }
}