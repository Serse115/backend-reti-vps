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
import lib.dbComponents.DBOrderOperations;
import lib.dbComponents.DBOrderOperationsInterface;
import lib.dbComponents.DynamicRefreshInterface;

/******** Insert the new order frame class ********/
public class InsertNewOrderFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton insertOrderButton;                            // Button to insert the reservation into the database
    private final JButton cancelButton;                                 // Button to cancel the operation and close
    private final JTextField[] insertionFields;                         // Insertion fields to know what will be inserted
    private final int tableNum;                                         // Reference to the table num
    private final DBOrderOperationsInterface orderOperationsConnector;  // Order operations connector object
    private final SelectedTablePanel parentPanelReference;              // Reference to the parent panel (the orders panel)


    /**** Constructors ****/
    // Main constructor
    public InsertNewOrderFrame(final String tableNum, final DBConnectionClassInterface conn, final SelectedTablePanel parentPanel) {

        // Setting the frame
        super("Insert a new order");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);

        // Initializing the order operations connector
        this.orderOperationsConnector = new DBOrderOperations(conn);

        // Initializing the parent panel reference field
        this.parentPanelReference = parentPanel;

        // Setting the table chosen table number
        this.tableNum = Integer.parseInt(tableNum); 

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                          // Creating the image
        super.setIconImage(appLogo);                                                                              // Setting the logo

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

        insertionTags[0] = new JLabel("Meal code:");
        this.insertionFields[0] = new JTextField();
        this.insertionFields[0].setPreferredSize(new Dimension(180, 25));

        insertionTags[1] = new JLabel("Amount:");
        this.insertionFields[1] = new JTextField();
        this.insertionFields[1].setPreferredSize(new Dimension(180, 25));

        insertionTags[2] = new JLabel("Special requests:");
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

        // Creating the insert order button
        this.insertOrderButton = new JButton("Insert");
        this.insertOrderButton.setFocusable(false);
        this.insertOrderButton.addActionListener(this);

        // Creating the cancel button
        this.cancelButton = new JButton("Close");
        this.cancelButton.setFocusable(false);
        this.cancelButton.addActionListener(this);

        buttonPanel.add(this.insertOrderButton);                      // Adding the insert reservation button
        buttonPanel.add(this.cancelButton);                           // Adding the cancel button

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
        if (e.getSource() == this.insertOrderButton) {

            // Obtaining the data
            try {
                String mealCode = this.insertionFields[0].getText();
                int amount = Integer.parseInt(this.insertionFields[1].getText());
                String specialReq = this.insertionFields[2].getText();

                // Adding the data into the table
                this.orderOperationsConnector.insertNewOrder(tableNum, mealCode, amount, specialReq);

                // Obtaining the data for the dynamic refresh
                int orderNum = this.orderOperationsConnector.getDataFromRows("order_num", tableNum, mealCode, amount, Integer.class);
                Character service = this.orderOperationsConnector.getDataFromRows("service", tableNum, mealCode, amount, Character.class);

                // Create a new row data array
                Object[] rowData = {orderNum, tableNum, mealCode, amount, specialReq, service};

                // Add the new order to the order panel
                this.parentPanelReference.addData(rowData, this.parentPanelReference.returnParentsTable());

                // Resets the insertion fields
                for (JTextField field : this.insertionFields) {
                    field.setText("");                          // Sets each field's text to an empty string
                }

                // Sets focus to the first field for quick new entry
                this.insertionFields[0].requestFocusInWindow();
            }
            catch (NumberFormatException h) {
                new MyOptionPane("The data format you inserted is invalid!", 0, "Error");
                h.printStackTrace();
            }
        }
        
        else if (e.getSource() == this.cancelButton) {
            super.dispose();                                // Close the frame
        }
    }
}