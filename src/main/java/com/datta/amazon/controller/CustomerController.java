package com.datta.amazon.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.CustomerRequest;
import com.datta.amazon.model.Customer;
import com.datta.amazon.service.CustomerSerivce;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private CustomerSerivce svc;

	public CustomerController(CustomerSerivce svc) {
		this.svc = svc;
	}

	@PostMapping("/signup")
	public Customer signup(@RequestBody CustomerRequest req) {
		return svc.signup(req);
	}

	@PostMapping("/login")
	public Customer login(@RequestBody CustomerRequest req) {
		return svc.login(req.getEmail(), req.getPassword());
	}
}
