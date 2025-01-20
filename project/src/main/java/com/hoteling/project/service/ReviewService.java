package com.hoteling.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.Review;
import com.hoteling.project.domain.entity.ReviewFile;
import com.hoteling.project.domain.entity.ReviewReply;
import com.hoteling.project.repository.HotelRepository;
import com.hoteling.project.repository.ReviewFileRepository;
import com.hoteling.project.repository.ReviewReplyRepository;
import com.hoteling.project.repository.ReviewRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewFileRepository reviewFileRepository;

    @Autowired
    private ReviewReplyRepository reviewReplyRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Transactional
    public void saveReview(Review review) throws Exception {
        System.out.println("Saving Review: Star Rating: " + review.getReview_star() + ", Content: " + review.getContent());
        reviewRepository.save(review);
        System.out.println("Saved Review ID: " + review.getReview_id());
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll(); // 모든 리뷰 가져오기
    }

    public List<Review> getReviewUserDataList(Long userId) {
        return reviewRepository.findByUserId(userId); // 특정 유저의 리뷰 리스트
    }

    @Transactional
    public void saveReviewFile(ReviewFile reviewFile) throws Exception {
        System.out.println("Saving Review File: Filename: " + reviewFile.getReview_filename());
        reviewFileRepository.save(reviewFile);
        System.out.println("Saved Review File ID: " + reviewFile.getReviewfile_id());
    }

    public List<ReviewFile> getFindReviewFileByReviewId(Long reviewId) throws Exception {
        return reviewFileRepository.findReviewFileByReviewId(reviewId); // 리뷰 ID로 리뷰 파일 찾기
    }

    @Transactional
    public void saveReviewReply(ReviewReply reviewReply) throws Exception {
        System.out.println("Saving Review Reply: Content: " + reviewReply.getReply_content());
        reviewReplyRepository.save(reviewReply);
        System.out.println("Saved Review Reply ID: " + reviewReply.getReply_id());
    }

    public List<ReviewReply> getFindReviewReplyByReviewId(Long reviewId) throws Exception {
        return reviewReplyRepository.findReviewReplyByReviewId(reviewId); // 리뷰 ID로 댓글 찾기
    }

    public Integer deleteReviewReplyByReviewId(Long reviewId) throws Exception {
        return reviewReplyRepository.deleteReviewReplyByReviewId(reviewId); // 리뷰 ID로 댓글 삭제
    }

    @Transactional
    public void updateReview(Review review) throws Exception {
        Review existingReview = reviewRepository.findById(review.getReview_id())
            .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다. ID: " + review.getReview_id()));

        existingReview.setReview_star(review.getReview_star());
        existingReview.setContent(review.getContent());
        existingReview.setUpdated_at(review.getUpdated_at());
        
        reviewRepository.save(existingReview);
        System.out.println("Updated Review ID: " + existingReview.getReview_id());
    }

    public Integer deleteReviewFileByReviewId(Long reviewId) throws Exception {
        return reviewFileRepository.deleteReviewFileByReviewId(reviewId); // 리뷰 ID로 리뷰 파일 삭제
    }

    @Transactional
    public void deleteReview(Long reviewId) throws Exception {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다. ID: " + reviewId));

        reviewRepository.delete(review);
        System.out.println("Deleted Review ID: " + reviewId);
    }

    public List<Review> getReviewsByHotelId(Long hotelId) {
        HotelEntity hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new EntityNotFoundException("호텔을 찾을 수 없습니다. ID: " + hotelId));

        return reviewRepository.findByHotel(hotel); // 호텔의 리뷰 반환
    }
}
