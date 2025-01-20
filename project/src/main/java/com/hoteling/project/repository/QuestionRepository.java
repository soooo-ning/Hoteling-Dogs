package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    List<QuestionEntity> findTop5ByHotel_HotelIdOrderByQuestionDateDesc(Long hotelId);
    List<QuestionEntity> findByHotel_HotelId(Long hotelId);
}
