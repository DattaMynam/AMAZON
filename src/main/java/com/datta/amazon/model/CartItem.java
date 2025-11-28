package com.datta.amazon.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "cart_item")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_item_seq")
	@SequenceGenerator(name = "cart_item_seq", sequenceName = "cart_item_seq", allocationSize = 1)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id")
	private Product product;
	private Integer quantity;
}
