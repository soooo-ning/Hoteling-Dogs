package com.hoteling.project.common;

public interface ResponseMessage {

  // HTTP Status 200
  public static final String SUCCESS = "Success.";
  public static final String NO_AVAILABLE_ROOMS = "No available rooms for the selected dates and dog type.";

  // HTTP Status 400
  public static final String VALIDATION_FAILED = "Validation failed.";
  public static final String NOT_EXISTED_HOTEL = "This hotel does not exist.";
  public static final String NOT_EXISTED_DATETIME = "This datetime does not exist.";
  public static final String NOT_EXISTED_DOGTYPE = "This dog type does not exist.";
  public static final String NOT_EXISTED_RESERVATION = "This reservation does not exist.";
  public static final String NOT_EXISTED_PAYMENT = "This payment does not exist.";
  public static final String DUPLICATE_USERID = "Duplicate userId";
  public static final String RESERVATION_FAIL = "Reservation failed.";
  public static final String PAYMENT_FAIL = "Payment failed.";
  public static final String ROOM_NOT_AVAILABLE = "The selected room is no longer available.";
  public static final String DUPLICATE_PAYMENT = "This reservation has already been paid.";
  public static final String PAYMENT_AMOUNT_MISMATCH = "Payment amount does not match the reservation amount.";

  // HTTP Status 401
  public static final String NOT_EXISTED_USER = "This user does not exist.";
  public static final String AUTHORIZATION_FAILED = "Authorization failed.";
  public static final String JWT_EXPIRED = "JWT token expired.";
  public static final String SIGN_IN_FAIL = "Login information mismatch.";

  // HTTP Status 500
  public static final String DATABASE_ERROR = "Database error.";
  public static final String EXTERNAL_API_ERROR = "Error occurred while communicating with external API.";

}
