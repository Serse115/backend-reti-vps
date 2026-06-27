package lib.guiComponents.HighBasicComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBWaiterOperations;
import lib.dbComponents.DBWaiterOperationsInterface;
import lib.guiComponents.LowBasicComponents.SelectedWaiterFrame;
import javax.swing.JButton;
import javax.swing.JLabel;

/******** Waiters panel class ********/
public class WaitersPanel extends JPanel implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton[] buttonList;                               // List of buttons for the waiters list
    private final String[] waiterList;                                // List of waiters available to choose from
    private final DBConnectionClassInterface connector;               // Connector object


    /**** Constructors ****/
    // Main constructor
    public WaitersPanel(final DBConnectionClassInterface conn) {
        
        // Setting the panel
        super.setBackground(new Color(238, 238, 238));                  // Setting the panel's background color
        super.setOpaque(true);                                                  // Making the background visible
        super.setLayout(new BorderLayout());                                    // Setting the layout

        // Initializing the connector
        this.connector = conn;

        // Initializing the waiters operations connector objects
        DBWaiterOperationsInterface waiterOperationsConnector = new DBWaiterOperations(this.connector);

        // Setting the text label to direct the user to select a waiter
        JLabel textLabel = new JLabel("Select a waiter from the list below");
        textLabel.setFont(new Font("MV Boli", Font.PLAIN, 40));                          // Setting the message's font and size
        textLabel.setHorizontalAlignment(JLabel.CENTER);                                           // Setting the alignment

        // Setting the white panel to adjust the layout
        JPanel whiteLowLayoutPanel = new JPanel();
        whiteLowLayoutPanel.setBackground(Color.WHITE);
        whiteLowLayoutPanel.setPreferredSize(new Dimension(750, 42));
        whiteLowLayoutPanel.setOpaque(true);

        // Setting the button panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // Setting the button list for the available waiters
        this.waiterList = waiterOperationsConnector.getWaiterCodes();

        // Create the button list for the waiters
        this.buttonList = new JButton[waiterList.length];
        for (int i = 0; i < waiterList.length; i++) {
            this.buttonList[i] = new JButton(waiterList[i]);
            this.buttonList[i].addActionListener(this);
            this.buttonList[i].setFocusable(false);
        }

        // Adding the buttons to the panel
        for (int i = 0; i < waiterList.length; i++) {
            buttonPanel.add(this.buttonList[i]);
        }

        // Adding the button panel to the main panel
        super.add(textLabel, BorderLayout.PAGE_START);
        super.add(buttonPanel, BorderLayout.CENTER);
        super.add(whiteLowLayoutPanel, BorderLayout.SOUTH);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < this.buttonList.length; i++) {
            if (e.getSource() == this.buttonList[i]) {                      // Depending on the selected waiter button
                new SelectedWaiterFrame(waiterList[i], this.connector);     // open a new tab with his assigned tables
            }
        }   
    }   
}