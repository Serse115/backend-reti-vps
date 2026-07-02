package lib.guiComponents.LowBasicComponents;

import javax.swing.*;
import java.awt.Component;
import java.awt.Container;

/******** Customized option pane for the "pop-up messages" to the user ********/
public class MyOptionPane {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor
    public MyOptionPane(final String message, final int messageType, final String information) {

        // Setting the Pane's settings
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);                            // Setting the message
        optionPane.setMessageType(messageType);                    // Setting the message type
        optionPane.setOptionType(JOptionPane.DEFAULT_OPTION);      // Setting the Option Type
        unFocusTheButton(optionPane);                              // Un-focusing the Button

        // Adding the JDialog
        JDialog dialog = optionPane.createDialog(null, information);
        dialog.setVisible(true);
    }

    // Secondary constructor for the popup message of the new orders found in the DB functionality for the DeliveryServicePanel
    public MyOptionPane(final String message, final int messageType, final String information, int displayTimeMillis) {

        // Setting the Pane's settings
        JOptionPane optionPane = new JOptionPane();
        optionPane.setMessage(message);                            // Setting the message
        optionPane.setMessageType(messageType);                    // Setting the message type
        optionPane.setOptionType(JOptionPane.DEFAULT_OPTION);      // Setting the Option Type
        unFocusTheButton(optionPane);                              // Un-focusing the Button

        // Adding the JDialog
        JDialog dialog = optionPane.createDialog(null, information);
        //dialog.setOpacity(0.85f);

        // Timer per la chiusura automatica
        Timer timer = new Timer(displayTimeMillis, e -> {
            dialog.dispose(); // Chiude la notifica
        });
        timer.setRepeats(false); // Esegue il timer una sola volta
        timer.start();

        dialog.setVisible(true);
    }


    /**** Methods ****/
    // Recursive un-focus button to turn off the highlight of the ok option button in the pane
    private static void unFocusTheButton(Component component) {
        if (component instanceof JButton) {                                   // If the component is a JButton
            component.setFocusable(false);                                    // un-focus it
        }
        else if (component instanceof Container) {                            // Else
            for (Component c : ((Container) component).getComponents()) {     // recursively look through the components to find the button 
                unFocusTheButton(c);                                          // and un-focus it
            }
        }
    }
}