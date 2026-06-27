package lib.guiComponents.LowBasicComponents;

import lib.dbComponents.DBConnectionClassInterface;
import lib.guiComponents.HighBasicComponents.DeliveryServicePanel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/******** Orders toolbar class ********/
public class OrdersToolBar extends JPanel implements ActionListener {

    /**** Fields ****/
    // Variables
    private final JButton showAllOrdersButton;              // Button to show all orders (orders panel)
    private final JButton changeOrderStatusButton;          // Button to change the status of a delivery order (delivery orders panel)
    private final JButton checkAllDeliveryOrdersInfoButton; // Button to check all the delivery orders information (delivery orders panel)
    private final JButton deleteDeliveryOrderButton;        // Button to delete a delivery order (delivery orders panel)
    private final DBConnectionClassInterface connector;     // Connector object
    private final int ordersToolTag;                        // Tag to separate the normal orders toolbar from the delivery orders toolbar
    private final DeliveryServicePanel panelToRefresh;      // Panel to refresh reference


    /**** Constructors ****/
    // Main constructor for the orders panel functionality
    public OrdersToolBar(final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setBackground(Color.WHITE);

        // Initializing the connection class object
        this.connector = conn;

        // Initializing the orders tool tag
        this.ordersToolTag = 0;

        // Initializing the reference panel (NOT USED HERE)
        this.panelToRefresh = null;

        // Defining the button to show all orders
        this.showAllOrdersButton = new JButton("All orders");
        this.showAllOrdersButton.setFocusable(false);
        this.showAllOrdersButton.addActionListener(this);

        // Defining the button to change the status of an order (NOT USED HERE, JUST TO AVOID ERROR)
        this.changeOrderStatusButton = new JButton();

        // Defining the button to check all the info for the orders (NOT USED HERE, JUST TO AVOID ERROR)
        this.checkAllDeliveryOrdersInfoButton = new JButton();

        // Defining the button to delete a delivery order button (NOT USED HERE, JUST TO AVOID ERROR)
        this.deleteDeliveryOrderButton = new JButton();

        // Adding the components
        super.add(this.showAllOrdersButton);
    }

    // Secondary constructor for the delivery orders panel functionality
    public OrdersToolBar(final DBConnectionClassInterface conn, final int deliveryTag, final DeliveryServicePanel panelToRefresh) {

        // Setting the panel
        super.setBackground(Color.WHITE);

        // Initializing the connection class object
        this.connector = conn;

        // Initializing the orders tool tag
        this.ordersToolTag = deliveryTag;

        // Initializing the reference panel (NOT USED HERE)
        this.panelToRefresh = panelToRefresh;

        // Defining the button to show all orders
        this.showAllOrdersButton = new JButton("All delivery orders");
        this.showAllOrdersButton.setFocusable(false);
        this.showAllOrdersButton.addActionListener(this);

        // Defining the button to change the status of an order
        this.changeOrderStatusButton = new JButton("Change order status");
        this.changeOrderStatusButton.setFocusable(false);
        this.changeOrderStatusButton.addActionListener(this);

        // Defining the button to check all the info for the orders
        this.checkAllDeliveryOrdersInfoButton = new JButton("Check orders information");
        this.checkAllDeliveryOrdersInfoButton.setFocusable(false);
        this.checkAllDeliveryOrdersInfoButton.addActionListener(this);

        // Defining the button to delete a delivery order button (NOT USED HERE, JUST TO AVOID ERROR)
        this.deleteDeliveryOrderButton = new JButton("Delete delivery order");
        this.deleteDeliveryOrderButton.setFocusable(false);
        this.deleteDeliveryOrderButton.addActionListener(this);

        // Adding the components
        super.add(this.showAllOrdersButton);
        super.add(this.changeOrderStatusButton);
        super.add(this.checkAllDeliveryOrdersInfoButton);
        super.add(this.deleteDeliveryOrderButton);
    }


    /**** Methods ****/
    // Action listener method
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.showAllOrdersButton) {                                        // If the button to show all the orders is pressed
            if (this.ordersToolTag == 0) {
                // Retrieve all the orders from the DB and shows them in a new frame
                new ShowAllOrdersFrame("""
                                                SELECT L.*, M.name FROM order_management AS L, menu AS M
                                                WHERE L.meal_code = M.meal_code
                                                """, this.connector);
            }
            else if (this.ordersToolTag == 1) {
                // Retrieve all the orders from the DB and shows them in a new frame
                new ShowAllOrdersFrame(this.connector, this.ordersToolTag);
            }
        }
        else if (e.getSource() == this.checkAllDeliveryOrdersInfoButton) {

            new ShowAllDeliveryOrdersInfo(this.connector);
        }
        else if (e.getSource() == this.changeOrderStatusButton) {

            new ChangeDeliveryOrderStatus(this.connector, this.panelToRefresh);
        }
        else if (e.getSource() == this.deleteDeliveryOrderButton) {

            new DeleteDeliveryOrderFrame(this.connector, this.panelToRefresh);
        }
    }
}