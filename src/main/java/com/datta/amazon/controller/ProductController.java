package com.datta.amazon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.ProductRequest;
import com.datta.amazon.model.Product;
import com.datta.amazon.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	private ProductService svc;

	public ProductController(ProductService svc) {
		this.svc = svc;
	}

	@PostMapping("/add")
	public Product addProduct(@RequestBody ProductRequest product) {
		return svc.save(product);
	}

	@GetMapping
	public List<Product> getAllProducts() {
		return svc.getAll();
	}

	@GetMapping("/{id}")
	public Product getProductById(@PathVariable Long id) {
		return svc.getById(id);
	}
	
	@PutMapping()
	public Product update(@RequestBody Product p) {
	    return svc.update(p);
	}


	@DeleteMapping("/{id}")
	public String deleteProduct(@PathVariable Long id) {
		return svc.deleteById(id);
	}

}
