package com.hoteling.project.service;

import org.springframework.http.ResponseEntity;

import com.hoteling.project.domain.dto.request.payment.PaymentRequestDto;
import com.hoteling.project.domain.dto.response.payment.PaymentResponseDto;

public interface PaymentService {

  ResponseEntity<? super PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto);
 
 ResponseEntity<? super PaymentResponseDto> getPaymentDetails(Long paymentId);

}