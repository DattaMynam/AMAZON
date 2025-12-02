package com.datta.amazon.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.Customer;
import com.datta.amazon.model.OrderItem;
import com.datta.amazon.model.Orders;
import com.datta.amazon.model.Product;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.CustomerRepository;
import com.datta.amazon.repository.OrderItemRepository;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.repository.ProductRepository;
import com.datta.amazon.service.OrderService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepo;
	private final OrderItemRepository orderItemRepo;
	private final CartItemRepository cartRepo;
	private final CustomerRepository customerRepo;
	private final ProductRepository productRepo;

	public OrderServiceImpl(CustomerRepository customerRepo, ProductRepository productRepo, OrderRepository orderRepo,
			OrderItemRepository orderItemRepo, CartItemRepository cartRepo) {
		this.orderRepo = orderRepo;
		this.orderItemRepo = orderItemRepo;
		this.cartRepo = cartRepo;
		this.customerRepo = customerRepo;
		this.productRepo = productRepo;
	}

	@Override
	@Transactional
	public Orders createOrder(CreateOrderRequest req) {
		var cartItems = cartRepo.findByCustomerId(req.getCustomerId());
		if (cartItems == null || cartItems.isEmpty()) {
			throw new IllegalStateException("Cart is empty");
		}

		Orders order = new Orders();
		order.setCustomer(cartItems.get(0).getCustomer());
		order.setStatus("CREATED");
		order.setOrderType("CART");

		double total = cartItems.stream().mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity()).sum();
		order.setTotalAmount(total);

		order = orderRepo.save(order);

		Orders finalOrder = order;
		var items = cartItems.stream().map(ci -> {
			OrderItem oi = new OrderItem();
			oi.setOrder(finalOrder);
			oi.setProductId(ci.getProduct().getId());
			oi.setProductName(ci.getProduct().getName());
			oi.setQuantity(ci.getQuantity());
			oi.setPrice(ci.getProduct().getPrice());
			return oi;
		}).toList();

		orderItemRepo.saveAll(items);
		System.out.println("Order created: " + order);
		return order;
	}

	@Override
	public Orders getById(Long id) {
		return orderRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
	}

	@Override
	public List<Orders> getOrdersByCustomerId(Long customerId) {
		return orderRepo.findByCustomerId(customerId);
	}

	@Override
	@Transactional
	public Orders buySingleProduct(AddToCartRequest req) {

		Customer customer = customerRepo.findById(req.getCustomerId())
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		Product product = productRepo.findById(req.getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found"));

		int qty = req.getQuantity() == null || req.getQuantity() < 1 ? 1 : req.getQuantity();

		double total = product.getPrice() * qty;

		// create Order
		Orders order = new Orders();
		order.setCustomer(customer);
		order.setTotalAmount(total);
		order.setStatus("CREATED");
		order.setOrderType("Single_Product");

		// create item
		OrderItem item = new OrderItem();
		item.setOrder(order);
		item.setProductId(product.getId());
		item.setProductName(product.getName());
		item.setQuantity(qty);
		item.setPrice(product.getPrice());

		order.setItems(List.of(item));

		return orderRepo.save(order);
	}
}
