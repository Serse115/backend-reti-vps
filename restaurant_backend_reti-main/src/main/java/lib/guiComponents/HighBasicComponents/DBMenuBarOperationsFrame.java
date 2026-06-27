package lib.guiComponents.HighBasicComponents;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import javax.swing.JFrame;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBMenuBarDeletion;
import lib.dbComponents.DBMenuBarDeletionInterface;
import lib.guiComponents.LowBasicComponents.MyOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/******** Class for the Database MenuBar operations ********/
public class DBMenuBarOperationsFrame extends JFrame implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton[] operationsList;                      // List of buttons for the operations available
    private final DBMenuBarDeletionInterface deletionConnector;  // DB Menu bar connector object


    /**** Constructors ****/
    // Main constructor
    public DBMenuBarOperationsFrame(DBConnectionClassInterface connector) {
        super("Database operations");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        super.setResizable(false);
        super.setLayout(new FlowLayout());

        // Logo for the frame
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(DBMenuBarOperationsFrame.class.getResource("/ForkKnifeLogo.png")));  // Getting the ImageIcon logo's path
        Image appLogo = logo.getImage();                                                                       // Creating the image
        super.setIconImage(appLogo);

        // Initializing the deletion connector object
        this.deletionConnector = new DBMenuBarDeletion(connector);

        // Defining the button list
        this.operationsList = new JButton[6];

        // Setting the button to clear all reservations
        this.operationsList[0] = new JButton("Delete all reservations");
        this.operationsList[0].setFocusable(false);
        this.operationsList[0].addActionListener(this);

        // Setting the button to clear all orders
        this.operationsList[1] = new JButton("Delete all orders");
        this.operationsList[1].setFocusable(false);
        this.operationsList[1].addActionListener(this);

        // Setting the button to delete all the waiters
        this.operationsList[2] = new JButton("Delete waiters");
        this.operationsList[2].setFocusable(false);
        this.operationsList[2].addActionListener(this);

        // Setting the button to clear the menu and the dishes list
        this.operationsList[3] = new JButton("Delete menu");
        this.operationsList[3].setFocusable(false);
        this.operationsList[3].addActionListener(this);

        // Setting the button to clear the delivery orders data info list
        this.operationsList[4] = new JButton("Delete delivery orders");
        this.operationsList[4].setFocusable(false);
        this.operationsList[4].addActionListener(this);

        // Setting the button to clear everything in the database
        this.operationsList[5] = new JButton("Delete all database data");
        this.operationsList[5].setFocusable(false);
        this.operationsList[5].addActionListener(this);

        // Adding the buttons to the frame
        super.add(this.operationsList[0]);
        super.add(this.operationsList[1]);
        super.add(this.operationsList[2]);
        super.add(this.operationsList[3]);
        super.add(this.operationsList[4]);
        super.add(this.operationsList[5]);

        // Setting the layout with the pack method
        super.pack();

        // Setting the frame's visibility
        super.setVisible(true);
    }


    /**** Methods ****/
    // Action performed method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.operationsList[0]) {                              // If the button to delete all reservations is pressed, call the method for it
            this.deletionConnector.deleteAllReservations();                         // All reservations and orders get deleted, with the foreign key on delete cascade the
            this.deletionConnector.resetTable("reservation_management");  // management table's data get deleted automatically together with the reservations
            this.deletionConnector.resetTable("table_management");        // The 3 tables also get a reset
            this.deletionConnector.deleteAllOrders();
            this.deletionConnector.resetTable("order_management");
            new MyOptionPane("Data deleted and Auto incrementation reset completed successfully", 1, "Info");
        }
        else if (e.getSource() == this.operationsList[1]) {                     // If the button to delete all orders is pressed, call the method for it
            this.deletionConnector.deleteAllOrders();                           // All the orders get deleted and the order management table gets a reset
            this.deletionConnector.resetTable("order_management");
            new MyOptionPane("Data deleted and Auto incrementation reset completed successfully", 1, "Info");
        }
        else if (e.getSource() == this.operationsList[2]) {                 // If the button to delete all waiters' data is pressed, call the method for it
            this.deletionConnector.deleteAllWaiters();                      // The waiter table's data get deleted and gets a reset
            new MyOptionPane("Data deleted!", 1, "Info");
        }
        else if (e.getSource() == this.operationsList[3]) {                 // If the button to delete all menu data is pressed, call the method for it
            this.deletionConnector.deleteAllMenu();                         // The menu table's data get deleted
            this.deletionConnector.resetTable("menu");
            new MyOptionPane("Data deleted and Auto incrementation reset completed successfully", 1, "Info");
        }
        else if (e.getSource() == this.operationsList[4]) {                 // If the button to delete delivery orders data is pressed, call the method for it
            this.deletionConnector.deleteAllDeliveryOrders();                         // The delivery orders info table's data get deleted
            //this.deletionConnector.resetTable("menu");
            new MyOptionPane("Data deleted completed successfully", 1, "Info");
        }
        else if (e.getSource() == this.operationsList[5]) {                 // If the button to delete all the DB data is pressed, call all the methods for it
            this.deletionConnector.deleteAllReservations();                 // Delete all data resets the DB (reservations, orders and menu table),
            this.deletionConnector.deleteAllOrders();                       // also resetting the tables
            this.deletionConnector.deleteAllMenu();
            this.deletionConnector.deleteAllDeliveryOrders();
            this.deletionConnector.resetTable("reservation_management");
            this.deletionConnector.resetTable("table_management");
            this.deletionConnector.resetTable("order_management");
            new MyOptionPane("Data deleted and Auto incrementation reset completed successfully", 1, "Info");
        }
    }
}