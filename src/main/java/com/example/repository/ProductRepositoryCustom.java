package com.example.repository;

import com.example.builder.ProductBuilder;
import com.example.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<Product> findAll2(ProductBuilder builder, Pageable pageable);
}
