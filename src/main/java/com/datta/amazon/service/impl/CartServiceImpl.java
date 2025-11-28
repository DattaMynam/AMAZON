package com.datta.amazon.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.CartItem;
import com.datta.amazon.model.Customer;
import com.datta.amazon.model.Product;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.ProductRepository;
import com.datta.amazon.service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	private final CartItemRepository cartRepo;
	private final ProductRepository productRepo;

	public CartServiceImpl(CartItemRepository cartRepo, ProductRepository productRepo) {
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
	}

	@Override
	@Transactional
	public CartItem addToCart(Long customerId, Long productId, Integer qty) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		CartItem item = new CartItem();
		Customer u = new Customer();
		u.setId(customerId);
		item.setCustomer(u);
		item.setProduct(product);
		item.setQuantity(qty);

		System.out.println("item saved in cart : " + item);
		return cartRepo.save(item);
	}

	@Override
	public List<CartItem> getCart(Long customerId) {
		System.out.println("cart getting: " + customerId);
		return cartRepo.findByCustomerId(customerId);
	}

	@Override
	@Transactional
	public CartItem updateCart(AddToCartRequest req) {
		CartItem item = cartRepo.findByCustomerIdAndProductId(req.getCustomerId(), req.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
		item.setQuantity(req.getQuantity());
		System.out.println("Cart updated: " + item);
		return cartRepo.save(item);
	}

	@Override
	@Transactional
	public void clearCart(Long customerId) {
		System.out.println("Cart cleared: " + customerId);
		cartRepo.deleteByCustomerId(customerId);
	}
}
