package it.restaurant;

import lib.dbComponents.DBConnectionClass;
import lib.dbComponents.DBCreationOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**** Start of the Spring Boot application ****/
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

            DBConnectionClass connector = new DBConnectionClass();

            // 1. Configuri l'URL estratto per il server (es. jdbc:mysql://localhost:3306)
            // Estraiamo la base dell'URL togliendo il nome del db se presente
            String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/"));
            connector.setDbUrl(dbUrl);

            // Usiamo una reflection o un piccolo trick per impostare il currentServerUrl se necessario,
            // ma dato che la tua classe di default punta a localhost:3306, su VPS funzionerà direttamente!

            System.out.println(">>> [INIT] Verifica esistenza database sul server...");
            // Usiamo il metodo della tua libreria per verificare se 'restaurant' esiste
            if (connector.checkServerConnection(this.dbUser, this.dbPassword)) {
                System.out.println(">>> [INIT] Il database 'restaurant' esiste già. Pronto all'uso.");
            } else {
                System.out.println(">>> [INIT] Database NON trovato! Avvio procedura di creazione automatica...");

                // Usiamo la tua classe per creare tutto da zero
                DBCreationOperations creationOperations = new DBCreationOperations(connector);
                try {
                    creationOperations.fullDBCreation(this.dbUser, this.dbPassword);
                    System.out.println(">>> [INIT] Database, tabelle, trigger e dati di default creati con successo!");
                } catch (Exception e) {
                    System.err.println(">>> [INIT] ERRORE critico durante la creazione del database: " + e.getMessage());
                }
            }

            // Chiudiamo eventuali connessioni residue temporanee prima che parta il pool di Spring
            if (connector.returnConnection() != null && !connector.returnConnection().isClosed()) {
                connector.returnConnection().close();
            }
        };
    }
}