package com.hoteling.project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.HotelImageEntity;
import com.hoteling.project.domain.entity.HotelRoomEntity;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.repository.HotelListRepository;
import com.hoteling.project.service.HotelListService;
import com.hoteling.project.service.UserService;
import com.hoteling.project.service.WishlistService;

@Controller
public class HotelListController {

    @Autowired
    private HotelListService hotelListService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private HotelListRepository hotelListRepository;

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
    
    // 전체 목록 불러오기
    @GetMapping("/hotelAllList")
    public String hotelAllList(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "dogType", required = false) DogType dogType,
            Model model,
            Authentication auth) throws Exception {

        model.addAttribute("loginType", "security-login");
        addUserAttributes(model, auth);

        // 날짜와 강아지 타입 설정
        // 시작 날짜와 종료 날짜 설정
        LocalDate searchStartDate = (startDate == null) ? LocalDate.now() : startDate;
        LocalDate searchEndDate = (endDate == null) ? searchStartDate.plusDays(1) : endDate; // 기본 종료일: 시작일 + 1일
        DogType selectedDogType = (dogType == null) ? DogType.SMALL : dogType;

        // 전체 호텔 조회
        List<HotelEntity> hotels = hotelListService.getAllHotelList(); // 모든 호텔 가져오는 서비스 메소드

        // 페이지네이션 처리
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "hotelId"));
        int totalHotels = hotels.size(); // 전체 호텔 수

        // 페이지 처리
        int startIndex = Math.min(page * size, totalHotels);
        int endIndex = Math.min(startIndex + size, totalHotels);
        List<HotelEntity> paginatedHotels = hotels.subList(startIndex, endIndex);

        // PageImpl 객체 생성
        Page<HotelEntity> hotelPage = new PageImpl<>(paginatedHotels, pageable, totalHotels);

        // 모델에 추가
        model.addAttribute("hotelList", hotelPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", hotelPage.getTotalPages());

        // 페이지 번호 리스트 생성
        int totalPages = hotelPage.getTotalPages();
        int pageBlock = 5; // 한번에 표시할 페이지 번호 수
        int startPage = Math.max(0, page - pageBlock / 2); // 시작 페이지 번호
        int endPage = Math.min(startPage + pageBlock - 1, totalPages - 1); // 마지막 페이지 번호

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        // 각 호텔에 대해 사용 가능한 방을 가져오는 서비스 호출
        Map<HotelEntity, List<HotelRoomEntity>> hotelRoomMap = new HashMap<>();

        for (HotelEntity hotel : hotels) {
            List<HotelRoomEntity> rooms = hotel.getRooms().stream()
                    .filter(room -> room.isAvailable() && room.getDogType() == selectedDogType && room.isRoomAvailableForDateRange(searchStartDate, searchEndDate))
                    .collect(Collectors.toList());
            
            // 방의 가격을 모델에 추가
            for (HotelRoomEntity room : rooms) {
                System.out.println("Room Price: " + room.getPrice()); // 가격 로그 출력
            }
            
            hotelRoomMap.put(hotel, rooms);
        }

        // 모델에 추가
        model.addAttribute("availableRooms", hotelRoomMap); // 호텔별 방 정보를 모델에 추가

        return "hotel_AllList";
    }

    // 검색된 호텔만 가져오는 메소드
    @GetMapping("/hotelList")
    public String hotelList(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "12") int size,
        @RequestParam(value = "startDate", required = false) LocalDate startDate,
        @RequestParam(value = "endDate", required = false) LocalDate endDate,
        @RequestParam(value = "dogType", required = false) DogType dogType,
        @RequestParam(value = "dogWeight1", required = false, defaultValue = "0") int dogWeight1,
        @RequestParam(value = "dogWeight2", required = false, defaultValue = "0") int dogWeight2,
        @RequestParam(value = "dogWeight3", required = false, defaultValue = "0") int dogWeight3,
        @RequestParam(value = "filter", required = false) String filter,
        Model model,
        Authentication auth) throws Exception {

    model.addAttribute("loginType", "security-login");
    model.addAttribute("pageName", "쉼, 독 : 호텔");
    addUserAttributes(model, auth);

    LocalDate searchStartDate = (startDate == null) ? LocalDate.now() : startDate;
    LocalDate searchEndDate = (endDate == null) ? searchStartDate.plusDays(1) : endDate; // endDate는 체크아웃일임
    DogType selectedDogType = (dogType == null) ? DogType.SMALL : dogType;

    Map<DogType, Integer> dogTypes = new HashMap<>();
    if (dogWeight1 > 0) dogTypes.put(DogType.SMALL, dogWeight1);
    if (dogWeight2 > 0) dogTypes.put(DogType.MEDIUM, dogWeight2);
    if (dogWeight3 > 0) dogTypes.put(DogType.LARGE, dogWeight3);

    // 필터 파라미터에 따라 호텔 리스트 가져오기
    Page<HotelEntity> hotelPage;
    Pageable pageable = PageRequest.of(page, size);

    // 필터에 따라 다른 서비스 호출
    if ("wishCount".equals(filter)) {
        hotelPage = hotelListService.getHotelsByWishCount(keyword, pageable);
    } else if ("price_asc".equals(filter)) {
        hotelPage = hotelListService.getFindHotelRoomEntityByPriceAsc(keyword, searchStartDate, searchEndDate, selectedDogType, pageable);
    } else if ("price_desc".equals(filter)) {
        hotelPage = hotelListService.getFindHotelRoomEntityByPriceDesc(keyword, searchStartDate, searchEndDate, selectedDogType, pageable);
    } else {
        // 키워드로 호텔 검색 및 페이지 처리
        hotelPage = hotelListService.findHotelsByKeyword(keyword, dogTypes, searchStartDate, searchEndDate, pageable);
    }

    model.addAttribute("hotelList", hotelPage.getContent());
    model.addAttribute("currentPage", page);
    model.addAttribute("totalPages", hotelPage.getTotalPages());
    model.addAttribute("filter", filter);
    model.addAttribute("keyword", keyword);
    model.addAttribute("totalHotelsCount", hotelPage.getTotalElements());

    // 페이지 번호 계산
    int totalPages = hotelPage.getTotalPages();
    int pageBlock = 5;
    int startPage = Math.max(0, page - pageBlock / 2);
    int endPage = Math.min(startPage + pageBlock - 1, totalPages - 1);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    // 각 호텔에 대해 사용 가능한 방을 가져오는 서비스 호출
    Map<Long, List<HotelRoomEntity>> hotelRoomMap = new HashMap<>();
    for (HotelEntity hotel : hotelPage.getContent()) {
        List<HotelRoomEntity> rooms = hotel.getRooms().stream()
                .filter(room -> {
                    boolean isAvailable = room.isAvailable();
                    boolean matchesDogType = room.getDogType() == selectedDogType;
                    boolean isAvailableForDates = room.isRoomAvailableForDateRange(searchStartDate, searchEndDate);
                    
//                    // 로그 출력
//                    System.out.println("Room ID: " + room.getRoomId() + 
//                                       ", Available: " + isAvailable + 
//                                       ", Dog Type Match: " + matchesDogType + 
//                                       ", Available for Dates: " + isAvailableForDates);
                    
                    return isAvailable && matchesDogType && isAvailableForDates;
                })
                .collect(Collectors.toList());

        // 방이 없을 경우 로그 출력
        if (rooms.isEmpty()) {
            System.out.println("No available rooms for hotel ID: " + hotel.getHotelId());
        } else {
            System.out.println("Available rooms for hotel ID " + hotel.getHotelId() + ": " + rooms);
        }

        hotelRoomMap.put(hotel.getHotelId(), rooms);
    }
//
//    // 검색 키워드 및 날짜 정보 로그
//    System.out.println("Keyword: " + keyword);
//    System.out.println("Dog Types: " + dogTypes);
//    System.out.println("Start Date: " + searchStartDate);
//    System.out.println("End Date: " + searchEndDate);

    model.addAttribute("startDate", searchStartDate);
    model.addAttribute("endDate", searchEndDate);
    model.addAttribute("date", searchStartDate);
    model.addAttribute("availableRooms", hotelRoomMap);
    
    if (auth != null) { // 사용자가 로그인했는지 확인
        String uId = auth.getName();
        Long userId = userService.getUserIdByUId(uId);
        List<Long> wishlist = wishlistService.getWishlist(userId); // 찜한 호텔 ID 목록
        model.addAttribute("wishlist", wishlist); // 찜 목록을 모델에 추가
    }

    return "hotel_list";
}

	private List<HotelImageEntity> getHotelFileList(List<HotelEntity> hotels) {
		List<HotelImageEntity> hotelImageList = new ArrayList<>();
		for (HotelEntity hotel : hotels) {
	        try {
	            List<HotelImageEntity> images = hotelListService.getFindHotelImageEntityByHotelId(hotel);
	            hotelImageList.addAll(images);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
		return hotelImageList;
	}
}
