package com.hoteling.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoteling.project.domain.entity.AnswerEntity;
import com.hoteling.project.domain.entity.QuestionEntity;
import com.hoteling.project.repository.AnswerRepository;
import com.hoteling.project.repository.QuestionRepository;

@Service
public class QnaService {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private AnswerRepository answerRepository;

    public List<QuestionEntity> getQuestionsByHotel(Long hotelId) {
        return questionRepository.findByHotel_HotelId(hotelId);
    }

    public List<AnswerEntity> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestion_QuestionId(questionId);
    }
}
