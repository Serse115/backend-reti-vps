import lib.dbComponents.DBConnectionClass;
import lib.dbComponents.DBConnectionClassInterface;
import lib.guiComponents.HighBasicComponents.DatabaseConnectionPage;

/* Starting class App */
public class App {
    public static void main(String[] args) {
        
        // Starting the application
        final DBConnectionClassInterface connector = new DBConnectionClass();   // Creating the connector object
        new DatabaseConnectionPage(connector);                                  // Passing the connector object to connect to the Database and start the application
    }
}