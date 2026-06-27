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
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBMenuOperations;
import lib.dbComponents.DBMenuOperationsInterface;
import lib.guiComponents.HighBasicComponents.MenuPanel;

/******** Insert the new meal frame class ********/
public class InsertNewMealFrame extends JFrame implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton insertMealButton;                             // Button to insert the reservation into the database
    private final JButton cancelButton;                                 // Button to cancel the operation and close
    private final JTextField[] insertionFields;                         // Insertion fields to know what will be inserted
    private final DBMenuOperationsInterface menuOperationsConnector;    // Menu operations connector
    private final MenuPanel parentPanelReference;                       // Reference to the parent panel (the menu panel)


    /**** Constructors ****/
    // Main constructor
    public InsertNewMealFrame(final DBConnectionClassInterface conn, final MenuPanel parentPanel) {

        // Setting the frame
        super("Insert a new meal");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Initializing the menu operations connector
        this.menuOperationsConnector = new DBMenuOperations(conn);

        // Initializing the parent panel reference
        this.parentPanelReference = parentPanel;

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
        JLabel[] insertionTags = new JLabel[5];
        this.insertionFields = new JTextField[5];

        insertionTags[0] = new JLabel("Meal Code:");
        this.insertionFields[0] = new JTextField();
        this.insertionFields[0].setPreferredSize(new Dimension(180, 25));

        insertionTags[1] = new JLabel("Category:");
        this.insertionFields[1] = new JTextField();
        this.insertionFields[1].setPreferredSize(new Dimension(180, 25));

        insertionTags[2] = new JLabel("Name:");
        this.insertionFields[2] = new JTextField();
        this.insertionFields[2].setPreferredSize(new Dimension(180, 25));

        insertionTags[3] = new JLabel("Price:");
        this.insertionFields[3] = new JTextField();
        this.insertionFields[3].setPreferredSize(new Dimension(180, 25));

        insertionTags[4] = new JLabel("Description:");
        this.insertionFields[4] = new JTextField();
        this.insertionFields[4].setPreferredSize(new Dimension(180, 25));

        insertionTags[5] = new JLabel("Availability:");
        this.insertionFields[5] = new JTextField();
        this.insertionFields[5].setPreferredSize(new Dimension(180, 25));

        // Add labels and fields to the form panel
        for (int i = 0; i < 6; i++) {
            gbc.gridx = 0;
            formPanel.add(insertionTags[i], gbc);

            gbc.gridx = 1;
            formPanel.add(this.insertionFields[i], gbc);

            gbc.gridy++;
        }

        // Create button panel
        JPanel buttonPanel = new JPanel();

        this.insertMealButton = new JButton("Insert");
        this.insertMealButton.setFocusable(false);
        this.insertMealButton.addActionListener(this);

        this.cancelButton = new JButton("Close");
        this.cancelButton.setFocusable(false);
        this.cancelButton.addActionListener(this);

        buttonPanel.add(this.insertMealButton);                      // Adding the insert reservation button
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
        if (e.getSource() == this.insertMealButton) {

            // Obtaining the data
            try {
                String mealCode = this.insertionFields[0].getText();
                String category = this.insertionFields[1].getText();
                String name = this.insertionFields[1].getText();
                double price = Double.parseDouble(this.insertionFields[2].getText());
                String description = this.insertionFields[3].getText();
                int availability = Integer.parseInt(this.insertionFields[4].getText());

                // Adding the data into the table
                this.menuOperationsConnector.insertNewMeal(mealCode, category, name, price, description, availability);

                // Refreshing the menu table (dynamic refresh)
                // Creating a new row data array
                Object[] rowData = {mealCode, category, name, price, description, availability};

                // Add the new meal to the menu panel
                this.parentPanelReference.addData(rowData, this.parentPanelReference.returnPanelTable());
            }
            catch (NumberFormatException h) {
                new MyOptionPane("The data format you inserted is invalid!", 0, "Error");
            }
        }
        else if (e.getSource() == this.cancelButton) {
            super.dispose();
        }
    }
}