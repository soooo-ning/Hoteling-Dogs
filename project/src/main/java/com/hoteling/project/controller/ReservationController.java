package com.hoteling.project.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hoteling.project.domain.dto.request.reservation.ReservationRequestDto;
import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.dto.response.reservation.ReservationResponseDto;
import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.HotelRoomEntity;
import com.hoteling.project.service.HotelListService;
import com.hoteling.project.service.PaymentService;
import com.hoteling.project.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final HotelListService hotelListService;
    private final PaymentService paymentService;

    @GetMapping
    public String reservationPage(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("dogType") DogType dogType,
            Model model,
            Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", auth.getName());
        } else {
            model.addAttribute("isAuthenticated", false);
        }

        logger.info("Reservation page accessed. HotelId: {}, StartDate: {}, EndDate: {}, DogType: {}",
                hotelId, startDate, endDate, dogType);

                
                HotelEntity hotel = hotelListService.findHotelById(hotelId);
                List<HotelRoomEntity> availableRooms = hotelListService.findAvailableRooms(hotelId, startDate, endDate,
                dogType);
                
                model.addAttribute("hotel", hotel);
                model.addAttribute("hotelMainImageUrl", hotel.getMainImageUrl());
                model.addAttribute("availableRooms", availableRooms);
                model.addAttribute("startDate", startDate);
                model.addAttribute("endDate", endDate);
                String dogTypeKorean = convertDogType(dogType);
                model.addAttribute("dogType", dogTypeKorean);


        BigDecimal totalPrice = reservationService.calculateReservationAmount(hotelId, startDate, endDate, dogType);
        logger.info("Calculated total price: {}", totalPrice);
        model.addAttribute("totalPrice", totalPrice);

        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        model.addAttribute("nights", nights);

        ReservationRequestDto reservationForm = new ReservationRequestDto();
        model.addAttribute("reservationForm", reservationForm);

        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "예약 페이지");

        logger.info("Reservation page rendered successfully");

        return "reservation";
    }

    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculateReservationAmount(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("dogType") DogType dogType) {

        logger.info("Calculating reservation amount. HotelId: {}, StartDate: {}, EndDate: {}, DogType: {}",
                hotelId, startDate, endDate, dogType);

        BigDecimal totalPrice = reservationService.calculateReservationAmount(hotelId, startDate, endDate, dogType);
        logger.info("Calculated total price: {}", totalPrice);

        return ResponseEntity.ok(totalPrice);
    }

    @PostMapping
    public ResponseEntity<?> createReservation(
            @RequestParam("hotelId") Long hotelId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("dogType") DogType dogType,
            @RequestBody ReservationRequestDto reservationForm,
            Authentication authentication) {

        try {
            String userId = authentication.getName();

            logger.info("Received reservation request: {}", reservationForm);
            logger.info("HotelId: {}, StartDate: {}, EndDate: {}, DogType: {}", hotelId, startDate, endDate, dogType);

            ResponseEntity<? super ResponseDto> response = reservationService.createReservation(
                    userId,
                    hotelId,
                    startDate,
                    endDate,
                    dogType,
                    reservationForm);

            if (response.getStatusCode().is2xxSuccessful()) {
                ReservationResponseDto reservationResponse = (ReservationResponseDto) response.getBody();
                return ResponseEntity.ok(reservationResponse);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("예약 생성 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            logger.error("Error creating reservation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예약 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // DogType을 한글로 변환하는 메소드 추가
    private String convertDogType(DogType dogType) {
        switch (dogType) {
            case SMALL:
                return "소형견";
            case MEDIUM:
                return "중형견";
            case LARGE:
                return "대형견";
            default:
                return dogType.toString();
        }
    }

}
