package com.datta.amazon.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
	@SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize = 1)
	private Long id;
	
	@Setter
	@ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

	@Setter
	@Column(nullable = false, unique = true)
    private String razorpayPaymentId;

	@Setter
    @Column(nullable = false)
    private String razorpayOrderId;

	@Setter
    @Column(nullable = false)
    private String status; // SUCCESS, FAILED

	@Setter
	@Column(name="paid_at")
	private LocalDateTime paidAt;

}
