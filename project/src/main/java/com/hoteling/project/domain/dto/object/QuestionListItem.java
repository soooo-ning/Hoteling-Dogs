package com.hoteling.project.domain.dto.object;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListItem {

  private String userId;
  private LocalDateTime questionDate;
  private String content;
  private String answer;

}