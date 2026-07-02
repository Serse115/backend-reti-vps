package it.restaurant;

import lib.dbComponents.DBConnectionClass;
import lib.dbComponents.DBCreationOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RestaurantApplication {

    // Importiamo le credenziali direttamente dalle variabili d'ambiente di Spring
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
            //String fullDbUrl = "jdbc:mysql://127.0.0.1:3306/restaurant";
            //String serverUrl = "jdbc:mysql://127.0.0.1:3306/";

            String fullDbUrl = "jdbc:mysql://127.0.0.1:3306/restaurant?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&protocol=tcp";
            String serverUrl = "jdbc:mysql://127.0.0.1:3306/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&protocol=tcp";

            // 1. Tentiamo la connessione diretta al database 'restaurant'
            connector.setDbUrl(fullDbUrl);

            if (connector.connectToDB(dbUser, dbPassword)) {
                System.out.println(">>> [INIT] Connessione riuscita. Il database 'restaurant' è pronto e operativo.");
                connector.closeConnectionToDB();
            } else {
                System.out.println(">>> [INIT] Il database non esiste. Procedo con l'installazione automatica (Web Mode)...");

                // 2. Il DB non esiste, quindi ci connettiamo al server base vuoto
                connector.setDbUrl(serverUrl);
                if (connector.connectToDB(dbUser, dbPassword)) {
                    System.out.println(">>> [INIT] Connesso al server MySQL base. Avvio script di creazione...");

                    try {
                        // 3. Invochiamo la TUA classe nativa per creare DB, tabelle, trigger e dati
                        DBCreationOperations creationOperations = new DBCreationOperations(connector);
                        creationOperations.fullDBCreation(dbUser, dbPassword);

                        System.out.println(">>> [INIT] Setup completato! Database 'restaurant', tabelle e dati generati con successo.");
                    } catch (Exception e) {
                        System.err.println(">>> [INIT] Errore critico durante l'esecuzione di fullDBCreation: " + e.getMessage());
                    } finally {
                        connector.closeConnectionToDB();
                    }
                } else {
                    System.err.println(">>> [INIT] ERRORE CRITICO: Impossibile connettersi al server MySQL base. Controlla che le credenziali siano corrette e che MySQL sia attivo.");
                }
            }
        };
    }
}