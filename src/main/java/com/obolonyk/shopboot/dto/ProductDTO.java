package com.obolonyk.shopboot.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductDTO {
    private Integer id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}
