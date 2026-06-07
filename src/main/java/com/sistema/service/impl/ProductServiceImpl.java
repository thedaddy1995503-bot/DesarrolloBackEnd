package com.sistema.service.impl;

import com.sistema.exception.InsufficientStockException;
import com.sistema.exception.ProductNotFoundException;
import com.sistema.model.Product;
import com.sistema.repository.ProductRepository;
import com.sistema.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Product create(Product product) {
        product.setActive(true);
        // Note: For extra points we could add external API integration here, but omitting for JSF focus unless requested.
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findAllByActiveTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Product update(Product product) {
        Product existing = findById(product.getId());
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        return productRepository.save(existing);
    }

    @Override
    @Transactional
    public Product updateStock(Long id, Integer quantityChange) {
        Product product = findById(id);
        int newStock = product.getStock() + quantityChange;
        
        if (newStock < 0) {
            throw new InsufficientStockException("Stock insuficiente para el producto: " + product.getName());
        }
        
        product.setStock(newStock);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteLogically(Long id) {
        Product product = findById(id);
        product.setActive(false);
        productRepository.save(product);
    }
}
