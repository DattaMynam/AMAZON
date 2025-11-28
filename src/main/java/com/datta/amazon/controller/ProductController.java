package com.datta.amazon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public Product addProduct(@RequestBody Product product) {
		return svc.save(product);
	}

	@GetMapping
	public List<Product> all() {
		return svc.getAll();
	}

	@GetMapping("/{id}")
	public Product one(@PathVariable Long id) {
		return svc.getById(id);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable Long id) {
		svc.deleteById(id);
	}

}
