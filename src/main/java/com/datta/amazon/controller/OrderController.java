package com.datta.amazon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.model.Orders;
import com.datta.amazon.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private OrderService svc;

	public OrderController(OrderService svc) {
		this.svc = svc;
	}

	@PostMapping("/create")
	public Orders createOrder(@Valid @RequestBody CreateOrderRequest req) {
		return svc.createOrder(req);
	}

	@GetMapping("/{id}")
	public Orders getOrder(@PathVariable Long id) {
		return svc.getById(id);
	}
}
