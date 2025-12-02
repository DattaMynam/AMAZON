package com.datta.amazon.service;

import java.util.List;

import com.datta.amazon.dtos.ProductRequest;
import com.datta.amazon.model.Product;

public interface ProductService {

	Product save(ProductRequest product);

	List<Product> getAll();

	Product getById(Long id);

	Product update(Product p);

	String deleteById(Long id);
}
