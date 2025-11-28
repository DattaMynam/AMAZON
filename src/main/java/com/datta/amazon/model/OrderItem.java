package com.datta.amazon.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "order_item")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_item_seq")
	@SequenceGenerator(name = "order_item_seq", sequenceName = "order_item_seq", allocationSize = 1)
	private Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders order;
	private Long productId;
	private String productName;
	private Integer quantity;
	private Double price;
}
