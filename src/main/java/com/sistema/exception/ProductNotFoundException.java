package com.sistema.exception;

public class ProductNotFoundException extends BusinessException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
