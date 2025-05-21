package fr.epsi.service.product.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private Float price;
    private String description;
    private String color;
    private Integer stock;
}