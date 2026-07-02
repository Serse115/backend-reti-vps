package lib.guiComponents.HighBasicComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBReservationOperations;
import lib.dbComponents.DBReservationOperationsInterface;
import lib.dbComponents.DynamicRefreshInterface;
import lib.guiComponents.LowBasicComponents.ReservationToolBar;

/******** Reservation panel class ********/
public class ReservationPanel extends JPanel implements DynamicRefreshInterface {

    /**** Fields ****/
    // Variables
    private final JTable reservationTable;                                  // Table for the reservations


    /**** Constructors ****/
    // Main constructor
    public ReservationPanel(final DBConnectionClassInterface conn) {

        // Setting the Reservations' panel info
        super.setBackground(new Color(238, 238, 238));                        // Setting the panel's background color
        super.setOpaque(true);                                                        // Making the background visible
        super.setLayout(new BorderLayout());

        // Initializing the reservations operations connector object
        DBReservationOperationsInterface reservationOperationsConnector = new DBReservationOperations(conn);

        // Defining the new toolBar for more options
        ReservationToolBar toolbar = new ReservationToolBar(conn, (ReservationPanel) this.returnPanel(this));

        // Field for the table model type
        DefaultTableModel tableModel = new DefaultTableModel();

        // Set the column names for the table
        String[] columnNames = {"Reservation Number", "Reservation Holder Name", "Date", "Number of Seats", "Table Number"};
        tableModel.setColumnIdentifiers(columnNames);

        // Retrieve reservation data from the database
        Object[][] reservationData = reservationOperationsConnector.getReservationOnCurrentData();

        // Add the reservation data to the table model
        for (Object[] rowData : reservationData) {
            tableModel.addRow(rowData);
        }

        // Initializing the "reservations" table using the table model
        this.reservationTable = new JTable(tableModel);

        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(reservationTable);

        // Add the scroll pane to the panel
        super.add(scrollPane, BorderLayout.CENTER);
        super.add(toolbar, BorderLayout.PAGE_END);
    }


    /**** Methods ****/
    // Return the reservation panel's table
    public JTable returnPanelTable() {
        return this.reservationTable;
    }
}