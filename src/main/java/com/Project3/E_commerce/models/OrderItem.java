package com.Project3.E_commerce.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private Integer quantity;
    private Double priceAtPurchase;
    private Double subtotal;
}
