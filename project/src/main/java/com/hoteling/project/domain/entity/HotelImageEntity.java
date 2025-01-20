package com.hoteling.project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="hotel_image")
@Table(name="hotel_images")
public class HotelImageEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Boolean isMain;

    private String hTitleFileName;

    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] hTitleFile;

    private String hFileName;

    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] hFile;
}
