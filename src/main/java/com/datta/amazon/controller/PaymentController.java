package com.datta.amazon.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.CreatePaymentRequest;
import com.datta.amazon.model.Orders;
import com.datta.amazon.model.Payment;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.repository.PaymentRepository;
import com.datta.amazon.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private PaymentService svc;
	private PaymentRepository paymentRepo;
	private CartItemRepository cartRepo;
	private OrderRepository orderRepo;

	public PaymentController(PaymentService svc, PaymentRepository paymentRepo, OrderRepository orderRepo,
			CartItemRepository cartRepo) {
		this.svc = svc;
		this.paymentRepo = paymentRepo;
		this.orderRepo = orderRepo;
		this.cartRepo = cartRepo;
	}

	@PostMapping("/create")
	public Map<String, Object> createPayment(@RequestBody CreatePaymentRequest request) {
		return svc.createPayment(request.getOrderId(), request.getAmount());
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyPayment(@RequestParam String razorpay_order_id,
			@RequestParam String razorpay_payment_id, @RequestParam String razorpay_signature) {

		// Step 1: Verify signature
		boolean ok = svc.verifyPayment(razorpay_order_id, razorpay_payment_id, razorpay_signature);

		if (!ok) {
			return ResponseEntity.badRequest().body("Invalid signature");
		}

		// Step 2: Fetch order by razorpay_order_id
		Orders order = orderRepo.findByRazorpayOrderId(razorpay_order_id)
				.orElseThrow(() -> new RuntimeException("Order not found"));

		// Step 3: Save payment record
		Payment p = new Payment();
		p.setOrder(order);
		p.setRazorpayOrderId(razorpay_order_id);
		p.setRazorpayPaymentId(razorpay_payment_id);
		p.setStatus("SUCCESS");
		p.setPaidAt(Instant.now());
		paymentRepo.save(p);

		// Step 4: Update order status to PAID
		order.setStatus("PAID");
		orderRepo.save(order);

		// Step 5: Clear cart (ONLY place where cart gets deleted)
		var cartItems = cartRepo.findByCustomerId(order.getCustomerId());
		if (!cartItems.isEmpty()) {
			cartRepo.deleteAll(cartItems);
		}

		return ResponseEntity.ok("Payment verified & cart cleared");
	}

}
