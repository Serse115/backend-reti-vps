package lib.guiComponents.LowBasicComponents;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
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

/******** Remove the meal frame class ********/
public class RemoveMealFrame extends JFrame implements ActionListener, DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JButton confirmButton;                             // Button to confirm the operation and close
    private final JComboBox<String> mealComboBox;                    // Combo box for meal selection
    private final DBMenuOperationsInterface menuOperationsConnector; // Menu operations connector object
    private final MenuPanel parentPanelReference;                    // Reference to the parent panel (the menu panel)



    /**** Constructor ****/
    // Main constructor
    public RemoveMealFrame(final DBConnectionClassInterface conn, final MenuPanel parentPanel) {

        // Setting the frame
        super("Remove meal");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setSize(350, 175);
        super.setResizable(false);

        // Initializing the menu operations connector
        this.menuOperationsConnector = new DBMenuOperations(conn);

        // Initializing the parent panel reference
        this.parentPanelReference = parentPanel;

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(InsertNewReservationFrame.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                          // Creating the image
        super.setIconImage(appLogo);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create form panel
        JPanel mealPanel = new JPanel(new FlowLayout());

        // Retrieve the meal codes available for deletion from the database
        String[] mealList = this.menuOperationsConnector.getMealCodes();

        // Create meal combo box
        this.mealComboBox = new JComboBox<>(mealList);
        mealPanel.add(this.mealComboBox);

        // Create button panel
        JPanel buttonPanel = new JPanel();

        // Setting the close button
        this.confirmButton = new JButton("Confirm");
        this.confirmButton.setFocusable(false);
        this.confirmButton.addActionListener(this);

        buttonPanel.add(this.confirmButton);              // Adding the confirm button

        // Add meal panel and button panel to the main panel
        mainPanel.add(mealPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);

        // Set main panel as the content pane
        super.setContentPane(mainPanel);
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {

        // Retrieve the selected meal from the combo box and find its row index
        String selectedMeal = (String) mealComboBox.getSelectedItem();
        int dataRowIndex = this.findDataRowIndex(this.parentPanelReference.returnPanelTable(), selectedMeal, 0);    // Column index 0 in the display table

        // Delete the selected meal from the database
        if (selectedMeal != null) {
            this.menuOperationsConnector.deleteMeal(selectedMeal);
        }

        // Deleting the row from the current showing table (dynamic refresh)
        if (dataRowIndex != -1) {
            this.parentPanelReference.removeData(dataRowIndex, this.parentPanelReference.returnPanelTable());      // Dynamic refresh of the table
        }

        // If the confirmation button is pressed, confirm and close
        if (e.getSource() == this.confirmButton) {
            super.dispose();
        }
    }
}