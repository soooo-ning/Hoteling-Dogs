package com.hoteling.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.PaymentEntity;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
  
  Optional<PaymentEntity> findByReservation_ReservationId(Long reservationId);

  boolean existsByReservation_ReservationId(Long reservationId);
  
}
