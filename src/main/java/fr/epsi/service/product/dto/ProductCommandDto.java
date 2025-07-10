package fr.epsi.service.product.dto;

public class ProductCommandDto {

    private Integer idProduit;
    private Integer quantity;

    public ProductCommandDto() {}

    public ProductCommandDto(Integer idProduit, Integer quantity) {
        this.idProduit = idProduit;
        this.quantity = quantity;
    }

    public Integer getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Integer idProduit) {
        this.idProduit = idProduit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
