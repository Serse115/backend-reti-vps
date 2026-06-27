package lib.dbComponents;

/******** Interface for the WaiterOperations class ********/
public interface DBWaiterOperationsInterface {

    // Methods
    // Method to retrieve all the waiter codes to assign reservations to them
    String[] getWaiterCodes();

    // Method to assign a waiter to a reservation
    void assignWaiterToReservation(String selectedReservation, String selectedWaiterCode);

    // Get the list of tables
    String[] getTheTableCodes(String waiterCode);
}