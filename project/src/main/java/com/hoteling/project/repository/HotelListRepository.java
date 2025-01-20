package com.hoteling.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoteling.project.domain.entity.HotelEntity;


public interface HotelListRepository extends JpaRepository<HotelEntity, Long> {
    
	@Query(value = "SELECT * FROM hotels ORDER BY hotel_name ASC", nativeQuery = true)
    List<HotelEntity> findAllHotelList();

    @Query(value = "SELECT * FROM hotels WHERE hotel_id = :hotelId", nativeQuery = true)
    Optional<HotelEntity> findHotelEntityByHotelId(@Param("hotelId") Long hotelId);
    
    @Query(value = "SELECT * FROM hotels WHERE hotel_name LIKE CONCAT('%', :keyword, '%') OR content LIKE CONCAT('%', :keyword, '%') OR location LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<HotelEntity> findHotelsByKeyword(@Param("keyword") String keyword);
    
    //페이지 역순으로
    Page<HotelEntity> findAllByOrderByHotelIdDesc(Pageable pageable);

 
    // 찜 많은 순으로 호텔 리스트 찾기 (hotelName, location, content로 검색 가능하게 확장)
    @Query("SELECT h FROM hotel h LEFT JOIN Wish w ON w.hotel = h " +
           "WHERE (:keyword IS NULL OR :keyword = '' " +
           "OR h.hotelName LIKE %:keyword% " +
           "OR h.location LIKE %:keyword% " +
           "OR h.content LIKE %:keyword%) " +
           "GROUP BY h ORDER BY COUNT(w) DESC")
    Page<HotelEntity> findHotelsOrderByWishCountWithKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT h FROM hotel h LEFT JOIN Wish w ON w.hotel = h GROUP BY h.hotelId ORDER BY COUNT(w) DESC")
    Page<HotelEntity> findHotelsByWishlistCount(Pageable pageable);

}
