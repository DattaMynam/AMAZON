package com.datta.amazon.service;

import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.model.Orders;

public interface OrderService {

	Orders createOrder(CreateOrderRequest req);

	Orders getById(Long id);
}
