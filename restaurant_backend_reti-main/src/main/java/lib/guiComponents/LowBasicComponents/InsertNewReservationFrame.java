package lib.guiComponents.LowBasicComponents;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBReservationOperations;
import lib.dbComponents.DBReservationOperationsInterface;
import lib.dbComponents.DynamicRefreshInterface;
import lib.guiComponents.HighBasicComponents.ReservationPanel;

/******** Class for the new reservation frame ********/
public class InsertNewReservationFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton insertReservationButton;                                      // Button to insert the reservation into the database
    private final JButton cancelButton;                                                 // Button to cancel the operation and close
    private final JTextField[] insertionFields;                                         // Insertion fields to know what will be inserted
    private final DBReservationOperationsInterface reservationsOperationsConnector;     // Reservations operations connector object
    private final ReservationPanel parentPanelReference;                                // Reference to the parent panel (the reservation panel)


    /**** Constructors ****/
    // Main constructor
    public InsertNewReservationFrame(final DBConnectionClassInterface conn, final ReservationPanel parentPanel) {

        // Setting the frame
        super("Insert a new reservation");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Initializing the parent panel field
        this.parentPanelReference = parentPanel;

        // Initializing the reservations operations connector
        this.reservationsOperationsConnector = new DBReservationOperations(conn);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                          // Creating the image
        super.setIconImage(appLogo);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());

        // Create GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Setting the labels and field tags to know what will be inserted
        JLabel[] insertionTags = new JLabel[3];
        this.insertionFields = new JTextField[3];

        insertionTags[0] = new JLabel("Reservation holder name:");                  // Name for the reservation
        this.insertionFields[0] = new JTextField();
        this.insertionFields[0].setPreferredSize(new Dimension(180, 25));

        insertionTags[1] = new JLabel("Number of seats:");                          // Number of seats for the reservation
        this.insertionFields[1] = new JTextField();
        this.insertionFields[1].setPreferredSize(new Dimension(180, 25));

        insertionTags[2] = new JLabel("Date (YYYY-MM-DD):");                        // Date of the reservation
        this.insertionFields[2] = new JTextField();
        this.insertionFields[2].setPreferredSize(new Dimension(180, 25));

        // Add labels and fields to the form panel
        for (int i = 0; i < 3; i++) {
            gbc.gridx = 0;
            formPanel.add(insertionTags[i], gbc);

            gbc.gridx = 1;
            formPanel.add(this.insertionFields[i], gbc);

            gbc.gridy++;
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();

        // Defining the insert reservations button
        this.insertReservationButton = new JButton("Insert");
        this.insertReservationButton.setFocusable(false);
        this.insertReservationButton.addActionListener(this);

        // Defining the cancel button
        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setFocusable(false);
        this.cancelButton.addActionListener(this);

        buttonPanel.add(this.insertReservationButton);               // Adding the insert reservation button
        buttonPanel.add(this.cancelButton);                          // Adding the cancel button

        // Add form panel and button panel to the main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
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
        if (e.getSource() == this.insertReservationButton) {            // If the insert reservation button is pressed

            // Obtaining the data
            try {
                String name = this.insertionFields[0].getText();                    // Get the data
                int seats = Integer.parseInt(this.insertionFields[1].getText());    // from every
                String date = this.insertionFields[2].getText();                    // text field

                // Adding the data into the table and refreshing the panel
                this.reservationsOperationsConnector.insertReservation(name, seats, date);

                // Obtaining the values needed for the correct panel refresh display process
                int reservationNumAndTable = this.reservationsOperationsConnector.getDataFromRows("reservation_num", "reservation_management", name, Date.valueOf(date));

                // Create a new row data array
                Object[] rowData = {reservationNumAndTable, name, date, seats, reservationNumAndTable};

                // Add the new reservation to the reservation panel
                this.parentPanelReference.addData(rowData, this.parentPanelReference.returnPanelTable());

                // Resets the insertion fields
                for (JTextField field : this.insertionFields) {
                    field.setText("");                          // Sets each field's text to an empty string
                }

                // Sets focus to the first field for quick new entry
                this.insertionFields[0].requestFocusInWindow();
            }
            catch (NumberFormatException h) {
                new MyOptionPane("The data format you inserted is invalid!", 0, "Error");
            }
        }
        else if (e.getSource() == this.cancelButton) {                  // If the cancel button is pressed, dispose of the current frame
            super.dispose();
        }
    }
}