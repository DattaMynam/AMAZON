package com.datta.amazon.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.model.Customer;
import com.datta.amazon.model.Orders;
import com.datta.amazon.service.OrderService;
import com.datta.amazon.service.PaymentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private OrderService orderService;
	private PaymentService paymentService;
	

	public OrderController(OrderService orderService, PaymentService paymentService) {
		this.orderService = orderService;
		this.paymentService = paymentService;
	}

	@PostMapping("/create")
	public Orders createOrder(@Valid @RequestBody CreateOrderRequest req) {
		return orderService.createOrder(req);
	}

	@GetMapping("/{id}")
	public Orders getOrder(@PathVariable Long id) {
		return orderService.getById(id);
	}
	@GetMapping("/myorders")
	public List<Orders> getMyOrders(HttpSession session) {
	    Customer c = (Customer) session.getAttribute("customer");
	    if (c == null) {
	        throw new RuntimeException("Not logged in");
	    }
	    return orderService.getOrdersByCustomerId(c.getId());
	}

	@PostMapping("/buy")
	public ResponseEntity<Map<String, Object>> buyNow(@RequestBody AddToCartRequest req) {
	    // 1. Create order for single product
	    Orders order = orderService.buySingleProduct(req);

	    // 2. Trigger payment creation
	    Map<String, Object> paymentResponse = paymentService.createPayment(order.getId());

	    // 3. Return order + payment details to frontend
	    paymentResponse.put("orderId", order.getId());
	    return ResponseEntity.ok(paymentResponse);
	}

	
}
