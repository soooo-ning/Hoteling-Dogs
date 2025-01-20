package com.hoteling.project.service;

import java.util.List;

import com.hoteling.project.domain.entity.HotelEntity;

public interface WishlistService {
	void addToWishlist (Long hotel, Long user);
	void removeFromWishlist (Long hotel, Long user);
	List<Long> getWishlist(Long user);
	List<HotelEntity> getHotelsFromWishlist(Long userId); // 추가 
	List<HotelEntity> getMostWishedHotels();
}
