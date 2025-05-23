package fr.epsi.service.product.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private Float price;
    private String description;
    private String color;
    private Integer stock;
}