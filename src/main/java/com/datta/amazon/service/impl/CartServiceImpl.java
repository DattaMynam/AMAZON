package com.datta.amazon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CartItemResponse;
import com.datta.amazon.dtos.CartResponse;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.CartItem;
import com.datta.amazon.model.Customer;
import com.datta.amazon.model.Product;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.CustomerRepository;
import com.datta.amazon.repository.ProductRepository;
import com.datta.amazon.service.CartService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {

	private final CartItemRepository cartRepo;
	private final ProductRepository productRepo;
	private final CustomerRepository customerRepo;

	public CartServiceImpl(CartItemRepository cartRepo, ProductRepository productRepo,
			CustomerRepository customerRepo) {
		this.cartRepo = cartRepo;
		this.productRepo = productRepo;
		this.customerRepo = customerRepo;
	}

	@Override
	@Transactional
	public CartResponse addToCart(Long customerId, Long productId, Integer qty) {

		Customer customer = customerRepo.findById(customerId)
				.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		if (qty <= 0) {
			throw new IllegalArgumentException("Quantity must be greater than zero");
		}

		CartItem existing = cartRepo.findByCustomerIdAndProductId(customerId, productId).orElse(null);

		if (existing != null) {
			existing.setQuantity(existing.getQuantity() + qty);
			cartRepo.save(existing);
		} else {
			CartItem newItem = new CartItem();
			newItem.setCustomer(customer);
			newItem.setProduct(product);
			newItem.setQuantity(qty);
			cartRepo.save(newItem);
		}

		return buildCartResponse(customerId);
	}

	@Override
	public CartResponse getCart(Long customerId) {
		return buildCartResponse(customerId);
	}

	@Override
	@Transactional
	public CartResponse updateCart(AddToCartRequest req) {

		CartItem item = cartRepo.findByCustomerIdAndProductId(req.getCustomerId(), req.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

		if (req.getQuantity() <= 0) {
			throw new IllegalArgumentException("Quantity must be > 0");
		}

		item.setQuantity(req.getQuantity());
		cartRepo.save(item);

		return buildCartResponse(req.getCustomerId());
	}

	@Override
	@Transactional
	public void removeItem(Long customerId, Long productId) {
		cartRepo.deleteByCustomerIdAndProductId(customerId, productId);
	}

	@Override
	@Transactional
	public void clearCart(Long customerId) {
		cartRepo.deleteByCustomerId(customerId);
	}

	private CartResponse buildCartResponse(Long customerId) {

		List<CartItem> items = cartRepo.findByCustomerId(customerId);

		List<CartItemResponse> responses = new ArrayList<>();
		double total = 0;

		for (CartItem item : items) {

			double itemTotal = item.getQuantity() * item.getProduct().getPrice();
			total += itemTotal;

			responses.add(CartItemResponse.builder().productId(item.getProduct().getId())
					.name(item.getProduct().getName()).price(item.getProduct().getPrice()).quantity(item.getQuantity())
					.totalPrice(itemTotal).imageUrl(item.getProduct().getImageUrl()).build());
		}

		return CartResponse.builder().items(responses).cartTotal(total).totalItems(items.size()).build();
	}

}