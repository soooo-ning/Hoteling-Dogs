package com.hoteling.project.domain.dto.response.hotel;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hoteling.project.common.ResponseCode;
import com.hoteling.project.common.ResponseMessage;
import com.hoteling.project.domain.dto.object.EventListItem;
import com.hoteling.project.domain.dto.object.QuestionListItem;
import com.hoteling.project.domain.dto.object.ReviewListItem;
import com.hoteling.project.domain.dto.response.ResponseDto;
import com.hoteling.project.domain.entity.HotelImageEntity;
import com.hoteling.project.repository.resultSet.HotelDetailResultSet;

import lombok.Getter;

@Getter
public class HotelDetailResponseDto extends ResponseDto {

	private Long hotelId;
	private String hotelName;
	private String hotelContent;
	private String hotelLocation;
	private String hotelCaution;  // 주의사항 필드 추가
	private List<String> hotelImageList;
	private List<ReviewListItem> reviewList;
	private List<QuestionListItem> questionList;
	private double totalStarRating;
	private List<EventListItem> eventList;
	private List<HotelRoomDto> availableRooms;
	private double averageRating;

	private HotelDetailResponseDto(HotelDetailResultSet resultSet,
			List<String> hotelImageList,
			List<HotelRoomDto> availableRooms,
			List<ReviewListItem> reviewList,
			List<QuestionListItem> questionList,
			List<EventListItem> eventList,
			double averageRating) {

		super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);

		this.hotelId = resultSet.getHotelId();
		this.hotelName = resultSet.getHotelName();
		this.hotelContent = resultSet.getHotelContent();
		this.hotelLocation = resultSet.getHotelLocation();
		this.hotelCaution = resultSet.getHotelCaution();
		this.hotelImageList = hotelImageList;
		this.reviewList = reviewList;
		this.questionList = questionList;
		this.totalStarRating = resultSet.getTotalStarRating();
		this.eventList = eventList;
		this.availableRooms = availableRooms;
		this.averageRating = averageRating;

	}

	// 성공 response

	public static ResponseEntity<HotelDetailResponseDto> success(
			HotelDetailResultSet resultSet,
			List<HotelImageEntity> hotelImages,
			List<HotelRoomDto> availableRooms,
			List<ReviewListItem> reviewList,
			List<QuestionListItem> questionList,
			List<EventListItem> eventList,
			double averageRating) {

		List<String> hotelImageList = hotelImages.stream()
				.map(HotelImageEntity::getImageUrl)
				.collect(Collectors.toList());

		HotelDetailResponseDto result = new HotelDetailResponseDto(
				resultSet, hotelImageList, availableRooms, reviewList, questionList, eventList, averageRating);
		return ResponseEntity.ok(result);

	}

	// 실패 response

	public static ResponseEntity<ResponseDto> notExistHotel() {
		ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_HOTEL, ResponseMessage.NOT_EXISTED_HOTEL);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> invalidDate() {
		ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DATETIME, ResponseMessage.NOT_EXISTED_DATETIME);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> invalidDogType() {
		ResponseDto result = new ResponseDto(ResponseCode.NOT_EXISTED_DOGTYPE, ResponseMessage.NOT_EXISTED_DOGTYPE);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}

	public static ResponseEntity<ResponseDto> noAvailableRooms() {
		ResponseDto result = new ResponseDto(ResponseCode.NO_AVAILABLE_ROOMS, ResponseMessage.NO_AVAILABLE_ROOMS);
		return ResponseEntity.ok(result);
	}

}