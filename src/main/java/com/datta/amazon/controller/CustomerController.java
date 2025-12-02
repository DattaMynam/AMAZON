package com.datta.amazon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datta.amazon.dtos.CustomerRequest;
import com.datta.amazon.model.Customer;
import com.datta.amazon.service.CustomerSerivce;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private CustomerSerivce svc;

	public CustomerController(CustomerSerivce svc) {
		this.svc = svc;
	}

	@PostMapping("/signup")
    public Customer signup(@RequestBody CustomerRequest req) {
        Customer c = svc.signup(req);
        c.setPassword(null); // never return password
        return c;
    }

    @PostMapping("/login")
    public Customer login(@RequestBody CustomerRequest req, HttpSession session) {
        Customer c = svc.login(req.getEmail(), req.getPassword());
        session.setAttribute("customer", c);
        c.setPassword(null); // hide password
        return c;
    }

	@GetMapping("/me")
	public Customer me(HttpSession session) {
	    Customer c = (Customer) session.getAttribute("customer");
	    if (c == null) throw new RuntimeException("Not logged in");
	    return c;
	}

//	@PostMapping("/logout")
//	public String logout(HttpSession session) {
//	    session.invalidate();
//	    return "Logged out";
//	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session, HttpServletResponse res) {
	    session.invalidate();

	    Cookie cookie = new Cookie("JSESSIONID", null);
	    cookie.setPath("/");
	    cookie.setMaxAge(0);
	    res.addCookie(cookie);

	    return ResponseEntity.ok("Logged out");
	}

	
    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable Long id, HttpSession session) {
        Customer loggedIn = (Customer) session.getAttribute("customer");
        if (loggedIn == null || !loggedIn.getId().equals(id)) {
            throw new RuntimeException("Unauthorized or not logged in");
        }
        svc.deleteCustomer(id);
        session.invalidate(); // log out after delete
        return "Customer and all related data deleted";
    }
	
}