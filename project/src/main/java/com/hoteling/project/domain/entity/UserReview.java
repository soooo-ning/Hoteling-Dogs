package com.hoteling.project.domain.entity;

public class UserReview extends Review {
	private int rn;
    private String review_star_txt;
    
    private Review review;

    public UserReview() {}
    
	public int getRn() {
		return rn;
	}

	public void setRn(int rn) {
		this.rn = rn;
	}

	public String getReview_star_txt() {
		return review_star_txt;
	}

	public void setReview_star_txt(String review_star_txt) {
		this.review_star_txt = review_star_txt;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}
	
}
