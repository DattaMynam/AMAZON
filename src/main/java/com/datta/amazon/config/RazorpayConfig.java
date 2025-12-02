package com.datta.amazon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.razorpay.RazorpayClient;

@Configuration
public class RazorpayConfig {

	@Bean
	public RazorpayClient razorpayClient(RazorpayProperties props) throws Exception {
		return new RazorpayClient(props.getKey(), props.getSecret());
	}
}