package com.hoteling.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.repository.HotelListRepository;
import com.hoteling.project.service.UserService;
import com.hoteling.project.service.WishlistService;

@Controller
@RequestMapping("/wishlist")
public class WishController {
    
    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

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
                model.addAttribute("user", loginedUser);
            }
        }
    }

    private List<HotelEntity> getHotelsFromWishlist(Long userId) {
        List<Long> wishlist = wishlistService.getWishlist(userId);
        List<HotelEntity> hotels = wishlist.stream()
                .map(hotelId -> {
                    HotelEntity hotel = hotelListRepository.findHotelEntityByHotelId(hotelId).orElse(null);
                    if (hotel == null) {
                        System.out.println("Hotel not found for ID: " + hotelId); // 추가된 디버그 메시지
                    }
                    return hotel;
                })
                .collect(Collectors.toList());
        System.out.println("Retrieved hotels from wishlist: " + hotels); // 리스트 내용 출력
        return hotels;
    }


    @PostMapping("/add")
    public String addToWishlist(@RequestParam("hotelId") Long hotelId, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            String uId = auth.getName(); 
            Long userId = userService.getUserIdByUId(uId);

            if (userId == null || hotelId == null) {
                redirectAttributes.addFlashAttribute("error", "유효하지 않은 요청입니다.");
                return "redirect:/wishlist/my-favorites";
            }

            wishlistService.addToWishlist(hotelId, userId);
            redirectAttributes.addFlashAttribute("message", "찜 목록에 추가되었습니다.");
            return "redirect:/wishlist/my-favorites"; // 리다이렉트
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "찜 목록에 추가하는 중 오류가 발생했습니다.");
            return "redirect:/wishlist/my-favorites"; // 리다이렉트
        }
    }


    @PostMapping("/remove")
    public String removeFromWishlist(@RequestParam("hotelId") Long hotelId, Authentication auth, RedirectAttributes redirectAttributes) {
        String uId = auth.getName(); 
        Long userId = userService.getUserIdByUId(uId);
        wishlistService.removeFromWishlist(hotelId, userId);
        redirectAttributes.addFlashAttribute("message", "찜 목록에서 제거되었습니다.");
        return "redirect:/wishlist/my-favorites"; // 리다이렉트
    }

    // 찜 목록 페이지 요청
    @GetMapping("/my-favorites")
    public String myFavoritePage(Model model, Authentication auth) {
        addUserAttributes(model, auth); // 사용자 정보를 모델에 추가

        String uId = auth.getName();
        Long userId = userService.getUserIdByUId(uId);

        // 호텔 목록을 가져오기
        List<HotelEntity> hotels = getHotelsFromWishlist(userId);

        // 모델에 호텔 목록 추가
        model.addAttribute("hotels", hotels);

        // 찜 목록이 없을 경우 메시지 추가
        if (hotels.isEmpty()) {
            model.addAttribute("message", "찜한 목록이 없습니다."); // 메시지를 모델에 추가
        }

        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 찜 목록");

        return "myFavorites"; // myFavorites.html 반환
    }
}
