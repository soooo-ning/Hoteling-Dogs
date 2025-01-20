package com.hoteling.project.domain.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class ReviewReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reply_id;

    @Column(length = 2000)
    private String reply_content;

    @Column(updatable = false)
    private java.sql.Timestamp reply_created_at;
    private java.sql.Timestamp reply_updated_at;
    

	@OneToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

	public int getReply_id() {
		return reply_id;
	}

	public void setReply_id(int reply_id) {
		this.reply_id = reply_id;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public java.sql.Timestamp getReply_created_at() {
		return reply_created_at;
	}

	public void setReply_created_at(java.sql.Timestamp reply_created_at) {
		this.reply_created_at = reply_created_at;
	}

	public java.sql.Timestamp getReply_updated_at() {
		return reply_updated_at;
	}

	public void setReply_updated_at(java.sql.Timestamp reply_updated_at) {
		this.reply_updated_at = reply_updated_at;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
}
