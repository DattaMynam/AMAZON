package com.datta.amazon.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

	@NotBlank(message = "Name cannot be empty")
	@Column(nullable = false, unique = true)
	private String name;

	@Column(length = 1000)
	private String description;
	private String imageUrl;

	@Positive(message = "Price must be greater than zero")
	private Double price;
	private Integer stockQuantity;

}
