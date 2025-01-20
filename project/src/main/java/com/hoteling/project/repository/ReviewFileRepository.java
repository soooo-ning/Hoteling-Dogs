package com.hoteling.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.ReviewFile;

public interface ReviewFileRepository extends JpaRepository<ReviewFile, Integer> {
	

    // 로그인한 회원 별 리뷰 리스트 보기.
    @Query(value="SELECT * FROM review_file u WHERE u.review_id = :review_id order by reviewfile_id desc",
    		countQuery="SELECT count(*) FROM review_file u WHERE u.review_id = :review_id"
    		, nativeQuery=true)
    public List<ReviewFile> findReviewFileByReviewId(@Param("review_id") Long review_id) ;
    

	@Transactional
	@Modifying
	@Query(value="DELETE FROM review_file WHERE review_id = :review_id", nativeQuery=true)
	Integer deleteReviewFileByReviewId(@Param("review_id") Long review_id);
	
	
}
