package com.datta.amazon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CartResponse;
import com.datta.amazon.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private CartService svc;

	public CartController(CartService svc) {
		this.svc = svc;
	}

	@PostMapping("/add")
	public ResponseEntity<CartResponse> addToCart(@RequestBody AddToCartRequest req) {
		CartResponse response = svc.addToCart(req.getCustomerId(), req.getProductId(), req.getQuantity());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{customerId}")
	public ResponseEntity<CartResponse> getCart(@PathVariable Long customerId) {
		CartResponse response = svc.getCart(customerId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/update")
	public ResponseEntity<CartResponse> updateCart(@RequestBody AddToCartRequest req) {
		CartResponse response = svc.updateCart(req);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{customerId}/{productId}")
	public ResponseEntity<String> removeItem(@PathVariable Long customerId, @PathVariable Long productId) {
		svc.removeItem(customerId, productId);
		return ResponseEntity.ok("Remove from Cart");
	}

	// clear all cart
	@DeleteMapping("/clear/{customerId}")
	public ResponseEntity<String> clearCart(@PathVariable Long customerId) {
		svc.clearCart(customerId);
		return ResponseEntity.ok("You Cart cleared");
	}

}
