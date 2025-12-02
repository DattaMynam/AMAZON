package com.datta.amazon.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.CustomerRequest;
import com.datta.amazon.model.Customer;
import com.datta.amazon.model.OrderItem;
import com.datta.amazon.model.Orders;
import com.datta.amazon.model.Payment;
import com.datta.amazon.repository.CartItemRepository;
import com.datta.amazon.repository.CustomerRepository;
import com.datta.amazon.repository.OrderItemRepository;
import com.datta.amazon.repository.OrderRepository;
import com.datta.amazon.repository.PaymentRepository;
import com.datta.amazon.service.CustomerSerivce;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerSerivce {

	private CustomerRepository customerRepo;
	private CartItemRepository cartRepo;
	private PaymentRepository paymentRepo;
	private OrderRepository orderRepo;
	private OrderItemRepository orderItemRepo;

	public CustomerServiceImpl(CustomerRepository repo, CartItemRepository cartRepo,
	PaymentRepository paymentRepo,
	OrderRepository orderRepo,
	OrderItemRepository orderItemRepo) {
		this.customerRepo = repo;
		this.cartRepo=cartRepo;
		this.paymentRepo=paymentRepo;
		this.orderRepo=orderRepo;
		this.orderItemRepo=orderItemRepo;
	}

	@Override
	@Transactional
	public Customer signup(CustomerRequest req) {
		Optional<Customer> existing = customerRepo.findByEmail(req.getEmail());
		if (existing.isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		Customer c = new Customer();
		c.setName(req.getName());
		c.setEmail(req.getEmail());
		c.setPassword(req.getPassword());
		System.out.println("Signup success: " + c);

		return customerRepo.save(c);
	}

	@Override
	public Customer login(String email, String password) {
		Customer c = customerRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email"));

		if (!c.getPassword().equals(password)) {
			throw new RuntimeException("Invalid password");
		}

		System.out.println("login success: " + c);
		return c;
	}
	
	@Override
	@Transactional
	public void deleteCustomer(Long customerId) {
	    Customer customer = customerRepo.findById(customerId)
	            .orElseThrow(() -> new RuntimeException("Customer not found"));

	    cartRepo.deleteByCustomerId(customerId);

	    List<Orders> orders = orderRepo.findByCustomerId(customerId);

	    for (Orders order : orders) {

	        List<Payment> payments = paymentRepo.findByOrderId(order.getId());
	        if (!payments.isEmpty()) {
	            paymentRepo.deleteAll(payments);
	        }

	        List<OrderItem> items = orderItemRepo.findByOrderId(order.getId());
	        if (!items.isEmpty()) {
	            orderItemRepo.deleteAll(items);
	        }

	        orderRepo.delete(order);
	    }

	    customerRepo.delete(customer);

	    System.out.println("Customer and all related data deleted: " + customerId);
	}

}
