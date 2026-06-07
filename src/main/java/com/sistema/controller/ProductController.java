package com.sistema.controller;

import com.sistema.dto.ProductDTO;
import com.sistema.dto.StockUpdateDTO;
import com.sistema.mapper.ProductMapper;
import com.sistema.model.Product;
import com.sistema.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    /**
     * Consultar un Producto por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(productMapper.toDto(product));
    }

    /**
     * Actualizar el Stock de un producto
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> updateStock(
            @PathVariable Long id, 
            @RequestBody StockUpdateDTO stockUpdate) {
        
        Product updatedProduct = productService.updateStock(id, stockUpdate.getQuantityChange());
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }
}
