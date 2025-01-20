package com.hoteling.project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoteling.project.domain.entity.AnswerEntity;
import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.Event;
import com.hoteling.project.domain.entity.EventFile;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.HotelImageEntity;
import com.hoteling.project.domain.entity.HotelRoomEntity;
import com.hoteling.project.domain.entity.QuestionEntity;
import com.hoteling.project.domain.entity.Review;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.service.EventService;
import com.hoteling.project.service.HotelListService;
import com.hoteling.project.service.QnaService;
import com.hoteling.project.service.ReviewService;
import com.hoteling.project.service.UserService;
import com.hoteling.project.service.WishlistService;

@Controller
public class HotelController {
	@Autowired
	private UserService userService;

	@Autowired
	private HotelListService hotelListService;

	@Autowired
	private EventService eventService;

	@Autowired
	private QnaService qnaService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private WishlistService wishlistService;

	// 공통 메소드: 로그인된 사용자 정보를 모델에 추가
	private void addUserAttributes(Model model, Authentication auth) {
		if (auth != null && auth.isAuthenticated()) {
			String loginedId = auth.getName();
			User loginedUser = userService.getLoginUserByUId(loginedId);
			if (loginedUser != null) {
				model.addAttribute("uId", loginedUser.getUId());
				model.addAttribute("uName", loginedUser.getUName());
			}
		}
	}

	@GetMapping("/hotel/{hotelId}")
	public String hotelDetail(@PathVariable("hotelId") Long hotelId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "startDate", required = false) LocalDate startDate,
			@RequestParam(value = "endDate", required = false) LocalDate endDate,
			@RequestParam(value = "dogType", required = false) DogType dogType,
			@RequestParam(value = "dogWeight1", required = false, defaultValue = "0") int dogWeight1,
			@RequestParam(value = "dogWeight2", required = false, defaultValue = "0") int dogWeight2,
			@RequestParam(value = "dogWeight3", required = false, defaultValue = "0") int dogWeight3,
			Model model, Authentication auth) {
		// 로그인된 사용자 정보 추가
		addUserAttributes(model, auth);

		// hotelId를 사용하여 호텔 세부 정보 가져오기
		HotelEntity hotel = hotelListService.findHotelById(hotelId);

		// 호텔 정보(이미지, 방) 가져오기
		List<HotelImageEntity> hotelImages = new ArrayList<>();
		List<HotelRoomEntity> availableRooms = new ArrayList<>();
		try {
			// hotelId로 호텔 이미지 가져오기
			hotelImages = hotelListService.getFindHotelImageEntityByHotelId(hotel);

			// 호텔의 사용 가능한 방 가져오기
			availableRooms = hotel.getRooms().stream()
					.filter(room -> room.isAvailable() && room.isRoomAvailableForDateRange(startDate, endDate))
					.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace(); // 예외 처리: 로그 출력
		}

		// 모델에 호텔 정보 추가
		model.addAttribute("hotel", hotel);
		model.addAttribute("hotelImages", hotelImages); // 이미지 정보 추가
		model.addAttribute("availableRooms", availableRooms); // 사용 가능한 방 정보 추가

		// 이벤트 정보 가져오기
		List<Event> events = eventService.getAllEvents(); // 모든 이벤트 가져오기
		List<EventFile> eventFiles = new ArrayList<>();

		try {
			// 각 이벤트에 대해 이벤트 파일 가져오기
			for (Event event : events) {
				List<EventFile> files = eventService.getFindEventFileByEventId(event); // 각 이벤트의 파일 가져오기
				eventFiles.addAll(files); // 모든 파일을 리스트에 추가
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("events", events); // 이벤트 정보 추가
		model.addAttribute("eventFiles", eventFiles); // 이벤트 파일 추가

		// 호텔에 대한 질문과 답변 가져오기
		List<QuestionEntity> questions = qnaService.getQuestionsByHotel(hotelId);
		Map<Long, List<AnswerEntity>> answersMap = new HashMap<>();

		for (QuestionEntity question : questions) {
			List<AnswerEntity> answers = qnaService.getAnswersByQuestion(question.getQuestionId());
			answersMap.put(question.getQuestionId(), answers);
		}

		model.addAttribute("questions", questions);
		model.addAttribute("answersMap", answersMap);

		// 리뷰 가져오기
		List<Review> reviews = reviewService.getReviewsByHotelId(hotelId);

		// 리뷰가 없는 경우 빈 리스트로 처리
		if (reviews == null || reviews.isEmpty()) {
			reviews = Collections.emptyList();
		}

		// 리뷰 개수 구하기
		int reviewCount = reviews.size();

		// 리뷰가 있을 경우 평균 평점 계산, 없으면 0.0
		double averageRating = reviews.stream()
				.mapToDouble(Review::getReview_star)
				.average()
				.orElse(0.0);

		if (auth != null) { // 사용자가 로그인했는지 확인
			String uId = auth.getName();
			Long userId = userService.getUserIdByUId(uId);
			List<Long> wishlist = wishlistService.getWishlist(userId); // 찜한 호텔 ID 목록
			model.addAttribute("wishlist", wishlist); // 찜 목록을 모델에 추가
		}
		model.addAttribute("reviewCount", reviewCount);
		model.addAttribute("reviews", reviews); // 리뷰 추가
		model.addAttribute("averageRating", averageRating); // 평균 평점 추가

		model.addAttribute("keyword", keyword);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("dogType", dogType);
		model.addAttribute("dogWeight1", dogWeight1);
		model.addAttribute("dogWeight2", dogWeight2);
		model.addAttribute("dogWeight3", dogWeight3);

		model.addAttribute("loginType", "security-login");
		model.addAttribute("pageName", "쉼, 독 : " + hotel.getHotelName()); // 페이지 이름을 호텔 이름으로 설정

		return "hotel-detail";
	}

	@GetMapping("/hotel/availability")
	@ResponseBody
	public ResponseEntity<?> checkAvailability(@RequestParam("hotelId") Long hotelId,
					@RequestParam("startDate") LocalDate startDate,
					@RequestParam("endDate") LocalDate endDate,
					@RequestParam("dogType") DogType dogType) {
			try {
					List<HotelRoomEntity> availableRooms = hotelListService.findAvailableRooms(hotelId, startDate, endDate, dogType);
	
					Map<String, Object> response = new HashMap<>();
					response.put("code", "SU");
					response.put("availableRoom", availableRooms.stream()
							.map(room -> {
									Map<String, Object> roomData = new HashMap<>();
									roomData.put("roomId", room.getRoomId());
									roomData.put("availableRooms", room.getAvailableRooms());
									roomData.put("price", room.getPrice());
									return roomData;
							})
							.collect(Collectors.toList()));
	
					return ResponseEntity.ok(response);
			} catch (Exception e) {
					Map<String, Object> errorResponse = new HashMap<>();
					errorResponse.put("code", "ER");
					errorResponse.put("message", "An error occurred: " + e.getMessage());
	
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}
	}

}