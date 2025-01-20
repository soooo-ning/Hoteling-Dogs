package com.hoteling.project.domain.dto.request.payment;

import java.math.BigDecimal;

import com.hoteling.project.domain.entity.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentRequestDto {
	
	@NotNull
	private Long reservationId;

	@NotNull
	private PaymentMethod paymentMethod;

	@NotNull
	private BigDecimal paymentPrice;

	@NotNull
	private String impUid;

}
