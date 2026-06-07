package com.sistema.mapper;

import com.sistema.dto.ProductDTO;
import com.sistema.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO productDTO);
}
