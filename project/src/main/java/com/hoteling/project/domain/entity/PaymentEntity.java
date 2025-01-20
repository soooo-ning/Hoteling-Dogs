package com.hoteling.project.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.*;

import com.hoteling.project.domain.dto.request.payment.PaymentRequestDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class PaymentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@ManyToOne
	@JoinColumn(name = "reservation_id", nullable = false)
	private ReservationEntity reservation;

	@Column(nullable = false)
	private LocalDateTime paymentDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentMethod paymentMethod;

	@Column(nullable = false)
	private BigDecimal paymentPrice;

	@Column(nullable = false)
	private String impUid; // 아임포트 고유 결제 번호

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	public PaymentEntity(PaymentRequestDto dto) {
		this.paymentDate = LocalDateTime.now();
		this.paymentMethod = dto.getPaymentMethod();
		this.paymentPrice = dto.getPaymentPrice();
		this.impUid = dto.getImpUid();
	}

}
