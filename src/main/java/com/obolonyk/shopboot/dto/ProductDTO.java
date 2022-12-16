package com.obolonyk.shopboot.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Integer id;
    private String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}
