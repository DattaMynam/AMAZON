package com.datta.amazon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.razorpay.RazorpayException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(RazorpayException.class)
	public ResponseEntity<?> handleRazorpay(RazorpayException ex) {
		// Log details if needed
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Payment service error: " + ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleOther(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong: " + ex.getMessage());
	}

}
