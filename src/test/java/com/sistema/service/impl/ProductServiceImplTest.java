package com.sistema.service.impl;

import com.sistema.exception.InsufficientStockException;
import com.sistema.exception.ProductNotFoundException;
import com.sistema.model.Product;
import com.sistema.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(1L, "Test Product", "Description", BigDecimal.valueOf(100.0), 10, true);
    }

    @Test
    void create_ShouldSaveProduct_AndSetActiveTrue() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        
        Product newProduct = new Product();
        newProduct.setName("Test Product");
        
        Product created = productService.create(newProduct);
        
        assertNotNull(created);
        assertTrue(created.getActive());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void findAllActive_ShouldReturnPage() {
        Page<Product> page = new PageImpl<>(Collections.singletonList(sampleProduct));
        when(productRepository.findAllByActiveTrue(any())).thenReturn(page);
        
        Page<Product> result = productService.findAllActive(PageRequest.of(0, 10));
        
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAllByActiveTrue(any());
    }

    @Test
    void findById_WhenExists_ShouldReturnProduct() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleProduct));
        
        Product found = productService.findById(1L);
        
        assertEquals(sampleProduct.getId(), found.getId());
    }

    @Test
    void findById_WhenNotExists_ShouldThrowException() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());
        
        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void updateStock_WhenSufficient_ShouldUpdateAndSave() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);
        
        Product updated = productService.updateStock(1L, -5);
        
        assertEquals(5, updated.getStock());
        verify(productRepository).save(sampleProduct);
    }

    @Test
    void updateStock_WhenInsufficient_ShouldThrowException() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleProduct));
        
        assertThrows(InsufficientStockException.class, () -> productService.updateStock(1L, -15));
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteLogically_ShouldSetActiveFalse_AndSave() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(sampleProduct));
        
        productService.deleteLogically(1L);
        
        assertFalse(sampleProduct.getActive());
        verify(productRepository).save(sampleProduct);
    }
}
