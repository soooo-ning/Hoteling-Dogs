package com.hoteling.project.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_rooms")
public class HotelRoomEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private HotelEntity hotel;
    
    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DogType dogType;

    @Column(nullable = false)
    private Integer availableRooms;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    
    // 가격 찍힐 때 포맷
    public String getFormattedPrice() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        return numberFormat.format(this.price) + " 원/1박"; // 가격에 '원' 추가
    }

    public boolean isAvailable() {
    	return availableRooms > 0; // 방이 하나라도 있다면 이용 가능
    }
    
    public boolean isRoomAvailableForDateRange(LocalDate startDate, LocalDate endDate) {
    	// 방의 날짜와 가용 방 상태 출력
    	 System.out.println("Checking availability for Room ID: " + this.roomId + 
                 ", Room Date: " + this.date + 
                 ", Start Date: " + startDate + 
                 ", End Date: " + endDate + 
                 ", Available Rooms: " + availableRooms);

		// 체크: 방의 날짜가 시작 날짜와 종료 날짜 사이에 있는지 확인
    boolean isDateInRange = !this.date.isBefore(startDate) && !this.date.isAfter(endDate);
    boolean isRoomAvailable = isDateInRange && isAvailable();
    
    if (isRoomAvailable) {
        System.out.println("Available on: " + this.date);
        return true;
    } else {
        System.out.println("Room not available on: " + this.date);
        return false;
    }
    }
    
	public boolean decreaseAvailableRooms() {
		if (this.availableRooms > 0) {
				this.availableRooms--;
				return true;
		}
		return false;
	}

	public void increaseAvailableRooms() {
		this.availableRooms++;
	}
}
