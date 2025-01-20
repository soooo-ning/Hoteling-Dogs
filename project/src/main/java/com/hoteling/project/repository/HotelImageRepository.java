package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.HotelImageEntity;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImageEntity, Long> {

  List<HotelImageEntity> findByHotel_HotelId(Long hotelId);

}
