package lib.guiComponents.HighBasicComponents;

import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBCreationOperations;
import lib.dbComponents.DBCreationOperationsInterface;
import lib.guiComponents.LowBasicComponents.MyOptionPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/******** Page for the connection to the DB ********/
public class DatabaseConnectionPage extends JFrame implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JTextField userInsertionField;                // Text field for the user insertion
    private final JPasswordField passwordInsertionField;        // Password field for the password insertion
    private final JButton confirmButton;                        // Confirmation button
    private final JButton cancelButton;                         // Cancel button
    private final JButton showHideButton;                       // Show or Hide button
    private final DBCreationOperationsInterface connector;      // DB operations' creation object
    private final DBConnectionClassInterface conn;              // Connection object



    /**** Constructors ****/
    // Main constructor
    public DatabaseConnectionPage(final DBConnectionClassInterface conn) {

        // Setting the frame
        super("Connection to the Database");
        super.setBounds(300, 300, 500, 500);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setLayout(new BorderLayout());

        // Logo for the frame
        // Getting the ImageIcon logo's path
        ImageIcon logo = new ImageIcon(
                Objects.requireNonNull(HomePage.class.getResource("/ForkKnifeLogo.png"))
        );
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Initializing the connection object "conn"
        this.conn = conn;

        // Initializing the operations' creation object "connector"
        this.connector = new DBCreationOperations(this.conn);

        // Main panel to organize the data labels and text-fields
        JPanel mainPanel = new JPanel(new GridBagLayout());

        // Setting the "show/hide" button
        this.showHideButton = new JButton("Show");
        this.showHideButton.setFocusable(false);
        this.showHideButton.setPreferredSize(new Dimension(75, 25));
        this.showHideButton.addActionListener(this);

        // Setting the button panel
        JPanel buttonPanel = new JPanel();

        // Create GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Label for the user insertion
        JLabel userInsertionLabel = new JLabel("DB Username:");

        // Label for the password insertion
        JLabel passwordInsertionLabel = new JLabel("DB Password:");

        // Setting the user and password text-fields
        this.userInsertionField = new JTextField();
        this.userInsertionField.setPreferredSize(new Dimension(180, 25));
        this.passwordInsertionField = new JPasswordField();
        this.passwordInsertionField.setPreferredSize(new Dimension(180, 25));

        // Add labels and fields to the insertion panels
        gbc.gridx = 0;
        mainPanel.add(userInsertionLabel, gbc);

        gbc.gridx = 1;
        mainPanel.add(this.userInsertionField, gbc);

        gbc.gridx = 2;
        mainPanel.add(passwordInsertionLabel, gbc);

        gbc.gridx = 4;
        mainPanel.add(this.passwordInsertionField, gbc);

        gbc.gridx = 5;
        mainPanel.add(this.showHideButton, gbc);

        // Setting the confirmation button
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.setFocusable(false);
        this.confirmButton.addActionListener(this);

        // Setting the cancel button
        this.cancelButton = new JButton("Cancel");
        this.cancelButton.setFocusable(false);
        this.cancelButton.addActionListener(this);

        // Adding the action listener to the password insertion field to have the 'enter' keyword button act as pressing the confirm button
        this.passwordInsertionField.addActionListener(e -> this.confirmButton.doClick());

        // Adding the buttons to the button panel
        buttonPanel.add(this.confirmButton);
        buttonPanel.add(this.cancelButton);

        // Adding the panel and the buttons to the frame
        super.add(mainPanel, BorderLayout.CENTER);
        super.add(buttonPanel, BorderLayout.PAGE_END);

        // Packing and setting the visibility
        super.pack();
        super.setVisible(true);
    }



    /**** Methods ****/
    // Action performed override method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.confirmButton) {                      // If the confirmation button is pressed, try to log in

            // Obtaining the data
            try {
                String username = this.userInsertionField.getText();                        // Getting the user
                String password = new String(this.passwordInsertionField.getPassword());    // Getting the password

                // Connection to the database
                final boolean connectionStatus = this.conn.connectToDB(username, password);     // Boolean variable for the connection status

                // Result of the connection
                if (connectionStatus) {                                         // If the connection is successfully established
                    LocalDate today = LocalDate.now();                          // Get the current data
                    new HomePage(today, this.conn, username, password);         // Open the homepage passing the current "conn" object
                    super.dispose();                                            // Dispose of the current Frame
                }
                else {                                                // If the connection to the DB is not successfully established
                    // Connects tp the mysql server
                    final boolean serverDBExistence = this.conn.checkServerConnection(username, password);  // And check if the DB exists in the server

                    // Result of the DB existence check
                    if (!serverDBExistence) {                         // If the Database does not exist in the server
                        new MyOptionPane("DB unavailable! Creating a new DB...", 1, "Info");

                        // Create a new DB and initialize it, using the established connection to the server
                        createAndInitializeDB(username, password);    // The current connection is to the server and not the DB, that's what will be used here as first
                    }
                }
            }
            catch (NumberFormatException h) {
                new MyOptionPane("The data format you inserted is invalid!", 0, "Error");
            }

        } else if (e.getSource() == this.cancelButton) {                // If the cancel button is pressed, the application closes
            System.exit(0);
        }

        else if (e.getSource() == this.showHideButton) {                // If the show/hide button is pressed, the password becomes visible/invisible
            char passChar = this.passwordInsertionField.getEchoChar();
            if (passChar == '*') {                                      // If the password is currently set as an array of *
                this.passwordInsertionField.setEchoChar((char) 0);      // Set it visible
                this.showHideButton.setText("Hide");                    // And set the button to the "Hide" message
            }
            else {                                                      // If the password is currently set as an array of visible characters
                this.passwordInsertionField.setEchoChar('*');           // Set the characters back to *
                this.showHideButton.setText("Show");                    // And set the button to the "Show" message
            }
        }
    }

    // Create the DB if not existing already
    private void createAndInitializeDB(final String user, final String password) {

        // Creating the new DB
        this.connector.fullDBCreation(user, password);      // Create the DB, the tables in it, populate them and create the triggers

        // Launching a new Database connection page to see if the program can now start with the newly-created DB
        super.dispose();                                 // Dispose the previous frame
        new DatabaseConnectionPage(this.conn);           // And open a new one
    }
}