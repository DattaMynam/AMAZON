package com.datta.amazon.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.datta.amazon.model.Orders;

@Repository

public interface OrderRepository extends JpaRepository<Orders, Long> {
	List<Orders> findByCustomerId(Long customerId);

	Optional<Orders> findByRazorpayOrderId(String razorpayOrderId);
}
