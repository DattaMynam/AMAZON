package com.datta.amazon.service;

import java.util.Map;

public interface PaymentService {

	Map<String, Object> createPayment(Long orderId, Double amount);

	boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);
}
