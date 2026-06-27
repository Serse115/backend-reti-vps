package lib.guiComponents.LowBasicComponents;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBMenuOperations;
import lib.dbComponents.DBMenuOperationsInterface;
import lib.dbComponents.DynamicRefreshInterface;
import lib.guiComponents.HighBasicComponents.MenuPanel;

/******** Modify the meal frame class ********/
public class ModifyMealFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton confirmButton;                                // Button to confirm the operation and close
    private final JComboBox<String> mealComboBox;                       // Combo box for meal selection
    private final JTextField insertionField;                            // Insertion fields to know what will be inserted
    private final DBMenuOperationsInterface menuOperationsConnector;    // Menu operations connector object
    private final MenuPanel menuPanelReference;                         // Menu panel reference for dynamic refresh


    /**** Constructor ****/
    // Main constructor
    public ModifyMealFrame(final DBConnectionClassInterface conn, final MenuPanel menuPanel) {

        // Setting the frame
        super("Update meal's availability");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setLocationRelativeTo(null);
        super.setResizable(false);

        // Initializing menu operations connector
        this.menuOperationsConnector = new DBMenuOperations(conn);

        // Initializing the menu panel reference
        this.menuPanelReference = menuPanel;

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(500, 150)); // Set the preferred size of the main panel

        // Create form panel
        JPanel mealPanel = new JPanel(new FlowLayout());

        // Retrieve the meal codes available for update from the database
        String[] mealList = this.menuOperationsConnector.getMealCodes();

        // Create meal combo box
        this.mealComboBox = new JComboBox<>(mealList);
        mealPanel.add(this.mealComboBox);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());

        // Create GridBagConstraints for component placement
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Setting the labels and fields
        // Insertion tags to know what will be inserted
        JLabel insertionTag = new JLabel("Meal Availability:");
        this.insertionField = new JTextField();
        this.insertionField.setPreferredSize(new Dimension(180, 25));

        // Add labels and fields to the form panel
        formPanel.add(insertionTag, gbc);
        gbc.gridx = 1;
        formPanel.add(this.insertionField, gbc);

        // Setting the confirmation button
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.setFocusable(false);
        this.confirmButton.addActionListener(this);

        // Adding the components to the frame
        mainPanel.add(mealPanel, BorderLayout.PAGE_START);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(this.confirmButton, BorderLayout.PAGE_END);
        super.setContentPane(mainPanel);

        // Pack the frame to adjust its size
        super.pack();

        // Setting the frame as visible
        super.setVisible(true);
    }

    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.confirmButton) {              // If the confirm button is pressed

            // Obtaining the data
            try {
                String mealCodeChosen = (String) mealComboBox.getSelectedItem();        // Obtain the meal code chosen
                int amount = Integer.parseInt(this.insertionField.getText());           // Obtain the amount chosen

                // Updating the meal's availability
                if (mealCodeChosen != null && amount >= 0) {
                    this.menuOperationsConnector.updateMealAvailability(mealCodeChosen, amount);
                }
                else {
                    new MyOptionPane("Please select a meal code and insert an availability equal to or greater than 0!", 0, "Error");
                }

                // Updating the meal's availability with the dynamic refresh
                this.menuPanelReference.updateMealRow(mealCodeChosen, amount);

                // Show success message and reset field
                this.insertionField.setText("");                // Resets the input field to default
            }
            catch (NumberFormatException h) {
                new MyOptionPane("The data format you inserted is invalid!", 0, "Error");
            }
        }
    }
}