package com.sistema.dto;

public class StockUpdateDTO {
    private Integer quantityChange;

    public StockUpdateDTO() {
    }

    public StockUpdateDTO(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }
}
