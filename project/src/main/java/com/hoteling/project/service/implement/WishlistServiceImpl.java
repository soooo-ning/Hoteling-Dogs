package com.hoteling.project.service.implement;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.domain.entity.Wish;
import com.hoteling.project.repository.HotelListRepository;
import com.hoteling.project.repository.UserRepository;
import com.hoteling.project.repository.WishlistRepository;
import com.hoteling.project.service.WishlistService;

@Service
@Transactional
public class WishlistServiceImpl implements WishlistService {
	
	@Autowired
	private WishlistRepository wishlistRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HotelListRepository hotelListRepository;

	@Override
	public void addToWishlist(Long hotelId, Long userId) {
		
		 // 사용자의 찜 목록에 호텔이 이미 있는지 확인
	    if (wishlistRepository.existsByHotel_HotelIdAndUser_UserId(hotelId, userId)) {
	        throw new RuntimeException("이미 찜한 호텔입니다.");
	    }
	    
		//호텔 찾기
		HotelEntity hotel = hotelListRepository.findHotelEntityByHotelId(hotelId).orElseThrow(()-> new RuntimeException("호텔을 찾을 수 없습니다."));
		
		// 사용자 찾기
		User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException ("사용자를 찾을 수 없습니다."));
		
		//찜 객체 생성 및 설정
		Wish wish = new Wish();
		wish.setHotel(hotel);
		wish.setUser(user);
		
		//찜 목록에 추가
		wishlistRepository.save(wish);
	}

	@Override
	public void removeFromWishlist(Long hotelId, Long userId) {
		// 삭제 메소드
		wishlistRepository.deleteByHotel_HotelIdAndUser_UserId(hotelId, userId);
		
	}

	@Override
	public List<Long> getWishlist(Long userId) {
		return wishlistRepository.findHotelIdsByUser_UserId(userId);
	}
	 public List<HotelEntity> getMostWishedHotels() {
	        Pageable limit = PageRequest.of(0, 4); // 4개의 호텔만 가져오기
	        return hotelListRepository.findHotelsByWishlistCount(limit).getContent(); // 4개의 호텔 반환
	    }
	 @Override
	    public List<HotelEntity> getHotelsFromWishlist(Long userId) {
	        List<Long> wishlist = getWishlist(userId); // 기존에 찜 목록을 가져오는 로직
	        return wishlist.stream()
	                .map(hotelListRepository::findHotelEntityByHotelId)
	                .filter(Optional::isPresent)
	                .map(Optional::get)
	                .collect(Collectors.toList());
	    }
	
}
