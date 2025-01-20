package com.hoteling.project.domain.dto.response.hotel;

import java.math.BigDecimal;

import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.HotelRoomEntity;

import lombok.Getter;

@Getter
public class HotelRoomDto {
    private DogType dogType;
    private int availableRooms;
    private BigDecimal price;

    public HotelRoomDto(HotelRoomEntity room) {
        this.dogType = room.getDogType();
        this.availableRooms = room.getAvailableRooms();
        this.price = room.getPrice();
    }
}