package com.hoteling.project.service.implement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.dto.request.reservation.ReservationRequestDto;
import com.hoteling.project.domain.dto.response.reservation.ReservationResponseDto;
import com.hoteling.project.domain.entity.*;
import com.hoteling.project.repository.HotelRepository;
import com.hoteling.project.repository.HotelRoomRepository;
import com.hoteling.project.repository.ReservationRepository;
import com.hoteling.project.repository.UserRepository;
import com.hoteling.project.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReservationServiceImplement implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final HotelRoomRepository hotelRoomRepository;
    

        private static final Logger logger = LoggerFactory.getLogger(ReservationServiceImplement.class);


        @Override
        @Transactional
        public ResponseEntity<? super ResponseDto> createReservation(String userId, Long hotelId,
                LocalDate startDate, LocalDate endDate, DogType dogType, ReservationRequestDto requestDto) {
            try {
                User user = userRepository.findByuId(userId);
                if (user == null) {
                    throw new RuntimeException("User not found");
                }
    
                HotelEntity hotel = hotelRepository.findById(hotelId)
                        .orElseThrow(() -> new RuntimeException("Hotel not found"));
    
                List<HotelRoomEntity> availableRooms = hotelRoomRepository.findAvailableRooms(
                        hotelId, startDate, endDate, dogType);
    
                if (availableRooms.isEmpty()) {
                    return ReservationResponseDto.roomNotAvailable();
                }
    
                ReservationEntity reservationEntity = new ReservationEntity();
            reservationEntity.setUser(user);
            reservationEntity.setHotel(hotel);
            reservationEntity.setHotelRoom(availableRooms.get(0)); // 첫 번째 가용 객실 선택
            reservationEntity.setStartDate(startDate);
            reservationEntity.setEndDate(endDate);
            reservationEntity.setDogType(dogType);
            // requestDto에서 나머지 정보 설정
            reservationEntity.setReservationName(requestDto.getReservationName());
            reservationEntity.setReservationTel(requestDto.getReservationTel());
            reservationEntity.setDogName(requestDto.getDogName());
            reservationEntity.setDogAge(requestDto.getDogAge());
            reservationEntity.setDogWeight(requestDto.getDogWeight());
            reservationEntity.setDogGender(requestDto.getDogGender());
            reservationEntity.setDogBreed(requestDto.getDogBreed());
            reservationEntity.setSpecialRequests(requestDto.getSpecialRequests());
            reservationEntity.setAgreedPersonal(requestDto.getAgreedPersonal());
            reservationEntity.setPaymentStatus(PaymentStatus.COMPLETED);

            reservationEntity = reservationRepository.save(reservationEntity);

            updateRoomAvailability(availableRooms);

            BigDecimal totalAmount = calculateReservationAmount(hotelId, startDate, endDate, dogType);

            return ReservationResponseDto.success(reservationEntity.getReservationId(), totalAmount);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
    }

    // 예약 금액 계산
    @Override
    public BigDecimal calculateReservationAmount(Long hotelId, LocalDate startDate, LocalDate endDate, DogType dogType) {
        List<HotelRoomEntity> availableRooms = hotelRoomRepository.findAvailableRooms(hotelId, startDate, endDate, dogType);

        logger.info("Available rooms: {}", availableRooms.size());
        logger.info("Start date: {}, End date: {}", startDate, endDate);

        if (availableRooms.isEmpty()) {
            throw new RuntimeException("No available rooms for the selected dates and dog type.");
        }

        long nights = ChronoUnit.DAYS.between(startDate, endDate);
        logger.info("Number of nights: {}", nights);

        BigDecimal totalPrice = availableRooms.stream()
                .map(HotelRoomEntity::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        logger.info("Total price for all rooms: {}", totalPrice);

        BigDecimal averagePricePerNight = totalPrice.divide(BigDecimal.valueOf(availableRooms.size()), 2, RoundingMode.HALF_UP);
        logger.info("Average price per night: {}", averagePricePerNight);

        BigDecimal finalPrice = averagePricePerNight.multiply(BigDecimal.valueOf(nights));
        logger.info("Final price: {}", finalPrice);

        return finalPrice;
    }

    // 객실 가용성 업데이트
    private void updateRoomAvailability(List<HotelRoomEntity> rooms) {
        for (HotelRoomEntity room : rooms) {
            if (!room.decreaseAvailableRooms()) {
                throw new RuntimeException("No available rooms for " + room.getDate());
            }
            hotelRoomRepository.save(room);
        }
    }

    

}