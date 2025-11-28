package com.datta.amazon.service;

import java.util.List;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.model.CartItem;

public interface CartService {

	CartItem addToCart(Long customerId, Long productId, Integer qty);

	List<CartItem> getCart(Long customerId);

	CartItem updateCart(AddToCartRequest req);

	void clearCart(Long customerId);

}
