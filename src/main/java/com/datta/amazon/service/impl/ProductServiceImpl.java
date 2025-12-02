package com.datta.amazon.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.ProductRequest;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.Product;
import com.datta.amazon.repository.ProductRepository;
import com.datta.amazon.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository repo;

	public ProductServiceImpl(ProductRepository repo) {
		this.repo = repo;
	}

	@Override
	@Transactional
	public Product save(ProductRequest req) {    
	    Optional<Product> existing = repo.findByName(req.getName());
		if (existing.isPresent()) {
			throw new RuntimeException("Product name already exists");
		}
	    Product p=new Product();
	    p.setName(req.getName());
	    p.setDescription(req.getDescription());
	    p.setPrice(req.getPrice());
	    p.setDescription(req.getDescription());
	    p.setStockQuantity(req.getStockQuantity());
	    return repo.save(p);
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
	@Transactional
	public Product update(Product p) {
	    Product existing = repo.findById(p.getId())
	            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

	    if (p.getPrice() != null) {
	        existing.setPrice(p.getPrice());
	    }

	    if (p.getDescription() != null) {
	        existing.setDescription(p.getDescription());
	    }

	    if (p.getImageUrl() != null) {
	        existing.setImageUrl(p.getImageUrl());
	    }

	    if (p.getStockQuantity() != null) {
	        existing.setStockQuantity(p.getStockQuantity());
	    }
	    return repo.save(existing);
	}


	
	@Override
	@Transactional
	public String deleteById(Long id) {
		System.out.println("product deleted" + id);
		repo.deleteById(id);
		return "Deleted Product from DB";
	}
}
