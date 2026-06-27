package lib.guiComponents.HighBasicComponents;

import java.awt.*;
import java.util.Comparator;
import javax.swing.*;

import lib.dbComponents.*;
import lib.guiComponents.LowBasicComponents.MyOptionPane;
import lib.guiComponents.LowBasicComponents.OrdersToolBar;
import lib.guiComponents.LowBasicComponents.DeliveryOrderPanelComponent;

/******** Delivery service panel class ********/
public class DeliveryServicePanel extends JPanel {

    /**** Fields ****/
    private final JPanel ordersContainer;                                       // Orders container panel
    private final DBDeliveryOperationsInterface deliveryOrdersConnector;        // Delivery orders connector object
    private String[][] listOfOrderCodes;                                        // List of strings with the order codes
    private int lastOrderCount;                                             // Variable to count the number of orders present in the DB


    /**** Constructors ****/
    // Main constructor
    public DeliveryServicePanel(final DBConnectionClassInterface conn) {

        // Setting the panel
        super.setBackground(new Color(238, 238, 238));
        super.setOpaque(true);
        super.setLayout(new BorderLayout());

        // Initializing the orders operations connection object
        this.deliveryOrdersConnector = new DBDeliveryOperations(conn);

        this.lastOrderCount = this.deliveryOrdersConnector.countOrders(); // Inizializzazione silenziosa

        // Initializing the timer for the smart refresh
        Timer timerToRefresh = new Timer(5000, e -> {
            int currentCount = this.deliveryOrdersConnector.countOrders(); // Dovresti creare questo metodo nel connettore

            if (currentCount > lastOrderCount) {
                lastOrderCount = currentCount;
                refreshPanel();

                // Popup translucido che dura 2 secondi (2000 ms)
                new MyOptionPane("New orders found!", 1, "Notifica", 2500);
            }
            else if (currentCount < lastOrderCount) {
                lastOrderCount = currentCount;
                refreshPanel();
            }
        });

        // Avviamo il timer
        timerToRefresh.start();

        // Initializing the orders container panel
        this.ordersContainer = new JPanel();
        ordersContainer.setLayout(new GridBagLayout()); // Usiamo GridBagLayout
        ordersContainer.setBackground(new Color(238, 238, 238));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margine tra i pannelli
        gbc.fill = GridBagConstraints.HORIZONTAL; // Si allarga solo in orizzontale
        gbc.weightx = 1.0; // Distribuisce lo spazio orizzontale equamente
        gbc.anchor = GridBagConstraints.NORTH; // Spinge i componenti verso l'alto

        // Retrieve orders codes and statuses
        this.listOfOrderCodes = deliveryOrdersConnector.getTheDeliveryOrderCodesAndStatus();

        // Reorder the orders data according to the priority
        this.sortOrdersByPriority(listOfOrderCodes);

        for (int i = 0; i < listOfOrderCodes.length; i++) {
            String orderCode = listOfOrderCodes[i][0];
            String orderStatus = listOfOrderCodes[i][1];
            Object[][] ordersData = deliveryOrdersConnector.getTheDeliveryOrderData(orderCode);

            DeliveryOrderPanelComponent orderComp = new DeliveryOrderPanelComponent(orderCode, ordersData, orderStatus);

            // Gestione delle 2 colonne (0 e 1)
            gbc.gridx = i % 2; // Colonna: 0 per pari, 1 per dispari
            gbc.gridy = i / 2; // Riga: aumenta ogni due elementi

            // TRUCCO: Per l'ultimo elemento, se vogliamo che non si "allunghi"
            // nell'intero spazio vuoto rimasto in fondo:
            if (i == listOfOrderCodes.length - 1) {
                gbc.weighty = 1.0; // Solo l'ultimo si prende il "peso" verticale residuo
            } else {
                gbc.weighty = 0;
            }

            ordersContainer.add(orderComp, gbc);
        }

        // Wrapping in un pannello "Nord" per evitare che tutto il blocco si centri verticalmente
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(ordersContainer, BorderLayout.NORTH);
        wrapper.setBackground(new Color(238, 238, 238));

        JScrollPane scrollPane = new JScrollPane(wrapper);

        // Setting the toolbar for more options
        OrdersToolBar toolBar = new OrdersToolBar(conn, 1, this);

        // Add the scroll pane to the panel
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(toolBar, BorderLayout.PAGE_END);
    }


    /**** Methods ****/
    // Sub-method to reorder the 2d array according to the priorities
    private void sortOrdersByPriority(String[][] orders) {
        java.util.Arrays.sort(orders, Comparator.comparingInt(a -> getPriority(a[1])));
    }

    // Sub-method to decide the priorities of the orders
    private int getPriority(String toEvaluate) {
        if (toEvaluate == null) {
            return 4;
        }
        switch (toEvaluate.toLowerCase()) {
            case "ongoing":
                return 1;
            case "awaiting":
                return 2;
            case "ready":
                return 3;
            default:
                return 4;
        }
    }

    // Refreshing method
    public void refreshPanel() {

        // Removing the old components
        this.ordersContainer.removeAll();

        // Getting new data from the DB and filling the list of order codes with it
        this.listOfOrderCodes = this.deliveryOrdersConnector.getTheDeliveryOrderCodesAndStatus();

        // Reorder the data
        this.sortOrdersByPriority(this.listOfOrderCodes);

        // Reset constraints for the GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.NORTH;

        // Loop to insert the data into the single table components contained into the delivery order panel components
        for (int i = 0; i < this.listOfOrderCodes.length; i++) {
            String orderCode = this.listOfOrderCodes[i][0];
            String orderStatus = this.listOfOrderCodes[i][1];

            Object[][] ordersData = this.deliveryOrdersConnector.getTheDeliveryOrderData(orderCode);
            DeliveryOrderPanelComponent orderComp = new DeliveryOrderPanelComponent(orderCode, ordersData, orderStatus);

            // Graphics
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;

            if (i == this.listOfOrderCodes.length - 1) {
                gbc.weighty = 1.0;
            } else {
                gbc.weighty = 0;
            }

            this.ordersContainer.add(orderComp, gbc);
        }

        // Revalidate and repaint the orders container panel
        this.ordersContainer.revalidate();
        this.ordersContainer.repaint();

        // Revalidate and repaint the main panel itself
        this.revalidate();
        this.repaint();
    }
}