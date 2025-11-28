package com.datta.amazon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datta.amazon.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
