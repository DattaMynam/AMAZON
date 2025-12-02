package com.datta.amazon.service;

import java.util.List;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CartResponse;
import com.datta.amazon.model.CartItem;

public interface CartService {

	CartResponse addToCart(Long customerId, Long productId, Integer qty);

	CartResponse getCart(Long customerId);

	CartResponse updateCart(AddToCartRequest req);

	void clearCart(Long customerId);

	void removeItem(Long customerId, Long productId);

}
