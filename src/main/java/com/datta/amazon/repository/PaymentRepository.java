package com.datta.amazon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datta.amazon.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
