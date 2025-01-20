package com.hoteling.project.domain.dto.response.hotelList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HotelListResponseDto {

    private Long hotelId;            // 호텔 ID
    private String hotelName;        // 호텔 이름
    private String location;         // 호텔 위치
    private BigDecimal pricePerNight; // 1박당 가격
    private String imageUrl;         // 호텔 대표 이미지 URL



}
