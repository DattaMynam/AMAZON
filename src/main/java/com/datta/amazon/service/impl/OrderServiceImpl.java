package com.datta.amazon.service.impl;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.OrderItem;
import com.datta.amazon.model.Orders;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.OrderItemRepository;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final CartItemRepository cartRepo;

	public OrderServiceImpl(OrderRepository orderRepo, OrderItemRepository orderItemRepo, CartItemRepository cartRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.cartRepo = cartRepo;
	}

	@Override
	@Transactional
	public Orders createOrder(CreateOrderRequest req) {
		var cartItems = cartRepo.findByCustomerId(req.getCustomerId());
		if (cartItems == null || cartItems.isEmpty()) {
			throw new RuntimeException("Cart is empty");
		}

		Orders order = new Orders();
		order.setCustomerId(req.getCustomerId());
		order.setStatus("CREATED");

		double total = cartItems.stream().mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity()).sum();
		order.setTotalAmount(total);

		order = orderRepo.save(order);

		final Orders finalOrder = order;
		var items = cartItems.stream().map(ci -> {
			var oi = new OrderItem();
			oi.setOrder(finalOrder);
			oi.setProductId(ci.getProduct().getId());
			oi.setProductName(ci.getProduct().getName());
			oi.setQuantity(ci.getQuantity());
			oi.setPrice(ci.getProduct().getPrice());
			return oi;
		}).toList();

		orderItemRepo.saveAll(items);
		order.setItems(items);
		System.out.println("Order created: " + order);
		return order;
	}

	@Override
	public Orders getById(Long id) {
		System.out.println("Order get by id: " + id);
		return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}
}
