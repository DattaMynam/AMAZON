package com.datta.amazon.model;

import java.time.Instant;

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
@Table(name = "payment")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
	@SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize = 1)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Orders order;
	private String razorpayPaymentId;
	private String razorpayOrderId;
	private String status; // SUCCESS, FAILED
	private Instant paidAt;
}
