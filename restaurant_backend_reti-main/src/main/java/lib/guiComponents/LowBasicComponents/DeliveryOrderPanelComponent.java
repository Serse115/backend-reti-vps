package lib.guiComponents.LowBasicComponents;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**** Class for the delivery order panel that is formed by a table with data and a label with the order code *****/
public class DeliveryOrderPanelComponent extends JPanel {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor
    public DeliveryOrderPanelComponent(String orderCode, Object[][] ordersData, String orderStatus) {
        super.setLayout(new BorderLayout());
        super.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        super.setBackground(Color.WHITE);

        JLabel orderTabHeader = new JLabel("Order Code: " + orderCode + " - Status: " + orderStatus);
        orderTabHeader.setFont(orderTabHeader.getFont().deriveFont(Font.BOLD));
        orderTabHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        orderTabHeader.setOpaque(true); // Necessario per vedere il colore di sfondo

        switch (orderStatus) {
            case "awaiting":
                orderTabHeader.setBackground(new Color(214, 5, 5));         // Red
                break;
            case "ongoing":
                orderTabHeader.setBackground(new Color(230, 120, 9));       // Yellow
                break;
            case "ready":
                orderTabHeader.setBackground(new Color(5, 156, 85));        // Green
                break;
            default:
                orderTabHeader.setBackground(new Color(200, 200, 255));     // Default blueish
                break;
        }
        //orderTabHeader.setBackground(new Color(200, 200, 255));

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Meal Code", "Meal Name", "Quantity"});

        for (Object[] row : ordersData) {
            tableModel.addRow(row);
        }

        JTable orderTable = new JTable(tableModel);

        // --- TRUCCO PER L'ALTEZZA ---
        // Calcoliamo l'altezza necessaria: (numero righe * altezza riga) + altezza header
        int rowCount = orderTable.getRowCount();
        int rowHeight = orderTable.getRowHeight();
        int headerHeight = orderTable.getTableHeader().getPreferredSize().height;
        int totalHeight = (rowCount * rowHeight) + headerHeight;

        JScrollPane tableScroll = new JScrollPane(orderTable);
        // Impediamo che la tabella diventi troppo alta o troppo piccola
        // 200px è un buon limite, ma si adatterà se il contenuto è minore
        tableScroll.setPreferredSize(new Dimension(tableScroll.getPreferredSize().width, Math.min(totalHeight + 5, 200)));

        super.add(orderTabHeader, BorderLayout.NORTH);
        super.add(tableScroll, BorderLayout.CENTER);
    }


    /**** Methods ****/
    // Empty
    // new Color(200, 200, 255)
}
