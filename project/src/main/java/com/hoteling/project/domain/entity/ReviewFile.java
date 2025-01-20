package com.hoteling.project.domain.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class ReviewFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewfile_id;

    private String review_filename;
    
    @Column(columnDefinition = "MEDIUMBLOB")
    private String review_file;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
    
    
    public int getReviewfileId() {
		return reviewfile_id;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public int getReviewfile_id() {
		return reviewfile_id;
	}

	public void setReviewfile_id(int reviewfile_id) {
		this.reviewfile_id = reviewfile_id;
	}

	public String getReview_filename() {
		return review_filename;
	}

	public void setReview_filename(String review_filename) {
		this.review_filename = review_filename;
	}

	public String getReview_file() {
		return review_file;
	}

	public void setReview_file(String review_file) {
		this.review_file = review_file;
	}


}
