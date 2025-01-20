package com.hoteling.project.repository;

import com.hoteling.project.domain.entity.HotelImageEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelListImageRepository extends JpaRepository<HotelImageEntity, Integer> {

    @Query(value = "SELECT * FROM hotel_images WHERE hotel_id = :hotel_id ORDER BY hotel_id asc", 
           nativeQuery = true)
    public List<HotelImageEntity> findHotelImageEntityByHotelId(@Param("hotel_id") Long hotelId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM hotel_images WHERE hotel_id = :hotel_id", nativeQuery = true)
    Integer deleteHotelImageEntityByHotelId(@Param("hotel_id") Long hotelId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE hotel_images SET h_file_name = :h_file_name, h_file = :h_file WHERE hotel_id = :hotel_id", 
           nativeQuery = true)
    int updateHotelFile(@Param("h_file_name") String hFileName, 
                        @Param("h_file") byte[] hFile, 
                        @Param("hotel_id") Long hotelId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE hotel_images SET h_title_filename = :h_title_filename, h_title_file = :h_title_file WHERE hotel_id = :hotel_id", 
           nativeQuery = true)
    int updateHotelFileTitle(@Param("h_title_filename") String hTitleFileName, 
                             @Param("h_title_file") byte[] hTitleFile, 
                             @Param("hotel_id") Long hotelId);
}
