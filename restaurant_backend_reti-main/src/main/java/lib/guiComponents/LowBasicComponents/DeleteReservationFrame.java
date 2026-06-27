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
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBReservationOperations;
import lib.dbComponents.DBReservationOperationsInterface;
import lib.dbComponents.DynamicRefreshInterface;
import lib.guiComponents.HighBasicComponents.ReservationPanel;

/******** Delete the reservations frame class ********/
public class DeleteReservationFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton closeButton;                                      // Button to confirm the operation and close
    private final JButton[] reservationListButton;                          // Buttons representing the reservations
    private final DBReservationOperationsInterface reservationsConnector;   // Reservations operations connector object
    private final ReservationPanel parentPanelReference;                    // Reference to the parent panel (the reservation panel)


    /**** Constructor ****/
    // Main constructor
    public DeleteReservationFrame(final DBConnectionClassInterface conn, final ReservationPanel parentPanel) {

        // Setting the frame
        super("Delete reservation");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                          // Creating the image
        super.setIconImage(appLogo);

        // Initializing the reservations operations connector
        this.reservationsConnector = new DBReservationOperations(conn);

        // Initializing the parent panel field
        this.parentPanelReference = parentPanel;

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel reservationPanel = new JPanel(new FlowLayout());
        reservationPanel.setPreferredSize(new Dimension(750, 250));

        // Retrieve the reservations available for deletion from the database
        String[] reservations = this.reservationsConnector.getReservations();

        // Create reservation buttons
        this.reservationListButton = new JButton[reservations.length];
        for (int i = 0; i < reservations.length; i++) {
            this.reservationListButton[i] = new JButton(reservations[i]);
            this.reservationListButton[i].addActionListener(this);
            this.reservationListButton[i].setFocusable(false);
            reservationPanel.add(this.reservationListButton[i]);
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();

        // Setting the confirm button
        this.closeButton = new JButton("Close");
        this.closeButton.setFocusable(false);
        this.closeButton.addActionListener(this);

        buttonPanel.add(this.closeButton);              // Adding the cancel button

        // Add reservation panel and button panel to the main panel
        mainPanel.add(reservationPanel, BorderLayout.CENTER);
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
        for (JButton jButton : reservationListButton) {

            if (e.getSource() == jButton) {

                // If one of the buttons is pressed, get the code of the relative reservation and its index and delete it
                String selectedReservation = jButton.getText();
                int indexData = this.findDataRowIndex(this.parentPanelReference.returnPanelTable(), selectedReservation, 1);    // Column index 1 in the display table

                // Deleting from the database
                this.reservationsConnector.deleteReservation(selectedReservation);

                // Deleting the row from the current showing table (dynamic refresh)
                if (indexData != -1) {
                    this.parentPanelReference.removeData(indexData, this.parentPanelReference.returnPanelTable());      // Dynamic refresh of the table
                }

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