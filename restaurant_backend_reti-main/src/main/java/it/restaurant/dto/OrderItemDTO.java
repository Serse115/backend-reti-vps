package it.restaurant.dto;

// DATA TRANSFER OBJECT per l'ordine
public class OrderItemDTO {

    public String mealCode;
    public String mealName;
    public int quantity;

    // Costruttore vuoto necessario per Jackson (la libreria che legge i JSON)
    public OrderItemDTO() {

    }
}
