package com.hoteling.project.repository.resultSet;

import java.util.List;

public interface HotelDetailResultSet {

  Long getHotelId();

  String getHotelName();

  String getHotelContent();

  String getHotelLocation();

  String getHotelCaution();

  Double getTotalStarRating();

  String getLatestReviewList();

  String getLatestQuestionList();

  String getLatestEventList();

}