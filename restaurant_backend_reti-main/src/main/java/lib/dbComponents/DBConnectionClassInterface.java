package lib.dbComponents;

import java.sql.Connection;

/******** Interface for the ConnectionClass class ********/
public interface DBConnectionClassInterface {

    // Methods
    // Connect to the DB method
    boolean connectToDB(String user, String password);

    // Get the connection method
    Connection returnConnection();

    // Close the connection to the DB method
    void closeConnectionToDB();

    // Connect to the MySQL server to check the DB existence
    boolean checkServerConnection(String user, String password);
}