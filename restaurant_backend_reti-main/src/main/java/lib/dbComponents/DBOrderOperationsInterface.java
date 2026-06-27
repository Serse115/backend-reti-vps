package lib.dbComponents;

/******** Interface for the OrderOperations class********/
public interface DBOrderOperationsInterface {

    // Methods
    // Method to retrieve the orders' data from the database
    Object[][] getOrdersData(String sqlQuery);

    // Method to get only the yet-to-serve orders from the database
    Object[][] getToServeOrdersData();

    // Method to get only the chosen table's orders data from the database
    Object[][] getTableOrdersData(String tableNumber);

    // Method to insert a new order into the database
    void insertNewOrder(int tableNum, String mealCode, int amount, String specialReq);

    // Method to retrieve all the meal codes to set them as already served
    Integer[] getOrderNumbers(int tableNum);

    // Method to update the status of the service for the selected order
    void updateStatusService(Integer orderNumber, String updatedStatus);

    // Method to retrieve data from a row to enable the dynamic refresh of the orders table (this time with generic values to work it with both int and char)
    <G> G getDataFromRows(String columnName, int tableNumber, String mealCode, int amount, Class<G> returnType);
}