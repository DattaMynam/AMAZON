package com.datta.amazon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datta.amazon.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
