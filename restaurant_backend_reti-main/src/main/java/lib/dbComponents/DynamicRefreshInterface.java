package lib.dbComponents;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/******** Interface for the Dynamic refresh operations (Default methods) ********/
public interface DynamicRefreshInterface {

    // Default methods
    // Add method to add the row of data in the needed tables (acts as a dynamic "refresh" of the current panel view by adding the new value)
    default void addData(Object[] rowData, JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(rowData);
    }

    // Find the chosen row's index (used to then remove that row)
    default int findDataRowIndex(JTable model, String data, int columnIndex) {
        for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
            if (data.equals(model.getValueAt(rowIndex, columnIndex))) {
                return rowIndex;
            }
        }
        return -1;
    }

    // Remove method to remove the row of data in the needed tables (acts as a dynamic "refresh" of the current panel view by removing the required value)
    default void removeData(int rowIndex, JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.removeRow(rowIndex);
    }

    // Return a panel
    default JPanel returnPanel(JPanel panel) {
        return panel;
    }
}