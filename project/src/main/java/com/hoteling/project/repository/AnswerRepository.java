package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.AnswerEntity;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
	List<AnswerEntity> findByQuestion_QuestionId(Long questionId);
	List<AnswerEntity> findByhotel_HotelId(Long hotelId);
}
