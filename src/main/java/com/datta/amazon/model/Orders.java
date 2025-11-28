package com.datta.amazon.model;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
	@SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
	private Long id;
	private Long customerId;
	private Double totalAmount;
	private String razorpayOrderId;
	private String status; // CREATED, PAID, CANCELLED
	private Instant createdAt = Instant.now();

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private List<OrderItem> items;
}