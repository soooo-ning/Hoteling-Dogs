package com.hoteling.project.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.Review;
import com.hoteling.project.domain.entity.ReviewFile;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.service.HotelListService;
import com.hoteling.project.service.ReviewService;
import com.hoteling.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private HotelListService hotelListService;

    @Value("/Users/songjuhyeon/Documents/workspace-spring-tool-suite-4-4.24.0.RELEASE/Project01-11/src/main/resources/static/images") // 실제 파일 업로드 경로
    private String uploadPath;


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
    
    @GetMapping("/reviews/{hotelId}")
    public String getReviews(@PathVariable("hotelId") Long hotelId, Model model) {
        // 특정 호텔의 리뷰 데이터를 가져와서 모델에 추가
        List<Review> reviews = reviewService.getReviewsByHotelId(hotelId);
        
        // 리뷰가 있을 경우에만 평균 별점 계산
        double averageRating = 0.0;
        if (!reviews.isEmpty()) {
            averageRating = reviews.stream()
                .mapToInt(Review::getReview_star) // Review의 별점 가져오기
                .average()
                .orElse(0.0); // 평균 별점이 없을 경우 0.0으로 기본값 설정
        }

        // 리뷰 개수 가져오기
        int reviewCount = reviews.size();

        // 모델에 리뷰 목록과 평균 별점, 리뷰 개수 추가
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);
        
        // 호텔 정보 가져오기
        HotelEntity hotel = hotelListService.findHotelById(hotelId);
        model.addAttribute("hotel", hotel); // 모델에 호텔 정보 추가

        return "review_list"; // review_list.html 템플릿으로 이동
    }



    @GetMapping("/reviewForm")
    public String reviewForm(HttpServletRequest res, Model model, Authentication auth) {
        addUserAttributes(model, auth);
        return "review_form";
    }

    @PostMapping("/submitReview")
    public String submitReview(@ModelAttribute Review review, @RequestParam("image") MultipartFile image) throws Exception {
        List<ReviewFile> imgFileList = new ArrayList<>(); 
        
        // 이미지 업로드 처리
        if (!image.isEmpty()) {
            imgFileList = store(image);
        }

        // 리뷰 저장
        Review r = new Review();
        r.setContent(review.getContent());
        r.setReview_star(review.getReview_star());
        r.setHotel(review.getHotel());  // 호텔 엔티티 설정
        r.setUser(review.getUser());    // 사용자 엔티티 설정

        if (!image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            r.setImageUrl("/images/" + fileName);  // 이미지 경로 저장
        }

        reviewService.saveReview(r);
        
        // 리뷰에 연결된 이미지 파일 저장
        for (ReviewFile f : imgFileList) {
            f.setReview(r); // 리뷰와 연결
            reviewService.saveReviewFile(f);
        }

        return "redirect:/reviewList";
    }

    // 이미지 파일 저장 메소드
    public List<ReviewFile> store(MultipartFile file) throws Exception {
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image")) {
            throw new Exception("이미지 파일만 업로드 가능합니다.");
        }

        List<ReviewFile> list = new ArrayList<>();
        String orginalName = file.getOriginalFilename();
        String fileName = orginalName.substring(orginalName.lastIndexOf("\\") + 1);

        String folderPath = makeFolder();

        // UUID 생성
        String uuid = UUID.randomUUID().toString();
        String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;

        Path savePath = Paths.get(saveName);
        try {
            file.transferTo(savePath);  // 실제 이미지 저장
            
            ReviewFile rf = new ReviewFile();
            rf.setReview_filename(orginalName);
            rf.setReview_file(Base64.getEncoder().encodeToString(file.getBytes()));
            list.add(rf);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 날짜별 폴더 생성
    private String makeFolder() {
        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String folderPath = str.replace("/", File.separator);

        File uploadPathFolder = new File(uploadPath, folderPath);
        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }

    @GetMapping("/reviewList")
    public String reviewList(Model model, Authentication auth) {
        addUserAttributes(model, auth);
        
        Review review = new Review();
  // review.setUser_id(2L);  // 임의로 유저 ID 설정 (예시)
        
        List<Review> reviews = reviewService.getAllReviews();  // 모든 리뷰 가져오기
        model.addAttribute("reviews", reviews);
        
        List<ReviewFile> reviewFiles = new ArrayList<>();
        try {
            for (Review r : reviews) {
                // Review 객체에서 reviewId 추출 후 서비스 메소드 호출
                List<ReviewFile> files = reviewService.getFindReviewFileByReviewId(r.getReview_id());
                reviewFiles.addAll(files);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        model.addAttribute("reviewFiles", reviewFiles);

        return "review_list";
    }
}
