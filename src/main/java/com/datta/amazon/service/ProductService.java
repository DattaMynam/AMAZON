package com.datta.amazon.service;

import java.util.List;

import com.datta.amazon.model.Product;

public interface ProductService {

	List<Product> getAll();

	Product getById(Long id);

	Product save(Product product);

	void deleteById(Long id);
}
