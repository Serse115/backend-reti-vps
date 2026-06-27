package it.restaurant.controller;

import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import it.restaurant.dto.OrderItemDTO;
import it.restaurant.dto.OrderRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    public void sendOtpEmail(String to, String otp) {
        Resend resend = new Resend(apiKey);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ristorante <onboarding@resend.dev>")
                .to(to)
                .subject("Il tuo codice OTP per confermare l'ordine")
                // Usa .html() invece di .htmlContent()
                .html("<strong>Il tuo codice di verifica è: " + otp + "</strong> <br>Usalo per confermare sulla pagina dell'ordine. <br>Se non sei stato tu a richiederlo, <strong>ignora</strong> questa email.")
                .build();

        try {
            resend.emails().send(params);
            System.out.println("Email inviata con successo tramite Resend API!");
        } catch (Exception e) {
            System.err.println("Errore Resend: " + e.getMessage());
            throw new RuntimeException("Fallimento invio email API");
        }
    }

    public void sendOrderConfirmationEmail(String to, OrderRequestDTO request) {
        Resend resend = new Resend(apiKey);
        StringBuilder htmlBuilder = new StringBuilder();

        // Uso dello String builder per costruire il messaggio da inserire nell'email
        htmlBuilder.append("<h2>Grazie per aver effettuato l'ordine!</h2>");
        htmlBuilder.append("<p>Riepilogo dati personali: </p>");
        htmlBuilder.append("<p>Nominativo ordine: <strong>").append(request.name).append("</strong> </p>");
        htmlBuilder.append("<p>Indirizzo: <strong>").append(request.address).append("</strong> </p>");
        htmlBuilder.append("<p>Città: <strong>").append(request.city).append("</strong> </p>");
        htmlBuilder.append("<p>CAP: <strong>").append(request.cap).append("</strong> </p>");
        htmlBuilder.append("<p>Telefono: <strong>").append(request.phone).append("</strong> </p>");
        htmlBuilder.append("<p>Riepilogo dei piatti scelti:</p>");
        htmlBuilder.append("<ul>");

        for (OrderItemDTO item : request.items) {
            // Per ogni piatto viene creata una riga della lista (<li>)
            htmlBuilder.append("<li>")
                    .append("Codice Piatto: <strong>").append(item.mealCode).append("</strong> ")
                    .append("- Nome piatto: <strong>").append(item.mealName).append("</strong> ")
                    .append("- Quantità: <strong>").append(item.quantity).append("</strong>")
                    .append("</li>");
        }

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Ristorante <onboarding@resend.dev>")
                .to(to)
                .subject("Riepilogo ordine")
                .html(htmlBuilder.toString())
                .build();
        try {
            resend.emails().send(params);
            System.out.println("Email inviata con successo tramite Resend API!");
        } catch (Exception e) {
            System.err.println("Errore Resend: " + e.getMessage());
            throw new RuntimeException("Fallimento invio email API");
        }
    }
}