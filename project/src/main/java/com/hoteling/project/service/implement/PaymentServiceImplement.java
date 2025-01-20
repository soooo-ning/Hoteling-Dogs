package com.hoteling.project.service.implement;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.dto.request.payment.PaymentRequestDto;
import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.dto.response.payment.PaymentResponseDto;
import com.hoteling.project.domain.entity.*;
import com.hoteling.project.exception.PaymentException;
import com.hoteling.project.repository.HotelRoomRepository;
import com.hoteling.project.repository.PaymentRepository;
import com.hoteling.project.repository.ReservationRepository;
import com.hoteling.project.service.PaymentService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class PaymentServiceImplement implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final HotelRoomRepository hotelRoomRepository;

    @Value("${iamport.api.key}")
    private String apiKey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    private IamportClient iamportClient;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    @Override
    @Transactional
    public ResponseEntity<? super PaymentResponseDto> processPayment(PaymentRequestDto paymentRequestDto) {
        try {
            log.info("Processing payment for reservation ID: {}", paymentRequestDto.getReservationId());

            ReservationEntity reservation = reservationRepository.findById(paymentRequestDto.getReservationId())
                .orElseThrow(() -> new PaymentException("Reservation not found"));

            if (reservation.getPaymentStatus() == PaymentStatus.COMPLETED) {
                throw new PaymentException("Reservation already paid");
            }

            IamportResponse<Payment> paymentResponse = iamportClient.paymentByImpUid(paymentRequestDto.getImpUid());

            if (paymentResponse.getResponse() == null) {
                throw new PaymentException("Invalid payment response from Iamport");
            }

            Payment payment = paymentResponse.getResponse();

            BigDecimal calculatedAmount = calculatePaymentAmount(reservation);
            if (!payment.getAmount().equals(calculatedAmount)) {
                throw new PaymentException("Payment amount mismatch");
            }

            PaymentEntity paymentEntity = createPaymentEntity(reservation, payment);
            paymentRepository.save(paymentEntity);

            updateReservationStatus(reservation);

            log.info("Payment processed successfully for reservation ID: {}", paymentRequestDto.getReservationId());
            return PaymentResponseDto.success(paymentEntity);

        } catch (PaymentException e) {
            log.error("Payment processing failed: {}", e.getMessage());
            return PaymentResponseDto.validationFailed();
        } catch (IamportResponseException | IOException e) {
            log.error("Iamport API error: {}", e.getMessage());
            return PaymentResponseDto.validationFailed();
        } catch (Exception e) {
            log.error("Unexpected error during payment processing", e);
            return PaymentResponseDto.databaseError();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<? super ResponseDto> getPaymentDetails(Long paymentId) {
        try {
            log.info("Fetching payment details for payment ID: {}", paymentId);
            Optional<PaymentEntity> paymentOpt = paymentRepository.findById(paymentId);
            
            if (paymentOpt.isPresent()) {
                return PaymentResponseDto.success(paymentOpt.get());
            } else {
                log.warn("Payment not found for ID: {}", paymentId);
                return PaymentResponseDto.notFound();
            }
        } catch (Exception e) {
            log.error("Error fetching payment details", e);
            return PaymentResponseDto.databaseError();
        }
    }

    private BigDecimal calculatePaymentAmount(ReservationEntity reservation) {
        List<HotelRoomEntity> reservedRooms = hotelRoomRepository.findByHotelAndDateBetweenAndDogType(
            reservation.getHotel(),
            reservation.getStartDate(),
            reservation.getEndDate(),
            reservation.getDogType());

        if (reservedRooms.isEmpty()) {
            throw new PaymentException("No rooms found for the reservation");
        }

        long nights = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());

        return reservedRooms.stream()
            .map(HotelRoomEntity::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .multiply(BigDecimal.valueOf(nights));
    }

    private PaymentMethod convertPaymentMethod(String iamportPayMethod) {
        if (iamportPayMethod == null) {
            return PaymentMethod.OTHER;
        }

        return switch (iamportPayMethod.toLowerCase()) {
            case "kakaopay" -> PaymentMethod.KAKAO_PAY;
            case "tosspay" -> PaymentMethod.TOSS_PAY;
            default -> PaymentMethod.OTHER;
        };
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private PaymentEntity createPaymentEntity(ReservationEntity reservation, Payment payment) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setReservation(reservation);
        paymentEntity.setPaymentDate(convertToLocalDateTime(payment.getPaidAt()));
        paymentEntity.setPaymentMethod(convertPaymentMethod(payment.getPayMethod()));
        paymentEntity.setPaymentPrice(payment.getAmount());
        paymentEntity.setImpUid(payment.getImpUid());
        paymentEntity.setStatus(PaymentStatus.COMPLETED);
        return paymentEntity;
    }

    private void updateReservationStatus(ReservationEntity reservation) {
        reservation.setPaymentStatus(PaymentStatus.COMPLETED);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }
}