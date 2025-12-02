package com.datta.amazon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datta.amazon.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	boolean existsByRazorpayPaymentId(String razorpayPaymentId);

	List<Payment> findByOrderId(Long id);
}
