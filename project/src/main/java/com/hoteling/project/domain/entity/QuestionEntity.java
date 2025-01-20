package com.hoteling.project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="question")
@Table(name="questions")
public class QuestionEntity {
    
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long questionId;

  @ManyToOne
  @JoinColumn(name = "hotel_id", nullable = false)
  private HotelEntity hotel;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime questionDate;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
  private List<AnswerEntity> answers;

}
