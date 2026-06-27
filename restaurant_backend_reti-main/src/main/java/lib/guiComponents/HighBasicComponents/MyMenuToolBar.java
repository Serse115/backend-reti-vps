package lib.guiComponents.HighBasicComponents;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;

/******** Toolbar for the menu operations class ********/
public class MyMenuToolBar extends JPanel {

    /**** Fields ****/
    // Variables
    private final JButton insertDishButton;                 // Button to add a new dish to the menu
    private final JButton modifyDishButton;                 // Button to modify something about an already existing dish
    private final JButton removeDishButton;                 // Button to remove a dish from the menu


    /**** Constructors ****/
    // Main constructor
    public MyMenuToolBar() {

        // Setting the panel
        super.setBackground(Color.WHITE);

        // Defining the insert new dish button
        this.insertDishButton = new JButton("Add new meal");
        this.insertDishButton.setFocusable(false);

        // Defining the "modify dish" button
        this.modifyDishButton = new JButton("Update meal's availability");
        this.modifyDishButton.setFocusable(false);

        // Defining the remove a dish button
        this.removeDishButton = new JButton("Remove meal");
        this.removeDishButton.setFocusable(false);

        // Adding the components
        super.add(this.insertDishButton);
        super.add(this.modifyDishButton);
        super.add(this.removeDishButton);
    }

    /**** Methods ****/
    // Get the "insert dish" button
    public JButton getInsertDishButton() {
        return this.insertDishButton;
    }

    // Get the "modify dish" button
    public JButton getModifyDishButton() {
        return this.modifyDishButton;
    }

    // Get the "remove dish" button
    public JButton getRemoveDishButton() {
        return this.removeDishButton;
    }
}