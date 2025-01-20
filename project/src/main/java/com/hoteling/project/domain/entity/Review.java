package com.hoteling.project.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;  // 롬복 사용을 위한 import

@Entity
@Table(name = "reviews")
@Data  
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략으로 설정
    private Long review_id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    private String content;

    @ManyToOne // User와의 관계 설정
    @JoinColumn(name = "user_id")
    private User user;  // User 엔티티와의 관계로 변경

    private int review_star;

    private LocalDateTime created_at;  // LocalDateTime으로 변경
    private LocalDateTime updated_at;  // LocalDateTime으로 변경
    
    
    private String imageUrl;// 이미지 URL을 저장할 필드

    // 기본 생성자
    public Review() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate // 업데이트 전에 호출되는 메서드
    public void preUpdate() {
        this.updated_at = LocalDateTime.now(); // 수정 시 현재 시간으로 업데이트
    }
}
