package it.restaurant.controller;

import it.restaurant.dto.MealDTO;
import it.restaurant.dto.OrderItemDTO;
import it.restaurant.dto.OrderRequestDTO;
import lib.dbComponents.*;
import lib.guiComponents.HighBasicComponents.DeliveryServicePanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**** Class for the controller of the web server events for the orders ****/
@RestController
@RequestMapping("/order_management")
public class OrderController {

    // Recuperiamo le credenziali dal file properties
    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    private final lib.dbComponents.DBConnectionClass connector = new lib.dbComponents.DBConnectionClass();
    // Iniettiamo il servizio email
    @Autowired
    private EmailService emailService;

    // "Sala d'attesa" per gli ordini che aspettano la conferma OTP
    private final Map<String, OrderRequestDTO> pendingOrders = new ConcurrentHashMap<>();


    /**** FROM FRONTEND TO BACKEND ****/
    // Method to request the OTP
    @PostMapping("/request-otp")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> requestOtp(@RequestBody OrderRequestDTO request) {

        this.connector.setDbUrl(dbUrl);

        if (this.connector.connectToDB(dbUser, dbPassword)) {
            DBOTPOperationsInterface otpService = new DBOTPOperations(this.connector);

            // Clear all the previously expired OTPs (Self-cleaning of expired items everytime a new OTP is requested from the frontend)
            otpService.cleanExpiredOTPs();

            // 1. Genera l'OTP
            String code = otpService.generateOTP();

            // 2. Salva l'OTP nel DB (per la scadenza e verifica)
            if (otpService.saveOTP(request.email, code)) {
                // 3. Parcheggia l'ordine in memoria usando l'email come chiave
                pendingOrders.put(request.email, request);

                // 4. TODO: Invia Email reale qui. Per ora stampiamo in console
                // --- CHIAMATA AL SERVIZIO EMAIL ---
                try {
                    emailService.sendOtpEmail(request.email, code);
                    System.out.println(">>> OTP inviato a " + request.email + ": " + code);
                    return ResponseEntity.ok("OTP inviato all'email: " + request.email);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body("Errore nell'invio dell'email.");
                }
            }
            return ResponseEntity.status(500).body("Errore nella generazione dell'OTP.");
        }
        return ResponseEntity.status(503).body("Database non raggiungibile.");
    }


    // Method to submit the order from the front end call to the database
    @PostMapping("/verify-otp-and-submit")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> verifyOtpAndSubmit(@RequestParam("email") String email, @RequestParam("otp") String otp) {

        connector.setDbUrl(dbUrl);

        if (this.connector.connectToDB(dbUser, dbPassword)) {
            DBOTPOperationsInterface otpService = new DBOTPOperations(this.connector);

            // 1. Verifica l'OTP tramite la tua classe (che lo cancella se valido)
            if (otpService.verifyOTP(email, otp)) {

                // 2. Recupera l'ordine parcheggiato
                OrderRequestDTO originalRequest = pendingOrders.get(email);

                if (originalRequest != null) {
                    // 3. Salva l'ordine definitivamente
                    String orderCode = this.executeFinalSubmit(originalRequest);

                    if (orderCode != null) {
                        pendingOrders.remove(email);      // Pulizia
                        return ResponseEntity.ok(orderCode);
                    }
                    return ResponseEntity.status(500).body("Errore durante il salvataggio dell'ordine nel database.");
                }
                return ResponseEntity.status(400).body("Sessione ordine scaduta. Ricomincia il processo.");
            }
            return ResponseEntity.status(401).body("Codice OTP errato o scaduto.");
        }
        return ResponseEntity.status(503).body("Database non raggiungibile.");
    }


    // Method send the resume email after the order confirmation
    @PostMapping("/send-order-resume-email")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> sendOrderResume(@RequestBody OrderRequestDTO request) {

        connector.setDbUrl(dbUrl);

        if (this.connector.connectToDB(dbUser, dbPassword)) {

            try {
                emailService.sendOrderConfirmationEmail(request.email, request);
                System.out.println(">>> Email di conferma e riepilogo inviata a " + request.email);
                return ResponseEntity.ok("Riepilogo di conferma inviato all'email: " + request.email);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Errore nell'invio dell'email.");
            }
        }
        return ResponseEntity.status(503).body("Database non raggiungibile.");
    }



