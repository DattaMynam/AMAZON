package com.datta.amazon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "products")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
	@SequenceGenerator(name = "products_seq", sequenceName = "products_seq", allocationSize = 1)
	private Long id;

	@NotBlank(message = "Name cannot be empty")
	@Column(nullable = false)
	private String name;

	@Column(length = 1000)
	private String description;
	private String imageUrl;

	@Positive(message = "Price must be greater than zero")
	private Double price;
	private Integer stockQuantity;

}
