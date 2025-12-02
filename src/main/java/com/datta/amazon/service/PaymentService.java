package com.datta.amazon.service;

import java.util.Map;

public interface PaymentService {

	Map<String, Object> createPayment(Long orderId);

	boolean verifyPayment(String orderId, String paymentId, String signature);

	void processSuccessfulPayment(String razorpayOrderId, String razorpayPaymentId);


}
