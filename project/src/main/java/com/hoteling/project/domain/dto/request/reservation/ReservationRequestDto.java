package com.hoteling.project.domain.dto.request.reservation;

import java.math.BigDecimal;

import com.hoteling.project.domain.entity.Gender;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReservationRequestDto {

  @NotBlank
  private String reservationName;

  @NotBlank
  @Pattern(regexp = "^[0-9]{11,13}$")
  private String reservationTel;

  @NotBlank
  private String dogName;

  @NotNull
  private Integer dogAge;

  @NotNull
  private BigDecimal dogWeight;

  @NotNull
  private Gender dogGender;

  @NotBlank
  private String dogBreed;

  private String specialRequests;

  @NotNull
  @AssertTrue
  private Boolean agreedPersonal;

}
