package lib.guiComponents.HighBasicComponents;

import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBReservationOperations;
import lib.dbComponents.DBReservationOperationsInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.time.LocalDate;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/******** Main panel class ********/
public class MainPanel extends JPanel {

    /**** Fields ****/


    /**** Constructors ****/
    // Main constructor
    public MainPanel(LocalDate date, DBConnectionClassInterface connector) {

        // Setting the Panel's settings
        super.setLayout(new BorderLayout());

        // Setting the reservations' connection object
        DBReservationOperationsInterface reservationConnector = new DBReservationOperations(connector);

        // Getting the number of current reservations
        int nOfReservations = reservationConnector.getTotalReservationsNumber(date);

        // Getting the number of current customers
        int nOfCustomers = reservationConnector.getTotalNumberOfCustomers(date);

        // Set gray border to the main panel
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Setting the label's settings and contents
        String welcomeMessage = "<html>Welcome! As of " + date +                                    // Welcome message for the user
                                "<br>There are " + nOfReservations + " reservations" +              // that pops up when the application
                                "<br>For a total of " + nOfCustomers + " customers!</html>";        // is started

        // Label for the welcome message
        JLabel label = new JLabel(welcomeMessage);                                    // Setting the label with the message
        label.setHorizontalAlignment(JLabel.CENTER);                                  // Setting the location of the message
        label.setBackground(new Color(238, 238, 238));                       // Setting the label's background color
        label.setOpaque(true);                                                        // Making the background visible
        label.setFont(new Font("MV Boli", Font.PLAIN, 40));                // Setting the message's font and size

        // Create a separate panel for the progress bar
        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setBackground(Color.WHITE);
        progressBarPanel.setLayout(new BorderLayout());

        // Progress bar for number of current customers / max number of customers
        JProgressBar progressBar = new JProgressBar(0, 255);
        progressBar.setValue(nOfCustomers);                                   // Setting the value as the number of current customers
        progressBar.setPreferredSize(new Dimension(155, 50));     // Setting the preferred size for the bar
        progressBar.setStringPainted(true);                                   // Setting as visible the String painting the percentage
        progressBar.setForeground(Color.GREEN);                               // Setting the color of the progress notification
        progressBar.setBackground(Color.WHITE);                               // Setting the color of the rest of the bar

        // Setting the label for the number of customers / number of possible customers
        String percentage = nOfCustomers + "/255 possible customers";
        JLabel progressLabel = new JLabel(percentage);
        progressLabel.setHorizontalAlignment(JLabel.CENTER);                             // Setting the location of the message
        progressLabel.setBackground(new Color(238, 238, 238));                  // Setting the label's background color
        progressLabel.setOpaque(true);                                                   // Making the background visible

        // Add the progress bar to the progress bar panel
        progressBarPanel.add(progressBar, BorderLayout.SOUTH);
        progressBarPanel.add(progressLabel, BorderLayout.CENTER);

        // Add the components to the main panel
        add(label, BorderLayout.CENTER);
        add(progressBarPanel, BorderLayout.SOUTH);
    }

    /**** Methods ****/
    // Empty
}