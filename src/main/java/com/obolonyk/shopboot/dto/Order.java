package com.obolonyk.shopboot.dto;

import com.obolonyk.shopboot.entity.Product;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {
    private Product product;
    private int quantity;
    private double total;
}
