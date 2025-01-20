package com.hoteling.project.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.Review;
import com.hoteling.project.repository.ReviewRepository;

public class ReviewServiceImpl {

	@Autowired
    private ReviewRepository reviewRepository;

    
    public List<Review> getReviewsByHotelId(HotelEntity hotelId) {
        return reviewRepository.findByHotel(hotelId);
    }
}