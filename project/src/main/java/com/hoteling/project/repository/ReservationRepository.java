package com.hoteling.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.PaymentStatus;
import com.hoteling.project.domain.entity.ReservationEntity;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

	List<ReservationEntity> findByHotel_HotelId(Long hotelId);

	@Query("SELECT r FROM ReservationEntity r WHERE r.paymentStatus = COMPLETED AND r.reservationDate < :cutoffTime")
	List<ReservationEntity> findUnpaidReservationsOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

	List<ReservationEntity> findByPaymentStatus(PaymentStatus paymentStatus);

}
