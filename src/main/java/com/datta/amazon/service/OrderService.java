package com.datta.amazon.service;

import java.util.List;

import com.datta.amazon.dtos.AddToCartRequest;
import com.datta.amazon.dtos.CreateOrderRequest;
import com.datta.amazon.model.Orders;

public interface OrderService {

	Orders createOrder(CreateOrderRequest req);

	Orders getById(Long id);

	List<Orders> getOrdersByCustomerId(Long id);

	Orders buySingleProduct(AddToCartRequest req);

}
