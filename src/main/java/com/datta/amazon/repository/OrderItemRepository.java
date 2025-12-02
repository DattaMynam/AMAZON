package com.datta.amazon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datta.amazon.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByOrderId(Long id);
}
