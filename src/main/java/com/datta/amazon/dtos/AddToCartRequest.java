package com.datta.amazon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddToCartRequest {
	private Long customerId;
	private Long productId;
	private Integer quantity;
}