package lib.guiComponents.LowBasicComponents;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lib.dbComponents.DBConnectionClassInterface;
import lib.dbComponents.DBReservationOperations;
import lib.dbComponents.DBReservationOperationsInterface;
import lib.guiComponents.HighBasicComponents.HomePage;

/******** Frame for all the reservations class ********/
public class AllReservationsFrame extends JFrame {

    /**** Fields ****/
    // Empty


    /**** Constructors ****/
    // Main constructor
    public AllReservationsFrame(final DBConnectionClassInterface conn) {

        // Setting the Reservations' panel info
        super("All reservations");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Logo for the frame
        // Getting the ImageIcon logo's path
        ImageIcon logo = new ImageIcon(
                Objects.requireNonNull(HomePage.class.getResource("/ForkKnifeLogo.png"))
        );
        Image appLogo = logo.getImage();
        super.setIconImage(appLogo);               // Setting the image as the application's logo

        // Initializing the reservations operations connector
        DBReservationOperationsInterface reservationsOperationsConnector = new DBReservationOperations(conn);
        
        // Create a DefaultTableModel to hold the table data
        DefaultTableModel tableModel = new DefaultTableModel();
        
        // Set the column names for the table
        String[] columnNames = {"Reservation Number", "Reservation Holder Name", "Number of Seats", "Date"};
        tableModel.setColumnIdentifiers(columnNames);
        
        // Retrieve reservation data from the database
        Object[][] reservationData = reservationsOperationsConnector.getReservationData();
        
        // Add the reservation data to the table model
        for (Object[] rowData : reservationData) {
            tableModel.addRow(rowData);
        }
        
        // Create the JTable using the table model for the reservations
        JTable reservationTable = new JTable(tableModel);
        
        // Create a JScrollPane to wrap the JTable
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        
        // Create the main panel and set its layout to BorderLayout for the frame
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Add the scroll pane to the CENTER position of the main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add the main panel to the frame
        super.add(mainPanel);
        
        // Pack the frame to adjust its size based on the components
        super.pack();
        
        // Set the visibility of the frame
        super.setVisible(true);
    }


    /**** Methods ****/
    // Empty
}