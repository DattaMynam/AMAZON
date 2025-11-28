package com.datta.amazon.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.datta.amazon.dtos.CustomerRequest;
import com.datta.amazon.model.Customer;
import com.datta.amazon.repository.CustomerRepository;
import com.datta.amazon.service.CustomerSerivce;

import jakarta.transaction.Transactional;

@Service
public class CustomerServiceImpl implements CustomerSerivce {

	private CustomerRepository repo;

	public CustomerServiceImpl(CustomerRepository repo) {
		this.repo = repo;
	}

	@Override
	@Transactional
	public Customer signup(CustomerRequest req) {
		// check if email exists
		Optional<Customer> existing = repo.findByEmail(req.getEmail());
		if (existing.isPresent()) {
			throw new RuntimeException("Email already exists");
		}

		Customer c = new Customer();
		c.setName(req.getName());
		c.setEmail(req.getEmail());
		c.setPassword(req.getPassword());
		System.out.println("Signup success: " + c);

		return repo.save(c);
	}

	@Override
	public Customer login(String email, String password) {
		Customer c = repo.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid email"));

		if (!c.getPassword().equals(password)) {
			throw new RuntimeException("Invalid password");
		}

		System.out.println("login success: " + c);
		return c;
	}
}
