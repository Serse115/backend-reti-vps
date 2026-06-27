package lib.dbComponents;

/******** Interface for the GetAndDownloadData class ********/
public interface GetAndDownloadDataInterface {

    // Methods
    // Get the orders through the table number method
    void getOrdersThroughTableNum(String tableNum, String filePath);
}
