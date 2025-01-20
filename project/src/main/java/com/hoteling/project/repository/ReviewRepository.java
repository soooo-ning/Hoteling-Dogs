package com.hoteling.project.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 모든 리뷰를 생성일 기준 내림차순으로 정렬하여 가져오기
    @Query(value="SELECT * FROM reviews u ORDER BY created_at DESC, review_id DESC", nativeQuery=true)
    List<Review> findAllCreatedtSort();

    // 특정 유저의 리뷰를 조회
    @Query(value="SELECT u.* FROM reviews u WHERE u.user_id = :user_id ORDER BY created_at ASC, review_id ASC", nativeQuery=true)
    List<Review> findByUserId(@Param("user_id") long user_id);

    // 리뷰 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE Review r SET r.review_star = :reviewStar, r.content = :content, r.updated_at = :updatedAt WHERE r.review_id = :reviewId")
    Integer updateReview(@Param("reviewStar") int reviewStar, @Param("content") String content, @Param("updatedAt") LocalDateTime updatedAt, @Param("reviewId") Long reviewId);

    // 리뷰 삭제
    @Modifying
    @Transactional
    @Query(value="DELETE FROM reviews WHERE review_id = :review_id", nativeQuery=true)
    Integer deleteReview(@Param("review_id") Long review_id);

    // 호텔별 리뷰 조회
    List<Review> findByHotel(HotelEntity hotel);
}
