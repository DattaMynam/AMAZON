package com.datta.amazon.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.datta.amazon.config.RazorpayConfig;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.Orders;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.repository.PaymentRepository;
import com.datta.amazon.service.PaymentService;
import com.datta.amazon.util.CryptoUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

	private final RazorpayClient client;
	private final PaymentRepository paymentRepo;
	private final OrderRepository orderRepo;
	private final RazorpayConfig config;

	public PaymentServiceImpl(RazorpayClient client, PaymentRepository paymentRepo, OrderRepository orderRepo,
			RazorpayConfig config) {
		this.client = client;
		this.paymentRepo = paymentRepo;
		this.orderRepo = orderRepo;
		this.config = config;
	}

	@Override
	@Transactional
	public Map<String, Object> createPayment(Long orderId, Double amount) {
		Orders order = orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		// Amount in paise (INR)
		long amountInPaise = Math.round(amount * 100); // round to avoid decimal issues

		JSONObject options = new JSONObject();
		options.put("amount", amountInPaise);
		options.put("currency", "INR");
		options.put("receipt", "order_rcpt_" + order.getId());
		options.put("payment_capture", 1);

		try {
			Order razorpayOrder = client.orders.create(options);

			// Save razorpayOrderId in order for future verification
			order.setRazorpayOrderId(razorpayOrder.get("id"));
			orderRepo.save(order);

			Map<String, Object> res = new HashMap<>();
			res.put("razorpayOrderId", razorpayOrder.get("id"));
			res.put("amount", amountInPaise);
			res.put("currency", "INR");
			res.put("key", config.getKey());
			System.out.println("payment created: " + res);
			return res;

		} catch (RazorpayException e) {
			// Log the detailed exception for debugging
			System.err.println("RazorpayException while creating order: " + e.getMessage());
			e.printStackTrace();

			// Rethrow to be handled by the global handler
			throw new RuntimeException("Failed to create Razorpay order", e);
		}
	}

	@Override
	@Transactional
	public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		String data = razorpayOrderId + "|" + razorpayPaymentId;
		String generated = CryptoUtil.hmacSha256Hex(data, config.getSecret());
		System.out.println("Payment verified: " + razorpaySignature);
		return generated.equals(razorpaySignature); // hex lowercase comparison
	}

}
