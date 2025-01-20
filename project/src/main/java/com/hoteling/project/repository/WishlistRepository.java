package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hoteling.project.domain.entity.Wish;

public interface WishlistRepository extends JpaRepository<Wish, Long> {
    void deleteByHotel_HotelIdAndUser_UserId(Long hotelId, Long userId);
    
    @Query("SELECT w.hotel.hotelId FROM Wish w WHERE w.user.userId = :userId")
    List<Long> findHotelIdsByUser_UserId(@Param("userId") Long userId);
    
    boolean existsByHotel_HotelIdAndUser_UserId(Long hotelId, Long userId);
}
