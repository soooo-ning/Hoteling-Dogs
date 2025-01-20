package com.hoteling.project.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;

import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.dto.request.reservation.ReservationRequestDto;
import com.hoteling.project.domain.entity.DogType;

public interface ReservationService {

  ResponseEntity<? super ResponseDto> createReservation(String userId, Long hotelId,
      LocalDate startDate, LocalDate endDate, DogType dogType, ReservationRequestDto requestDto);

  BigDecimal calculateReservationAmount(Long hotelId, LocalDate startDate, LocalDate endDate, DogType dogType);

}
