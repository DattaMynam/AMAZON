package com.datta.amazon.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
	@SequenceGenerator(name = "customer_seq", sequenceName = "customer_seq", allocationSize = 1)
	private Long id;

	@Column(nullable = false, unique = true)
	private String email;

	@NotBlank(message = "Name cannot be empty")
	@Column(nullable = false)
	private String name;

	@NotBlank(message = "Password cannot be empty")
	@Column(nullable = false)
	private String password;

}
