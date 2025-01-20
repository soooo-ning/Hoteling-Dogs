package com.hoteling.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.repository.resultSet.HotelDetailResultSet;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

	@Query(value = "SELECT " +
			"H.hotel_id AS hotelId, " +
			"H.hotel_name AS hotelName, " +
			"H.content AS hotelContent, " +
			"H.location AS hotelLocation, " +
			"H.caution AS hotelCaution, " +

			// 평균 별점 계산
			"COALESCE(ROUND(AVG(R.rating), 1), 0) AS totalStarRating, " +

			// 최신 리뷰 리스트
			"(SELECT JSON_ARRAYAGG(" +
			"    JSON_OBJECT('userId', R2.user_id, 'profileImage', U.profile_image, " +
			"                'rating', R2.rating, 'reviewDate', R2.review_date, " +
			"                'content', R2.content, 'reviewImages', " +
			"                (SELECT JSON_ARRAYAGG(RI.image_url) " +
			"                 FROM review_images RI WHERE RI.review_id = R2.review_id)))" +
			" FROM reviews R2 " +
			" JOIN users U ON R2.user_id = U.user_id " +
			" WHERE R2.hotel_id = H.hotel_id " +
			" ORDER BY R2.review_date DESC LIMIT 5) AS latestReviewList, " +

			// 최신 질문 리스트
			"(SELECT JSON_ARRAYAGG(" +
			"    JSON_OBJECT('userId', Q.user_id, 'questionDate', Q.question_date, " +
			"                'content', Q.content, 'answer', " +
			"                (SELECT A.answer_content FROM answers A " +
			"                 WHERE A.question_id = Q.question_id " +
			"                 ORDER BY A.answer_date DESC LIMIT 1)))" +
			" FROM questions Q " +
			" WHERE Q.hotel_id = H.hotel_id " +
			" ORDER BY Q.question_date DESC LIMIT 5) AS latestQuestionList, " +

			// 최신 이벤트 리스트
			"(SELECT JSON_ARRAYAGG(" +
			"    JSON_OBJECT('mainImageUrl', E.main_image_url, 'eventTitle', E.event_title))" +
			" FROM events E " +
			" ORDER BY E.event_id DESC LIMIT 5) AS latestEventList " +

			// 최종 쿼리
			"FROM hotels AS H " +
			"LEFT JOIN reviews AS R ON H.hotel_id = R.hotel_id " +
			"WHERE H.hotel_id = :hotelId " +
			"GROUP BY H.hotel_id", nativeQuery = true)

	HotelDetailResultSet getHotelDetail(@Param("hotelId") Long hotelId);

}