package it.restaurant.dto;

import java.util.List;

// DATA TRANSFER OBJECT for the order itself
public class OrderRequestDTO {

    public String orderCode;
    public String name;
    public String address;
    public String cap;
    public String city;
    public String email;
    public String phone;
    public List<OrderItemDTO> items; // La nostra lista di piatti

    public OrderRequestDTO() {

    }
}
