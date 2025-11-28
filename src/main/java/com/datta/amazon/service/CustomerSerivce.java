package com.datta.amazon.service;

import com.datta.amazon.dtos.CustomerRequest;
import com.datta.amazon.model.Customer;

public interface CustomerSerivce {
	Customer signup(CustomerRequest c);

	Customer login(String email, String password);

}