    /**** FROM BACKEND TO FRONTEND ****/
    // Method to retrieve the menu data from the DB and send it to the frontend to display it
    @GetMapping("/menu")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<MealDTO>> getFullMenu() {

        connector.setDbUrl(dbUrl);

        // Qui istanzierai la tua classe di operazioni DB
        if (this.connector.connectToDB(dbUser, dbPassword)) {
            DBMenuOperationsInterface dbMenu = new DBMenuOperations(this.connector);
            List<MealDTO> menu = dbMenu.getMenuData(1);

            return ResponseEntity.ok(menu);

        } else {
            System.out.println("DB NON CONNESSO!");
            return null;
        }
    }


    /**** HELPERS ****/
    // Metodo helper privato che contiene la tua vecchia logica di salvataggio
    private String executeFinalSubmit(OrderRequestDTO request) {
        DBDeliveryOperationsInterface dbDelivery = new DBDeliveryOperations(this.connector);
        final String orderCode = this.createTheOrderCode();

        // 1. Salviamo l'anagrafica usando i campi dell'oggetto DTO
        boolean infoSaved = dbDelivery.insertDeliveryOrderDataInfo(
                orderCode,
                request.name,
                request.address,
                request.cap,
                request.city,
                request.email,
                request.phone,
                "awaiting"
        );

        if (infoSaved) {
            // 2. Convertiamo la lista di DTO nel formato richiesto dal tuo metodo DB (List<Object[]>)
            List<Object[]> itemsForDb = new ArrayList<>();
            for (OrderItemDTO item : request.items) {
                itemsForDb.add(new Object[]{item.mealCode, item.quantity});
            }
            if (dbDelivery.insertDeliveryOrderList(orderCode, itemsForDb)) {
                return orderCode;
            }
        }
        return null;
    }

    // Sub method to create the order code to insert into the DB with the order data taken from the frontend
    private String createTheOrderCode() {

        // Using the java security random class to generate random numbers
        java.security.SecureRandom random = new java.security.SecureRandom();

        // List of characters that can be used for the generation of the order code
        final String charactersToUse = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        StringBuilder sb = new StringBuilder(8);                    // String builder object to form the string with the characters

        // Loop to choose from the list of characters to use and form an array of 8 characters
        for (int i = 0; i < 8; i++) {
            // Append the char obtained randomly from the random choice from the list of chars to pick
            sb.append(charactersToUse.charAt(random.nextInt(charactersToUse.length())));
        }

        return sb.toString();       // Return the string obtained as code
    }



















    @GetMapping("/today")
    public ResponseEntity<?> getTodayOrders() {

        //lib.dbComponents.DBConnectionClass connector = new lib.dbComponents.DBConnectionClass();

        // Inserisci qui le tue credenziali reali
        if (this.connector.connectToDB(this.dbUser, this.dbPassword)) {
            try (Connection conn = this.connector.returnConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT meal_code FROM order_management")) {

                List<String> results = new ArrayList<>();
                while (rs.next()) {
                    results.add(rs.getString("meal_code"));
                }
                return ResponseEntity.ok(results);

            } catch (SQLException e) {
                return ResponseEntity.status(500).body("Errore query: " + e.getMessage());
            }
        } else {
            return ResponseEntity.status(401).body("Impossibile stabilire una connessione al DB");
        }
    }

    @PostMapping("/submit")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> submitOrder(@RequestBody OrderRequestDTO request) {

        if (this.connector.connectToDB(dbUser, dbPassword)) {

            DBDeliveryOperationsInterface dbDelivery = new DBDeliveryOperations(this.connector);
            final String orderCode = this.createTheOrderCode();

            // 1. Salviamo l'anagrafica usando i campi dell'oggetto DTO
            boolean infoSaved = dbDelivery.insertDeliveryOrderDataInfo(
                    orderCode,
                    request.name,
                    request.address,
                    request.cap,
                    request.city,
                    request.email,
                    request.phone,
                    "awaiting" // Stato iniziale fisso
            );

            if (infoSaved) {
                // 2. Convertiamo la lista di DTO nel formato richiesto dal tuo metodo DB (List<Object[]>)
                // Oppure potresti aggiornare il metodo DB per accettare direttamente la lista di DTO!
                List<Object[]> itemsForDb = new ArrayList<>();
                for (OrderItemDTO item : request.items) {
                    itemsForDb.add(new Object[]{item.mealCode, item.quantity});
                }

                boolean listSaved = dbDelivery.insertDeliveryOrderList(orderCode, itemsForDb);

                if (listSaved) {
                    return ResponseEntity.ok(orderCode);
                }
            }

            return ResponseEntity.status(500).body("Errore nel salvataggio dell'ordine");

        } else {
            System.out.println("DB NON CONNESSO!");
            return null;
        }
    }
}
