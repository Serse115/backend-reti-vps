package it.restaurant;

import lib.dbComponents.DBConnectionClass;
import lib.dbComponents.DBCreationOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@SpringBootApplication
public class RestaurantApplication {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Autowired
    private DataSource dataSource; // Lasciamo fare il lavoro sporco a Spring!

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            System.out.println(">>> [INIT] Avvio controllo del Database tramite DataSource...");

            try (Connection conn = dataSource.getConnection()) {
                // Se arriva qui, Spring è riuscito a connettersi al DB 'restaurant' perché esiste già!
                System.out.println(">>> [INIT] Connessione riuscita. Il database 'restaurant' è pronto.");
            } catch (Exception e) {
                // Se fallisce, molto probabilmente è perché il DB 'restaurant' non esiste ancora.
                System.out.println(">>> [INIT] Il database non esiste o non è raggiungibile. Tento la creazione automatica...");

                // Ricaviamo l'URL generico del server per la creazione guidata
                String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";

                try {
                    // Creiamo una connessione temporanea al server MySQL generico
                    java.sql.Connection rawConn = java.sql.DriverManager.getConnection(serverUrl, dbUser, dbPassword);
                    Statement stmt = rawConn.createStatement();

                    // Creiamo il database
                    stmt.executeUpdate("CREATE DATABASE restaurant");
                    System.out.println(">>> [INIT] Database 'restaurant' creato sul server!");

                    // Usiamo i tuoi componenti nativi per iniettare tabelle, trigger e dati
                    DBConnectionClass connector = new DBConnectionClass();
                    connector.setDbUrl(dbUrl);

                    // Forziamo una connessione pulita con le credenziali verificate
                    if (connector.connectToDB(dbUser, dbPassword)) {
                        DBCreationOperations creationOperations = new DBCreationOperations(connector);
                        creationOperations.fullDBCreation(dbUser, dbPassword);
                        System.out.println(">>> [INIT] Tabelle strutturate e dati popolati con successo!");
                    }

                    stmt.close();
                    rawConn.close();

                } catch (Exception ex) {
                    System.err.println(">>> [INIT] Errore critico durante la creazione guidata: " + ex.getMessage());
                    System.err.println(">>> [INIT] Se vedi errori sulle tabelle esistenti, ignora pure questo messaggio.");
                }
            }
        };
    }
}