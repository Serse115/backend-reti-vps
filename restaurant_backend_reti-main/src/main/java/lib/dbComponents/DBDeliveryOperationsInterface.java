package lib.dbComponents;

import java.util.List;

/******** Interface for the Delivery operations ********/
public interface DBDeliveryOperationsInterface {

    // Methods
    // Method to get the codes of all the delivery orders
    Object[][] getTheDeliveryOrderCodesAndTheirInfo();

    // Method to get all the orders data plus the current status from the orders info table
    Object[][] getAllTheDeliveryOrderDataWithStatus();

    // Overloading for the method to get the data useful for the order making for the chosen order code
    Object[][] getTheDeliveryOrderData(String order_code);

    // Method to get all the delivery order codes
    String[][] getTheDeliveryOrderCodesAndStatus();

    // Method to get all the order codes available for the status change
    String[] getTheDeliveryOrderCodes();

    // Method to update the status of the service for the selected order
    void updateStatusService(final String orderCode, final String updatedStatus);

    // Method to delete the chosen delivery order
    void deleteDeliveryOrder(final String deliveryOrderCode);

    // Method to insert the delivery order data info
    boolean insertDeliveryOrderDataInfo(final String orderCode, final String orderName, final String address,
                                        final String cap, final String city, final String email, final String phone,
                                        final String orderStatus);

    // Method to insert the orders list
    boolean insertDeliveryOrderList(String orderCode, List<Object[]> items);

    // Method to count the number of orders in the DB
    int countOrders();

}
