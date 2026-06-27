package lib.guiComponents.LowBasicComponents;

import lib.dbComponents.DBConnectionClassInterface;
import lib.guiComponents.HighBasicComponents.ReservationPanel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/******** Reservations toolbar class ********/
public class ReservationToolBar extends JPanel implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton insertReservationButton;          // Button to insert a reservation into the database
    private final JButton deleteReservationButton;          // Button to delete a reservation
    private final JButton assignWaiter;                     // Button to assign the waiter to the table
    private final DBConnectionClassInterface connector;     // Reservations operations connector object
    private final ReservationPanel parentPanelReference;    // Parent panel reference field
    

    /**** Constructors ****/
    // Main constructor
    public ReservationToolBar(final DBConnectionClassInterface conn, final ReservationPanel parentPanel) {

        // Setting the panel
        super.setBackground(Color.WHITE);

        // Initializing the reference to the parent panel
        this.parentPanelReference = parentPanel;

        // Initializing the reservations operations connector
        this.connector = conn;

        // Defining the button to insert a new reservation
        this.insertReservationButton = new JButton("Add reservation");
        this.insertReservationButton.setFocusable(false);
        this.insertReservationButton.addActionListener(this);

        // Defining the button to delete a reservation
        this.deleteReservationButton = new JButton("Delete reservation");
        this.deleteReservationButton.setFocusable(false);
        this.deleteReservationButton.addActionListener(this);

        // Defining the button to assign a waiter to the reservation
        this.assignWaiter = new JButton("Assign waiter");
        this.assignWaiter.setFocusable(false);
        this.assignWaiter.addActionListener(this);

        // Adding the components
        super.add(this.insertReservationButton);
        super.add(this.deleteReservationButton);
        super.add(this.assignWaiter);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.insertReservationButton) {        // If the option chosen is to insert a new reservation, open the tab for it  
            new InsertNewReservationFrame(this.connector, this.parentPanelReference);
        }
        else if (e.getSource() == this.deleteReservationButton) {   // If the option chosen is to delete a reservation, open the tab for it
            new DeleteReservationFrame(this.connector, this.parentPanelReference);
        }
        else if (e.getSource() == this.assignWaiter) {              // If the option chosen is to assign a waiter to the reservation, open the tab for it
            new AssignWaiterFrame(this.connector);
        }
    }
}