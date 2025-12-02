package com.datta.amazon.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.CreatePaymentRequest;
import com.datta.amazon.dtos.PaymentVerifyRequest;
import com.datta.amazon.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private PaymentService svc;

	public PaymentController(PaymentService svc) {
		this.svc = svc;
	}

	@PostMapping("/create")
	public Map<String, Object> createPayment(@RequestBody CreatePaymentRequest request) {
		return svc.createPayment(request.getOrderId());
	}

	@PostMapping("/verify")
	public ResponseEntity<Map<String, String>> verifyPayment(@RequestBody PaymentVerifyRequest req) {
		boolean verified = svc.verifyPayment(req.getRazorpayOrderId(), req.getRazorpayPaymentId(),
				req.getRazorpaySignature());
		if (!verified) {
			return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", "Invalid payment signature"));
		}
		svc.processSuccessfulPayment(req.getRazorpayOrderId(), req.getRazorpayPaymentId());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Payment verified & cart cleared"));
	}

}
