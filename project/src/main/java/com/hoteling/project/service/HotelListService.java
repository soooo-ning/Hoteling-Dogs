package com.hoteling.project.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hoteling.project.domain.entity.DogType;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.HotelImageEntity;
import com.hoteling.project.domain.entity.HotelRoomEntity;
import com.hoteling.project.repository.HotelListImageRepository;
import com.hoteling.project.repository.HotelListRepository;
import com.hoteling.project.repository.HotelRoomRepository;

@Service
public class HotelListService {

    @Autowired
    private HotelListRepository hotelListRepository;

    @Autowired
    private HotelListImageRepository hotelListImageRepository;
    
    @Autowired
    private HotelRoomRepository hotelRoomRepository;
    
    // 전체 호텔 리스트
    public List<HotelEntity> getAllHotelList() {
        return hotelListRepository.findAllHotelList();
    }

    // 페이징 처리된 호텔 리스트를 역순으로 반환하는 메서드 (Pageable 추가)
    public Page<HotelEntity> getAllHotelListSort(Pageable pageable) {
    	  return hotelListRepository.findAllByOrderByHotelIdDesc(pageable); // findAll 메서드는 기본적으로 Pageable을 지원
    }
    // 호텔 ID로 호텔을 조회하는 메서드
    public HotelEntity findHotelById(Long hotelId) {
        return hotelListRepository.findById(hotelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 호텔이 존재하지 않습니다. ID: " + hotelId));
    }
    
 // 키워드로 호텔 찾기
    public Page<HotelEntity> findHotelsByKeyword(String keyword, Map<DogType, Integer> dogTypes, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        List<HotelEntity> hotels = hotelListRepository.findHotelsByKeyword(keyword);

        // 필터링 로직
        List<HotelEntity> filteredHotels = hotels.stream()
            .filter(hotel -> hotel.getRooms() != null && 
                             hotel.getRooms().stream().anyMatch(HotelRoomEntity::isAvailable))
            .filter(hotel -> {
                for (Map.Entry<DogType, Integer> entry : dogTypes.entrySet()) {
                    DogType type = entry.getKey();
                    int count = entry.getValue();
                    
                    if (count > 0) {
                        boolean hasRoom = hotel.getRooms().stream()
                            .filter(room -> room.getDogType() == type)
                            .peek(room -> {
                                boolean isAvailable = room.isRoomAvailableForDateRange(startDate, endDate);
                                System.out.println("Filtering - Room ID: " + room.getRoomId() + 
                                                   ", Dog Type: " + room.getDogType() + 
                                                   ", Available: " + isAvailable + 
                                                   ", Available Rooms: " + room.getAvailableRooms());
                            })
                            .anyMatch(room -> room.isRoomAvailableForDateRange(startDate, endDate) && room.getAvailableRooms() >= count);
                        
                        if (!hasRoom) {
                            return false; // 해당 강아지 타입의 방이 없다면 필터링
                        }
                    }
                }
                return true; // 모든 강아지 타입의 방이 가능한 경우
            })
            .collect(Collectors.toList());
        
        // 페이지 처리
        int totalHotels = filteredHotels.size();
        int startIndex = Math.min(pageable.getPageNumber() * pageable.getPageSize(), totalHotels);
        int endIndex = Math.min(startIndex + pageable.getPageSize(), totalHotels);
        
        // 페이지네이션된 결과 리스트
        List<HotelEntity> paginatedHotels = filteredHotels.subList(startIndex, endIndex);
        
        // PageImpl 반환
        return new PageImpl<>(paginatedHotels, pageable, totalHotels);
    }

   
    // 호텔아이디로 호텔이미지 찾기
    public List<HotelImageEntity> getFindHotelImageEntityByHotelId(HotelEntity hotel) throws Exception {
        return (List<HotelImageEntity>) hotelListImageRepository.findHotelImageEntityByHotelId(hotel.getHotelId());
    }
    
    public List<HotelEntity> findHotelsUnderPrice(BigDecimal maxPrice) {
        return hotelRoomRepository.findHotelsUnderPrice(maxPrice); // 홈화면에서 가성비 숙소 추천 
    }
    
    
    //가격 오름 차순 정렬
    public Page<HotelEntity> getFindHotelRoomEntityByPriceAsc(String keyword, LocalDate startDate, LocalDate endDate, DogType dogType, Pageable pageable) {
        // 호텔 룸 레벨에서 모든 결과 가져오기 (Pageable 사용 X)
        List<HotelRoomEntity> allRooms = hotelRoomRepository.findAvailableRoomsByPriceAsc(keyword, startDate, endDate, dogType);

        // 호텔 룸에서 HotelEntity 추출 및 중복 제거
        List<HotelEntity> allHotelEntities = allRooms.stream()
                .map(HotelRoomEntity::getHotel)
                .distinct()
                .collect(Collectors.toList());

        // 페이지네이션을 직접 수행
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allHotelEntities.size());

        if (start > allHotelEntities.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, allHotelEntities.size());
        }

        List<HotelEntity> hotelEntitiesPage = allHotelEntities.subList(start, end);

        return new PageImpl<>(hotelEntitiesPage, pageable, allHotelEntities.size());
    }

    //가격 내림차순 정렬 
    public Page<HotelEntity> getFindHotelRoomEntityByPriceDesc(String keyword, LocalDate startDate, LocalDate endDate, DogType dogType, Pageable pageable) {
        // 호텔 룸 레벨에서 모든 결과 가져오기 (Pageable 사용 X)
        List<HotelRoomEntity> allRooms = hotelRoomRepository.findAvailableRoomsByPriceDesc(keyword, startDate, endDate, dogType);

        // 호텔 룸에서 HotelEntity 추출 및 중복 제거
        List<HotelEntity> allHotelEntities = allRooms.stream()
                .map(HotelRoomEntity::getHotel) // HotelRoomEntity에서 HotelEntity 추출
                .distinct() // 중복된 호텔 제거
                .collect(Collectors.toList());

        // 페이지네이션을 직접 수행
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allHotelEntities.size());

        if (start > allHotelEntities.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, allHotelEntities.size());
        }

        List<HotelEntity> hotelEntitiesPage = allHotelEntities.subList(start, end);

        return new PageImpl<>(hotelEntitiesPage, pageable, allHotelEntities.size());
    }



    // 찜 많은 순으로 호텔 리스트 반환
    public Page<HotelEntity> getHotelsByWishCount(String keyword, Pageable pageable) {
        return hotelListRepository.findHotelsOrderByWishCountWithKeyword(keyword, pageable);
    }
    
    public List<HotelRoomEntity> findAvailableRooms(Long hotelId, LocalDate startDate, LocalDate endDate, DogType dogType) {
        List<HotelRoomEntity> rooms = hotelRoomRepository.findAvailableRooms(hotelId, startDate, endDate, dogType);
        return rooms.stream()
            .filter(room -> room.isRoomAvailableForDateRange(startDate, endDate))
            .collect(Collectors.toList());
    }

}