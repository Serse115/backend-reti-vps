package lib.guiComponents.LowBasicComponents;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/******** Help frame class ********/
//public class HelpFrame extends JFrame {
public class HelpDialog extends JDialog {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor
    public HelpDialog(JFrame parentFrame) {

        // Setting the frame's settings
        // Modal dialog, always on top of parent
        super(parentFrame, "Help tab", true);
        //super("Help tab");
        setLayout(new BorderLayout());
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Logo for the app
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(HelpDialog.class.getResource("/ForkKnifeLogo.png")));    // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                         // Creating the image
        super.setIconImage(appLogo);                                                            // Setting the image as the application's logo

        // Setting the help text for the user
        // Text for the user about the home page
        final String homePageHelpText = "The HOMEPAGE allows to see how many reservations are present at the current moment and it shows a graphic example of that percentage.\n";

        // Text for the user about the reservation panel
        final String reservationsHelpText = "On the RESERVATION PANEL you can add a reservation or delete one. To add one, make sure you pay attention to the format " +
                "of the data you insert.\nTo delete one you just need to press on the button representing the chosen reservation.\n" +
                "You can assign a waiter to a reservation (to its equivalent table) by choosing the table's holder name and the waiter's code below.";

        // Text for the user about the waiter panel
        final String waitersHelpText = "\nThe WAITER PANEL introduces you to the list of waiters available, you can choose one and see which tables are assigned to it. " +
                "\nOnce selected the table, you can insert an order for that table or set an already existing one as 'already served', " +
                "it will be useful for the next panel.\n";

        // Text for the user about the service panel
        final String serviceHelpText = "The SERVICE PANEL shows the current status of the orders service, the shown ones are the orders that are yet to serve, while you can " +
                "see the full list of orders from the button below.\n";

        // Text for the user about the menu panel
        final String menuHelpText = "The MENU PANEL introduces the available meals as a table and you can either add a new meal, update a meal's available in terms of portions" +
                " or remove the meal entirely from the menu.\n";

        // Text for the user about the refresh option
        final String refreshHelpText = "The REFRESH button allows you to refresh the whole application\n";

        // Text for the user about the disconnect option
        final String disconnectHelpText = "The DISCONNECT button allows you to temporarily disconnect from the database\n";

        // Text for the user about the disconnect option
        final String reconnectHelpText = "The RECONNECT button allows you to reconnect to the database in case the database has been disconnected\n";

        // Text for the user about the exit button
        final String exitHelpText = "The EXIT button allows you to close the application, disconnecting from the database first\n";

        // Text for the user about the menu bar options
        final String menuBarHelpText = "The MENU BAR allows you to perform more operations, for example with the first option 'CHECK ALL RESERVATIONS', you can check every " +
                "reservation registered and not only the ones for the current day.\nThen there's the other option called DATABASE OPERATIONS where you" +
                " can choose other operations for the database maintenance:\nDELETE ALL RESERVATIONS = Deletes all reservations/orders and resets the values to 0." +
                "\nDELETE ALL ORDERS = Deletes only the orders and resets the values to 0." + "\nDELETE ALL WAITERS = Delete all waiters data." +
                "\nDELETE ALL MENU = Deletes all menu data and resets the values to 0.\nDELETE ALL DATABASE DATA = does all the ones above together." +
                "\n\nPlease notice that resetting the values to 0 means that the auto-incrementing values representing for example the reservation number" +
                " or the order number will be brought back to 0.\nIT IS RECOMMENDED TO DO IT OFTEN TO AVOID DATA ORDERING ISSUES.\n";

        // Text for the user about the shortcuts
        final String shortcutHelpText = "Press ALT O to call the shortcut key and open the Options menu.\n" +
                "Press ALT H to call the shortcut key and open the help menu.\n" +
                "After pressing ALT O, press A to call the shortcut key and open the check all reservations tab.\n" +
                "After pressing ALT O, press D to call the shortcut key and open the DB operations tab.\n" +
                "After pressing ALT H, press I to call the shortcut key and open the info tab";

        // Combining the string messages
        // Full string for the text help
        final String fullHelpStringText = homePageHelpText + "\n" + reservationsHelpText + "\n" + waitersHelpText + "\n" +
                serviceHelpText + "\n" + menuHelpText + "\n" + refreshHelpText + "\n" +
                disconnectHelpText + "\n" + reconnectHelpText + "\n" + exitHelpText + "\n" + menuBarHelpText + "\n" + shortcutHelpText;

        // Setting the help label for the help message
        JTextArea helpArea = new JTextArea();
        helpArea.setEditable(false);
        helpArea.setText(fullHelpStringText);
        helpArea.setBackground(new Color(238, 238, 238));                  // Setting the label's background color
        helpArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(helpArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        add(scrollPane, BorderLayout.CENTER);

        // Size from content
        pack();

        // Center relative to parent
        setLocationRelativeTo(parentFrame);

        setVisible(true);
    }


    /**** Methods ****/
    // Empty
}