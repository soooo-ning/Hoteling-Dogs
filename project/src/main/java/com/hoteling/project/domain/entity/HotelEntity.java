package com.hoteling.project.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "hotel")
@Table(name = "hotels")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    @Column(nullable = false)
    private String hotelName;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private String location; // 고유한 위치

    @Column(length = 2000) // 주의사항은 길어질 수 있으므로 적절한 길이 설정
    private String caution;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelImageEntity> images;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelRoomEntity> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<QuestionEntity> questions;

    public HotelImageEntity getMainImage() {
        for (HotelImageEntity image : images) {
            if (image.getIsMain()) {
                return image;
            }
        }
        return null; // 기본값으로 null 반환 (메인이 없을 경우)
    }

    public String getMainImageUrl() {
        return "/images/hotel" + this.hotelId + ".jpg";
    }
}