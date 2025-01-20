package com.hoteling.project.common;

public interface ResponseCode {

  // HTTP Status 200
  public static final String SUCCESS = "SU";
  public static final String NO_AVAILABLE_ROOMS = "NAR";

  // HTTP Status 400
  public static final String VALIDATION_FAILED = "VF";
  public static final String NOT_EXISTED_HOTEL = "NH";
  public static final String NOT_EXISTED_DATETIME = "NT";
  public static final String NOT_EXISTED_DOGTYPE = "ND";
  public static final String NOT_EXISTED_RESERVATION = "NR";
  public static final String NOT_EXISTED_PAYMENT = "NP";
  public static final String DUPLICATE_USERID = "DU";
  public static final String RESERVATION_FAIL = "RF";
  public static final String PAYMENT_FAIL = "PF";
  public static final String ROOM_NOT_AVAILABLE = "RNA";
  public static final String DUPLICATE_PAYMENT = "DP";
  public static final String PAYMENT_AMOUNT_MISMATCH = "PAM";

  // HTTP Status 401
  public static final String NOT_EXISTED_USER = "NU";
  public static final String AUTHORIZATION_FAILED = "AF";
  public static final String JWT_EXPIRED = "JE";
  public static final String SIGN_IN_FAIL = "SF";

  // HTTP Status 500
  public static final String DATABASE_ERROR = "DBE";
  public static final String EXTERNAL_API_ERROR = "EAE";

}
