package lib.dbComponents;

import lib.guiComponents.LowBasicComponents.MyOptionPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Objects;

/******** CLASS FOR DATABASE CREATION OPERATIONS ********/
public class GetAndDownloadData implements GetAndDownloadDataInterface {

    /**** Fields ****/
    // Variables
    private final DBConnectionClassInterface connector;           // Connector object
    private final DBOrderOperationsInterface ordersOperations;    // OrdersOperation object
    private final DBMenuOperationsInterface menuOperations;       // MenuOperations object



    /**** Constructors ****/
    // Main constructor
    public GetAndDownloadData(final DBConnectionClassInterface connector) {
        this.connector = connector;
        this.ordersOperations = new DBOrderOperations(this.connector);
        this.menuOperations = new DBMenuOperations(this.connector);
    }



    /**** Methods ****/
    // Method to get the orders for the table number specified and return it as a file of data
    public void getOrdersThroughTableNum(String tableNum, String filePath) {

        Object[][] data = this.ordersOperations.getTableOrdersData(tableNum);       // Inserting the data from the ordersOperation's object method into a 2D array of data
        this.createTXTForData(tableNum, filePath, data);                            // Passing the data to the sub method to create the txt
    }

    // Sub-method to write the data on a txt file
    private void createTXTForData(String tableNum, String filePath, Object[][] data) {

        // JFileChooser needed to choose where to save
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose where to save the file");

        // ADDITION -> ADDED THE LOGO TO THE 'FOLDER TO SAVE THE DATA IN' PANEL
        // Listens for when the dialog is created and becomes visible
        fileChooser.addPropertyChangeListener("ancestor", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() != null) {                // Check if the file chooser has been added to a container (meaning its dialog is open)

                    JDialog dialog = (JDialog) SwingUtilities.getWindowAncestor(fileChooser);         // Get the parent dialog of the JFileChooser
                    if (dialog != null) {

                        ImageIcon customIcon = new ImageIcon(                                          // Creating the custom image icon
                                Objects.requireNonNull(GetAndDownloadData.class.getResource("/ForkKnifeLogo.png"))
                        );
                        dialog.setIconImage(customIcon.getImage());                                     // Set the icon image of the dialog
                        fileChooser.removePropertyChangeListener("ancestor", this);  // Remove the listener after setting the icon
                    }
                }
            }
        });

        // Filter for extensions txt
        fileChooser.setFileFilter(new FileNameExtensionFilter("File di testo (*.txt)", "txt"));

        // Set the name for the new file with the number of the table chosen
        fileChooser.setSelectedFile(new File("Ordini_Tavolo_" + tableNum + ".txt"));

        int userSelection = fileChooser.showSaveDialog(null);

        // Get the selected file to save it
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // If the txt extension is not selected by the user, add it by default
            if (!fileToSave.getAbsolutePath().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            // Write the file with the data and the number of the table chosen
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write("===== ORDINI TAVOLO " + tableNum + " =====\n\n");

                for (Object[] row : data) {                                 // For each data in each row
                    String mealCode = (String) row[2];                      // Get the meal code
                    String mealName = this.menuOperations.getMealNameByCode(mealCode);      // Get the meal name from the sub method passing the meal code to it

                    writer.write("Ordine n°: " + row[0] + "\n");
                    writer.write("Piatto (meal_code): " + mealCode + "\n");
                    writer.write("Nome piatto: " + (mealName != null ? mealName : "Nome non trovato") + "\n");
                    writer.write("Quantità: " + row[3] + "\n");
                    writer.write("Richieste speciali: " + (row[4] != null ? row[4] : "Nessuna") + "\n");
                    writer.write("Servizio: " + row[5] + "\n");
                    writer.write("---------------------------------\n");
                }

                new MyOptionPane("TXT correctly exported in " + fileToSave.getAbsolutePath(), 1, "Info");

            } catch (Exception e) {
                new MyOptionPane("TXT not correctly exported: " + e.getMessage(), 0, "Errore");
            }
        } else {
            new MyOptionPane("Export canceled!", 1, "Info");
        }
    }
}