package lib.guiComponents.LowBasicComponents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JPanel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBWaiterOperations;
import lib.dbComponents.DBWaiterOperationsInterface;
import lib.guiComponents.HighBasicComponents.HomePage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/******** Selected waiter frame class ********/
public class SelectedWaiterFrame extends JFrame implements ActionListener{

    /**** Fields ****/
    // Variables
    private final JButton[] tablesButtonList;                       // Button list to show the tables assigned to every waiter
    private JPanel currentPanel;                                    // Reference to the current panel in use
    private final String[] tablesList;                              // List of tables assigned to the selected waiter
    private final DBConnectionClassInterface connector;             // Connector object


    /**** Constructors ****/
    // Main constructor
    public SelectedWaiterFrame(final String waiterCode, final DBConnectionClassInterface conn) {

        // Setting the frame
        super("Table choice");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setSize(700, 600);
        super.setResizable(false);

        // Initializing the connector
        this.connector = conn;

        // Initializing the waiters operations connector
        DBWaiterOperationsInterface waitersConnector = new DBWaiterOperations(this.connector);

        // Logo for the frame
        // Getting the ImageIcon logo's path
        ImageIcon logo = new ImageIcon(
                Objects.requireNonNull(HomePage.class.getResource("/ForkKnifeLogo.png"))
        );
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);

        // Setting the label to direct the user to choose a waiter
        JLabel textLabel = new JLabel("Available tables");
        textLabel.setFont(new Font("MV Boli", Font.PLAIN, 20));                          // Setting the message's font and size
        textLabel.setHorizontalAlignment(JLabel.CENTER);                                           // Setting the alignment

        // Setting the main panel to add components to it
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Setting the choice panel to choose the waiter which tables need to be updated
        JPanel tablesChoicePanel = new JPanel(new FlowLayout());

        // Setting the list of buttons
        tablesList = waitersConnector.getTheTableCodes(waiterCode);             // Get the table codes with the selected waiter code
        this.tablesButtonList = new JButton[tablesList.length];                 // Creating a list of buttons for the tables assigned to that waiter

        // Create the waiter buttons
        for (int i = 0; i < tablesList.length; i++) {
            this.tablesButtonList[i] = new JButton(tablesList[i]);
            this.tablesButtonList[i].setFocusable(false);
            this.tablesButtonList[i].addActionListener(this);
            tablesChoicePanel.add(this.tablesButtonList[i]);
        }

        // Adding the components
        mainPanel.add(textLabel, BorderLayout.NORTH);
        mainPanel.add(tablesChoicePanel, BorderLayout.CENTER);
        super.add(mainPanel);

        // Setting the current panel reference to the main panel
        this.currentPanel = mainPanel;

        // Setting the frame as visible
        super.setVisible(true);
    }

    

    /**** Methods ****/
    // Action listener method
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < this.tablesButtonList.length; i++) {
            if (e.getSource() == this.tablesButtonList[i]) {
                this.disableCurrentMainPanel();
                this.addNewMainPanel(new SelectedTablePanel(tablesList[i], this.connector));
            }
        }
        
    }

    // Disable the current panel method
    private void disableCurrentMainPanel() {
        this.currentPanel.setEnabled(false);
    }
    
    // Add the new Panel as the main panel method
    private void addNewMainPanel(JPanel newPanel) {
        super.remove(currentPanel);
        this.currentPanel = newPanel;
        super.add(newPanel, BorderLayout.CENTER);
        super.revalidate();
        super.repaint();
    } 
}