package lib.guiComponents.HighBasicComponents;

import javax.swing.JMenuBar;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/******** Operations bar class ********/
public class MenuBar extends JMenuBar {

    /**** Fields ****/
    // Variables
    private final JMenuItem checkAllReservationsItem;   // Item to check all the reservations registered in the database feature
    private final JMenuItem helpItem;                   // Item for the help feature
    private final JMenuItem DBOperationsItem;           // Item to show other database operations


    /**** Constructors ****/
    // Main constructor
    public MenuBar() {

        // Setting the Menu bar
        super();

        // Defining the two menu to find other options and contents
        JMenu moreOptionsMenu = new JMenu("Options");

        // Menu to find help
        JMenu helpMenu = new JMenu("Help");

        // Defining the shortcuts for the menu
        moreOptionsMenu.setMnemonic(KeyEvent.VK_O);            // ALT O to call the shortcut key and open the Options menu
        helpMenu.setMnemonic(KeyEvent.VK_H);                   // ALT H to call the shortcut key and open the help menu

        // Defining the items
        this.checkAllReservationsItem = new JMenuItem("Check all reservations");
        this.helpItem = new JMenuItem("Info tab");
        this.DBOperationsItem = new JMenuItem("Database operations");

        // Defining the shortcuts for the items
        this.checkAllReservationsItem.setMnemonic(KeyEvent.VK_A);   // A to call the shortcut key and open the check all reservations tab
        this.helpItem.setMnemonic(KeyEvent.VK_I);                   // I to call the shortcut key and open the info tab
        this.DBOperationsItem.setMnemonic(KeyEvent.VK_D);          // D to call the shortcut key and open the DB operations tab

        // Adding the components
        super.add(moreOptionsMenu);                            // Adding the more options menu to the JMenuBar
        super.add(helpMenu);                                   // Adding the help menu to the JMenuBar
        moreOptionsMenu.add(this.checkAllReservationsItem);    // Adding the check all reservations item to the more options menu
        moreOptionsMenu.add(this.DBOperationsItem);            // Adding the DB operations item to the more options menu
        helpMenu.add(this.helpItem);                           // Adding the help item to the help menu
        }


    /**** Methods ****/
    // Get the check all reservations item
    public JMenuItem getReservationsItem() {
        return this.checkAllReservationsItem;
    }

    // Get the DB operations item
    public JMenuItem getDBOperationsItem() {
        return this.DBOperationsItem;
    }

    // Get the help item
    public JMenuItem getHelpItem() {
        return this.helpItem;
    }
}