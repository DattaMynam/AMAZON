package com.datta.amazon.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.model.CartItem;
import com.datta.amazon.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private CartService svc;

	public CartController(CartService svc) {
		this.svc = svc;
	}

	@PostMapping("/add")
	public CartItem addToCart(@Valid @RequestBody AddToCartRequest req) {
		return svc.addToCart(req.getCustomerId(), req.getProductId(), req.getQuantity());
	}

	// In CartController.java
	@PostMapping("/update")
	public CartItem updateCart(@Valid @RequestBody AddToCartRequest req) {
		return svc.updateCart(req);
	}

	@GetMapping("/{customerId}")
	public List<CartItem> getCart(@PathVariable Long customerId) {
		return svc.getCart(customerId);
	}
}
