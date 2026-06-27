package lib.guiComponents.HighBasicComponents;

import lib.dbComponents.DBConnectionClassInterface;
import lib.guiComponents.LowBasicComponents.AllReservationsFrame;
import lib.guiComponents.LowBasicComponents.HelpDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Objects;
import javax.swing.ImageIcon;

/******** Home page Class ********/
public class HomePage extends JFrame implements ActionListener {

    /**** Fields ****/
    // Variables
    private final SidePanel sideOptionsPanel;                     // Side options' panel for the option buttons
    private JPanel currentPanel;                                  // Reference to the current panel
    private final MenuBar menuBar;                                // Menu bar for other features
    private final LocalDate useDate;                              // Date used
    private final DBConnectionClassInterface connector;           // Connector object
    private final String username;                                // DB Username
    private final String password;                                // DB Password


    /**** Constructors ****/
    // Main constructor
    public HomePage(LocalDate date, final DBConnectionClassInterface connector, final String username, final String password) {

        // Setting the frame's settings
        super("Restaurant");                                        // Name of the Frame
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);     // Disabling the default close button to avoid issues with the database's connection
        super.setResizable(true);                                      // Enabling the resizable option but with limits (check below)

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();     // Get the screen size

        int width  = (int) (screen.width  * 0.85);                  // 85vw meaning 85% of the width view
        int height = (int) (screen.height * 0.90);                  // 85vh meaning 85% of the height view

        super.setSize(width, height);                   // Set the size of the frame to the sizes defined earlier
        super.setLocationRelativeTo(null);              // Set the location where the frame will appear on the screen when launched

        super.setMinimumSize(new Dimension((int) (screen.width  * 0.55), (int) (screen.height  * 0.65)));

        super.setLayout(new BorderLayout());                            // Setting the layout

        // Defining the connection
        this.connector = connector;                // Defining the object from the interface ConnectionClassInterface for the connection object

        // Defining the username and password
        this.username = username;
        this.password = password;

        // Defining the current use date
        this.useDate = date;

        // Defining the side options panel
        this.sideOptionsPanel = new SidePanel();

        // Defining the menu bar
        this.menuBar = new MenuBar();

        // Adding the actionListener to the buttons for the side options panel
        this.sideOptionsPanel.getHomeButton().addActionListener(this);
        this.sideOptionsPanel.getDisconnectButton().addActionListener(this);
        this.sideOptionsPanel.getReconnectButton().addActionListener(this);
        this.sideOptionsPanel.getExitButton().addActionListener(this);
        this.sideOptionsPanel.getMenuButton().addActionListener(this);
        this.sideOptionsPanel.getWaitersButton().addActionListener(this);
        this.sideOptionsPanel.getReservationsButton().addActionListener(this);
        this.sideOptionsPanel.getServiceStatusButton().addActionListener(this);
        this.sideOptionsPanel.getRefreshButton().addActionListener(this);
        this.sideOptionsPanel.getDeliveryServiceButton().addActionListener(this);

        // Adding the actionListener to the menu-bar items
        this.menuBar.getReservationsItem().addActionListener(this);
        this.menuBar.getDBOperationsItem().addActionListener(this);
        this.menuBar.getHelpItem().addActionListener(this);

        // Defining the main panel and updating the current panel reference
        // Main panel for the HomePage with statistics about the number of customers
        MainPanel mainPanel = new MainPanel(this.useDate, this.connector);
        this.currentPanel = mainPanel;

        // Logo for the frame
        // Getting the ImageIcon logo's path
        ImageIcon logo = new ImageIcon(
                Objects.requireNonNull(HomePage.class.getResource("/ForkKnifeLogo.png"))
        );
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);               // Setting the image as the application's logo

        // Adding the components
        super.add(sideOptionsPanel, BorderLayout.EAST);
        super.add(mainPanel, BorderLayout.CENTER);
        super.add(menuBar, BorderLayout.PAGE_START);

        // Setting the frame to visible
        super.setVisible(true);
    }


    /**** Methods ****/
    // Action performed method Override
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == this.sideOptionsPanel.getDisconnectButton()) {     // Disconnect
            this.connector.closeConnectionToDB();
        }
        else if (e.getSource() == this.sideOptionsPanel.getReconnectButton()) {   // Reconnect
            this.connector.connectToDB(this.username, this.password);
        }
        else if (e.getSource() == this.sideOptionsPanel.getExitButton()) {        // First disconnect and then close the application
            this.connector.closeConnectionToDB();
            System.exit(0);
        }
        else if (e.getSource() == this.sideOptionsPanel.getHomeButton()) {       // Shows the home page
            this.disableCurrentMainPanel();
            this.addNewMainPanel(new MainPanel(this.useDate, this.connector));
        }
        else if (e.getSource() == this.sideOptionsPanel.getMenuButton()) {        // Shows the menu through a new panel
            this.disableCurrentMainPanel();
            this.addNewMainPanel(new MenuPanel(this.connector));
        }
        else if (e.getSource() == this.sideOptionsPanel.getWaitersButton()) {       // Shows the waiters' table through a new panel
            this.disableCurrentMainPanel();
            this.addNewMainPanel(new WaitersPanel(this.connector));
        }
        else if (e.getSource() == this.sideOptionsPanel.getReservationsButton()) {   // Shows the reservations' table through a new panel
            this.disableCurrentMainPanel();
            this.addNewMainPanel(new ReservationPanel(this.connector));
        }
        else if (e.getSource() == this.sideOptionsPanel.getServiceStatusButton()) {      // Shows the orders' table through a new panel
            this.disableCurrentMainPanel();
            this.addNewMainPanel(new OrdersPanel(this.connector));
        }
        else if (e.getSource() == this.sideOptionsPanel.getRefreshButton()) {        // Closes the current frame and opens a new refreshed one
            super.dispose();
            new HomePage(this.useDate, this.connector, this.username, this.password);
        }
        else if (e.getSource() == this.sideOptionsPanel.getDeliveryServiceButton()) {
            this.disableCurrentMainPanel();
            // <----------------------------------------------------------------------------------------------------------
            this.addNewMainPanel(new DeliveryServicePanel(this.connector));              // Opens the deliveryServicePanel
            // <----------------------------------------------------------------------------------------------------------
        }
        else if (e.getSource() == this.menuBar.getReservationsItem()) {      // Shows all the reservations in the database through a new frame
            new AllReservationsFrame(this.connector);
        }
        else if (e.getSource() == this.menuBar.getDBOperationsItem()) {         // Shows database related operations through a new frame
            new DBMenuBarOperationsFrame(this.connector);
        }
        else if (e.getSource() == this.menuBar.getHelpItem()) {              // Shows the help tab through a new frame
            new HelpDialog(this);
        }
    }

    // Disable the current panel method
    private void disableCurrentMainPanel() {
        this.currentPanel.setEnabled(false);
    }
    
    // Add the new Panel as the main panel method
    private void addNewMainPanel(JPanel newPanel) {
        super.remove(currentPanel);                     // Remove the current panel
        this.currentPanel = newPanel;                   // Update the current panel
        super.add(newPanel, BorderLayout.CENTER);       // Add the new panel
        super.revalidate();
        super.repaint();
    }
}