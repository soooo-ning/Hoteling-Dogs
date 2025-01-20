package com.hoteling.project.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.HotelRoomEntity;

@Repository
public interface HotelRoomRepository extends JpaRepository<HotelRoomEntity, Long> {

	@Query("SELECT r FROM HotelRoomEntity r WHERE r.hotel.hotelId = :hotelId " +
	"AND r.date BETWEEN :startDate AND :endDate " +
	"AND r.dogType = :dogType " +
	"AND r.availableRooms > 0")
List<HotelRoomEntity> findAvailableRooms(@Param("hotelId") Long hotelId,
																							 @Param("startDate") LocalDate startDate,
																							 @Param("endDate") LocalDate endDate,
																							 @Param("dogType") DogType dogType);
	
		List<HotelRoomEntity> findByHotelAndDateBetweenAndDogType(
			HotelEntity hotel,
			LocalDate startDate,
			LocalDate endDate,
			DogType dogType);
	
	
	  @Query("SELECT r.hotel FROM HotelRoomEntity r WHERE r.price < :maxPrice")
	    List<HotelEntity> findHotelsUnderPrice(@Param("maxPrice") BigDecimal maxPrice);

	  
	  //가격 오름차  순으로 정렬 
	  @Query("SELECT r FROM HotelRoomEntity r WHERE " +
		       "(:keyword IS NULL OR :keyword = '' OR r.hotel.hotelName LIKE %:keyword% " +
		       "OR r.hotel.location LIKE %:keyword% OR r.hotel.content LIKE %:keyword%) " +
		       "AND r.date <= :endDate AND r.date >= :startDate " +
		       "AND r.dogType = :dogType ORDER BY r.price ASC")
		List<HotelRoomEntity> findAvailableRoomsByPriceAsc(@Param("keyword") String keyword,
		                                                   @Param("startDate") LocalDate startDate,
		                                                   @Param("endDate") LocalDate endDate,
		                                                   @Param("dogType") DogType dogType);


	  // 가격 내림차순으로 정렬 
	  @Query("SELECT r FROM HotelRoomEntity r WHERE " +
		       "(:keyword IS NULL OR :keyword = '' OR r.hotel.hotelName LIKE %:keyword% " +
		       "OR r.hotel.location LIKE %:keyword% OR r.hotel.content LIKE %:keyword%) " +
		       "AND r.date <= :endDate AND r.date >= :startDate " +
		       "AND r.dogType = :dogType ORDER BY r.price DESC")
		List<HotelRoomEntity> findAvailableRoomsByPriceDesc(@Param("keyword") String keyword,
		                                                    @Param("startDate") LocalDate startDate,
		                                                    @Param("endDate") LocalDate endDate,
		                                                    @Param("dogType") DogType dogType);

	
	}
