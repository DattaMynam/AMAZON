package com.datta.amazon.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.Product;
import com.datta.amazon.repository.ProductRepository;
import com.datta.amazon.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repo;

	public ProductServiceImpl(ProductRepository repo) {
		this.repo = repo;
	}

	@Override
	public Product save(Product product) {
		System.out.println("product saved" + product);
		return repo.save(product);
	}

	@Override
	public List<Product> getAll() {
		System.out.println("all product getting ");
		return repo.findAll();
	}

	@Override
	public Product getById(Long id) {
		System.out.println("product getting" + id);
		return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	@Override
	public void deleteById(Long id) {
		System.out.println("product deleted" + id);
		repo.deleteById(id);
	}

}
