package com.hoteling.project.domain.dto.object;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListItem {

  private String userId;
  private String profileImage;
  private BigDecimal rating;
  private LocalDateTime reviewDate;
  private String content;

}