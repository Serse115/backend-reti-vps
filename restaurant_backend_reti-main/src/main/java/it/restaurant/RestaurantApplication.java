package it.restaurant;

import lib.dbComponents.DBConnectionClass;
import lib.dbComponents.DBCreationOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class RestaurantApplication {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUser;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    public static void main(String[] args) {
        SpringApplication.run(RestaurantApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            System.out.println(">>> [INIT] Avvio controllo consistenza Database...");

            // Ricaviamo l'URL del server (es. jdbc:mysql://localhost:3306/) mantenendo i parametri di timeZone e SSL
            String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/")) + "/";
            if (dbUrl.contains("?")) {
                String queryParams = dbUrl.substring(dbUrl.indexOf("?"));
                serverUrl += queryParams;
            }

            boolean dbExists = false;

            System.out.println(">>> [INIT] Connessione al server MySQL: " + serverUrl);

            // Facciamo un check manuale ed esplicito senza rischiare il System.exit(0)
            try (Connection conn = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = conn.getMetaData().getCatalogs()) {

                while (rs.next()) {
                    if ("restaurant".equalsIgnoreCase(rs.getString(1))) {
                        dbExists = true;
                        break;
                    }
                }

                if (dbExists) {
                    System.out.println(">>> [INIT] Il database 'restaurant' esiste già. Pronto all'uso.");
                } else {
                    System.out.println(">>> [INIT] Database NON trovato! Avvio procedura di creazione automatica...");

                    // Creiamo il database direttamente da qui per essere sicuri al 100%
                    stmt.executeUpdate("CREATE DATABASE restaurant");
                    System.out.println(">>> [INIT] Database 'restaurant' creato. Generazione tabelle e trigger...");

                    // Ora che il DB esiste sul server, usiamo la tua classe nativa per completare il lavoro!
                    DBConnectionClass connector = new MyVpsConnector(dbUrl, conn);
                    DBCreationOperations creationOperations = new DBCreationOperations(connector);

                    // Eseguiamo la creazione delle tabelle, trigger e popolamento dati
                    creationOperations.fullDBCreation(this.dbUser, this.dbPassword);
                    System.out.println(">>> [INIT] Sincronizzazione tabelle e dati di default completata con successo!");
                }

            } catch (Exception e) {
                System.err.println(">>> [INIT] Errore durante l'inizializzazione: " + e.getMessage());
                System.out.println(">>> [INIT] Nota: Se il DB è già stato parzialmente creato, Spring Boot proverà ad avviarsi comunque.");
            }
        };
    }

    // Piccola classe di supporto interna per aggirare i limiti del costruttore di DBConnectionClass
    private static class MyVpsConnector extends DBConnectionClass {
        private final Connection activeConn;
        public MyVpsConnector(String url, Connection conn) {
            super();
            this.setDbUrl(url);
            this.activeConn = conn;
        }
        @Override
        public Connection returnConnection() {
            try {
                // Se la connessione al server è chiusa o nulla, restituisce una nuova verso il DB specifico
                if (activeConn == null || activeConn.isClosed()) {
                    return super.returnConnection();
                }
            } catch (Exception e) {}
            return activeConn;
        }
    }
}