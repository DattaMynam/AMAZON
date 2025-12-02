package com.datta.amazon.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.datta.amazon.config.RazorpayProperties;
import com.datta.amazon.exception.ResourceNotFoundException;
import com.datta.amazon.model.Orders;
import com.datta.amazon.model.Payment;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.repository.PaymentRepository;
import com.datta.amazon.service.PaymentService;
import com.datta.amazon.util.CryptoUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final RazorpayClient client;
    private final RazorpayProperties props;
    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;
    private final CartItemRepository cartRepo;

    public PaymentServiceImpl(
            RazorpayProperties props,
            RazorpayClient client,
            OrderRepository orderRepo,
            PaymentRepository paymentRepo,
            CartItemRepository cartRepo
    ) {
        this.props = props;
        this.client = client;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
        this.cartRepo = cartRepo;
    }

    @Override
    @Transactional
    public Map<String, Object> createPayment(Long orderId) {

        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        long amountInPaise = Math.round(order.getTotalAmount() * 100);

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "order_rcpt_" + orderId);

        try {
            Order rzpOrder = client.orders.create(options);

            order.setRazorpayOrderId(rzpOrder.get("id"));
            orderRepo.save(order);

            Map<String, Object> res = new HashMap<>();
            res.put("razorpayOrderId", rzpOrder.get("id"));
            res.put("amount", amountInPaise);
            res.put("currency", "INR");
            res.put("key", props.getKey());

            return res;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }

    @Override
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        String data = orderId + "|" + paymentId;
        String generated = CryptoUtil.hmacSha256Hex(data, props.getSecret());
        return generated.equalsIgnoreCase(signature);
    }

    @Override
    @Transactional
    public void processSuccessfulPayment(String razorpayOrderId, String razorpayPaymentId) {

        if (paymentRepo.existsByRazorpayPaymentId(razorpayPaymentId)) {
            return;
        }

        Orders order = orderRepo.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if ("PAID".equals(order.getStatus())) {
            return;
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setRazorpayOrderId(razorpayOrderId);
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));



        paymentRepo.save(payment);

        order.setStatus("PAID");
        orderRepo.save(order);
        
        if ("CART".equals(order.getOrderType())) {
            var cartItems = cartRepo.findByCustomerId(order.getCustomer().getId());
            if (!cartItems.isEmpty()) {
                cartRepo.deleteAll(cartItems);
            }
        }

        
    }
}
