package com.hoteling.project.controller;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hoteling.project.domain.dto.EmailDto;
import com.hoteling.project.domain.dto.JoinRequest;
import com.hoteling.project.domain.dto.LoginRequest;
import com.hoteling.project.domain.dto.PasswordChangeRequest;
import com.hoteling.project.domain.dto.UserUpdateRequest;
import com.hoteling.project.domain.entity.DogInfo;
import com.hoteling.project.domain.entity.HotelEntity;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.repository.UserRepository;
import com.hoteling.project.service.EmailService;
import com.hoteling.project.service.HotelListService;
import com.hoteling.project.service.UserService;
import com.hoteling.project.service.WishlistService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login")
public class SecurityLoginController {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityLoginController.class);

	@Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;

    
    @Autowired
    private WishlistService wishlistService;
    
    @Autowired
    private HotelListService hotelListService;
    
    @Autowired
    private UserRepository userRepository;
    
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

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) {
    	 Pageable limit = PageRequest.of(0, 4); // 4개의 호텔만 가져오기
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독");

        addUserAttributes(model, auth);
      //  Page<HotelRoomEntity> cheapHotels = hotelListService.getFindHotelRoomEntityByPriceAsc("", null, null, null, topThree);
        List<HotelEntity> cheapHotels = hotelListService.findHotelsUnderPrice(new BigDecimal(30000));
        
        // List에서 처음 4개의 요소만 가져오기
        List<HotelEntity> limitedCheapHotels = cheapHotels.stream().limit(4).collect(Collectors.toList());
        model.addAttribute("cheapHotels", limitedCheapHotels);
       
        List<HotelEntity> mostLikedHotels = wishlistService.getMostWishedHotels();
       
      //  model.addAttribute("cheapHotels", cheapHotels.getContent());
        model.addAttribute("mostLikedHotels", mostLikedHotels);
      
        return "home";
    }

    // 회원가입 페이지 요청
    @GetMapping("/join")
    public String joinPage(Model model, Authentication auth) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 회원가입");
        model.addAttribute("joinRequest", new JoinRequest());        
        addUserAttributes(model, auth);
        
        return "join";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest,
            BindingResult bindingResult, Model model, Authentication auth) {
		model.addAttribute("loginType", "security-login");
		model.addAttribute("pageName", "쉼, 독 : 회원가입");
		addUserAttributes(model, auth);
		
		// 비밀번호 확인
		if (!joinRequest.getUPw().equals(joinRequest.getUPwCheck())) {
		 bindingResult.rejectValue("uPwCheck", "error.joinRequest", "비밀번호가 일치하지 않습니다.");
		}
		
		// 아이디 중복 확인
		if (userService.checkUIdDuplicate(joinRequest.getUId())) {
		 bindingResult.rejectValue("uId", "error.joinRequest", "로그인 아이디가 중복됩니다.");
		}
		
		
		if (bindingResult.hasErrors()) {
		 return "join";
		}
		
		// 비밀번호 암호화하여 회원가입
		userService.securityJoin(joinRequest);
		
		return "redirect:/security-login";
}
 
    // 이메일 인증 코드 발송
    @PostMapping("/send-email")
    @ResponseBody
    public ResponseEntity<Map<String, String>> mailSend(@RequestBody EmailDto emailDto) throws MessagingException {
        String mail = emailDto.getMail();
        if (mail == null || mail.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 주소가 누락되었습니다."));
        }
        emailService.sendEmail(mail);
        Map<String, String> response = new HashMap<>();
        response.put("message", "인증코드가 발송되었습니다.");
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // 이메일 인증 코드 검증
    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<Map<String, String>> verify(@RequestBody EmailDto emailDto) {
        String mail = emailDto.getMail();
        String verifyCode = emailDto.getVerifyCode();
        if (mail == null || mail.isEmpty() || verifyCode == null || verifyCode.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 주소 또는 인증코드가 누락되었습니다."));
        }
        boolean isVerify = emailService.verifyEmailCode(mail, verifyCode);
        Map<String, String> response = new HashMap<>();
        if (isVerify) {
            response.put("message", "인증이 완료되었습니다.");
        } else {
            response.put("message", "인증 실패하셨습니다.");
        }
        return ResponseEntity.ok(response);
    }

    // 로그인 페이지 요청
    @GetMapping("/login")
public String loginPage(Model model, Authentication authentication) {
    if (authentication != null && authentication.isAuthenticated()) {
        model.addAttribute("isAuthenticated", true);
        model.addAttribute("username", authentication.getName());
    } else {
        model.addAttribute("isAuthenticated", false);
    }
    model.addAttribute("loginRequest", new LoginRequest());
    model.addAttribute("loginType", "security-login");
    model.addAttribute("pageName", "로그인");
    return "login";
}
    

    // 마이페이지 요청
    @GetMapping("/info")
    public String memberInfo(Model model, Authentication auth) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 마이페이지");
        addUserAttributes(model, auth);

        // JWT에서 사용자 정보 추출
        String loginedId;
        if (auth != null) {
            loginedId = auth.getName(); // 이미 인증된 사용자
        } else {
            return "redirect:/security-login/login"; // 인증되지 않은 경우 로그인 페이지로 리디렉션
        }

        String uId = auth.getName();
        Long userId = userService.getUserIdByUId(uId);

        User loginedUser = userService.getLoginUserByUId(loginedId);

        if (loginedUser == null) {
            return "redirect:/security-login/login"; // 사용자 정보가 없으면 로그인 페이지로 리디렉션
        }
        // 호텔 목록을 가져오기
        List<HotelEntity> hotels = wishlistService.getHotelsFromWishlist(userId);
        // 가장 최근에 찜한 호텔 List 만 가져오기
        Collections.reverse(hotels);

        // 모델에 호텔 목록 추가
        model.addAttribute("hotels", hotels);

        // 찜 목록이 없을 경우 메시지 추가
        if (hotels.isEmpty()) {
            model.addAttribute("message", "찜한 목록이 없습니다.");
        }
        

        model.addAttribute("user", loginedUser);

        // 강아지 정보를 조회
        List<DogInfo> dogInfos = userService.getDogInfosByUser(loginedUser);
        model.addAttribute("dogInfos", dogInfos);

        return "info";
    }
    
    // 개인정보수정 페이지 요청
    @GetMapping("/info/modify")
    public String modifyInfoPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("user", loginedUser);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 개인정보수정");

        return "infoModify";
    }
    
    // 계정정보수정 페이지 요청
    @GetMapping("/info/modify-form")
    public String showModifyFormPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }

        // 사용자 정보와 관련된 데이터 추가
        model.addAttribute("user", loginedUser);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 개인정보수정");

        return "infoModifyForm";
    }

    //계정정보수정 처리
    @PostMapping("/info/modify-form")
    public String updateUserInfo(@ModelAttribute("userUpdateRequest") UserUpdateRequest updateRequest,
                                 BindingResult bindingResult,
                                 Authentication auth,
                                 Model model) {
    	
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);
        
        if (loginedUser == null) {
        	return "redirect:/security-login/login";
        }
        
        boolean emailChanged = updateRequest.getNewUEmail() != null && !updateRequest.getNewUEmail().equals(loginedUser.getUEmail());
        
        if (emailChanged) {
        	String newEmail = updateRequest.getNewUEmail();
        	String codeFromUser = updateRequest.getEmailCode();
        	
        	try {
        		// 이메일 인증 확인
        		if (!emailService.verifyEmailCode(newEmail, codeFromUser)) {
                    model.addAttribute("error", "이메일 인증이 완료되지 않았습니다.");
                    model.addAttribute("userUpdateRequest", updateRequest); // 사용자 입력값 유지
                    model.addAttribute("user", loginedUser);
                    return "infoModifyForm"; // 인증 오류 시 폼 페이지로 돌아가기
                }
        		
        	} catch (RuntimeException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("userUpdateRequest", updateRequest); // 사용자 입력값 유지
                model.addAttribute("user", loginedUser);
                return "infoModifyForm"; // 오류 발생 시 폼 페이지로 돌아가기
            }
        } else {
            // 이메일이 변경되지 않았다면 다른 정보만 업데이트
            userService.updateUser(loginedUser.getUserId(), updateRequest);
        }

        //로그인한 유저의 UserId(고유키)를 가져와 업데이트 수행
        Long userId = loginedUser.getUserId();
                
        // 사용자 정보 업데이트
        userService.updateUser(userId, updateRequest);

        return "redirect:/security-login/info/modify";
    }
    
    // 비밀번호 변경 페이지 요청
    @GetMapping("/info/pw-modify-form")
    public String showChangePasswordPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }
        
        model.addAttribute("user", loginedUser);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 비밀번호 변경");
        return "pwModifyForm";
    }
    
    // 비밀번호 변경 처리
    @PostMapping("/info/pw-modify-form")
    public String changePassword(@ModelAttribute("passwordChangeRequest") PasswordChangeRequest passwordChangeRequest,
    							 BindingResult bindingResult,
							     Authentication auth,
							     Model model) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login"; // 로그인 사용자 정보가 없으면 로그인 페이지로 리디렉션
        }

     // 현재 비밀번호 확인
        if (!passwordEncoder.matches(passwordChangeRequest.getCurrentUPassword(), loginedUser.getUPw())) {
        	model.addAttribute("user", loginedUser);
            model.addAttribute("error", "현재 비밀번호가 맞지 않습니다.");
            return "pwModifyForm"; // 현재 비밀번호가 일치하지 않으면 폼 페이지로 돌아가기
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (!passwordChangeRequest.getNewUPassword().equals(passwordChangeRequest.getConfirmUPassword())) {
        	model.addAttribute("user", loginedUser);
            model.addAttribute("error", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "pwModifyForm"; // 비밀번호가 일치하지 않으면 폼 페이지로 돌아가기
        }

        // 비밀번호 변경
        try {
            userService.updateUserPassword(loginedUser.getUserId(), passwordChangeRequest.getCurrentUPassword(), passwordChangeRequest.getNewUPassword());
            model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "pwModifyForm";
        }
        return "redirect:/security-login/info/modify"; // 비밀번호 변경 후 로그인 페이지로 리디렉션
    }
    
    // 현재 비밀번호 확인 페이지 표시
    @GetMapping("/confirm-password")
    public String showConfirmPasswordPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 비밀번호 확인");
        return "confirm-password"; // confirm-password.html 페이지
    }

    // 현재 비밀번호 확인 처리
    @PostMapping("/confirm-password")
    public String confirmPassword(@RequestParam("currentUPassword") String currentUPassword,
                                  Authentication auth,
                                  Model model) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);
        
        if (loginedUser == null) {
            return "redirect:/security-login/login"; // 로그인 사용자 정보가 없으면 로그인 페이지로 리디렉션
        }

        // 비밀번호 확인
        if (passwordEncoder.matches(currentUPassword, loginedUser.getUPw())) {
            // 비밀번호가 일치하면 비밀번호 변경 페이지로 이동
            return "redirect:/security-login/info/pw-modify-form";
        } else {
            // 비밀번호가 일치하지 않으면 다시 확인 페이지로 돌아감
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "confirm-password";
        }
    }
    
    // 나의예약목록 페이지 요청
    @GetMapping("/info/my-reservations")
    public String myReservationPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("user", loginedUser);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 나의예약목록");

        return "myReservations";
    }
    
    // 나의예약목록 페이지 요청
    @GetMapping("/info/my-point")
    public String myPointPage(Model model, Authentication auth) {
        String loginedId = auth.getName();
        User loginedUser = userService.getLoginUserByUId(loginedId);
        addUserAttributes(model, auth);

        if (loginedUser == null) {
            return "redirect:/security-login/login";
        }

        model.addAttribute("user", loginedUser);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 나의포인트관리");

        return "myPoint";
    }
    
    // 관리자 페이지 요청
    @GetMapping("/admin")
    public String adminPage(Model model, Authentication auth) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "쉼, 독 : 관리자 페이지");
        addUserAttributes(model, auth);

        // 관리자 권한 확인 추가 필요
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/security-login";
        }

        return "admin";
    }
    @GetMapping("/find-id")
    public String showFindIdForm(Model model) {
    	model.addAttribute("loginType", "security-login");
        model.addAttribute("emailDto", new EmailDto()); // EmailDto 객체를 모델에 추가
        return "find-id"; // 아이디 찾기 페이지 반환
    }

    @PostMapping("/find-id/find-id-result")
    public String findIdResult(@RequestParam("mail") String mail, 
                               @RequestParam("verifyCode") String verifyCode, 
                               Model model) {
    	model.addAttribute("loginType", "security-login");
        // 인증 코드가 올바른지 확인
        if (emailService.verifyEmailCode(mail, verifyCode)) {
            // 이메일로 유저 찾기 (Optional 사용)
            Optional<User> userOptional = userRepository.findByuEmailIgnoreCase(mail);
            // 유저가 존재하는지 확인
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                model.addAttribute("uId", user.getUId()); // 유저의 아이디를 모델에 추가
                return "find-id-result"; // 결과 페이지 반환
            }
            // 유저가 존재하지 않을 때
            model.addAttribute("error", "등록된 아이디를 찾을 수 없습니다.");
            return "find-id"; // 다시 아이디 찾기 페이지로 이동
        }
        // 인증 코드가 올바르지 않을 때
        model.addAttribute("error", "인증 코드가 올바르지 않습니다.");
        return "find-id"; // 다시 아이디 찾기 페이지로 이동
    }
    @GetMapping("/find-password")
    public String showFindPasswordForm(Model model) {
        model.addAttribute("loginType", "security-login");
        model.addAttribute("emailDto", new EmailDto()); // EmailDto 객체를 모델에 추가
        return "find-password"; // 비밀번호 찾기 페이지 반환
    }
    @PostMapping("/find-password")
    public String findPassword(//@RequestParam("mail") String mail, 
                               //@RequestParam("verifyCode") String verifyCode, 
    							@RequestBody EmailDto emailDto, 
                               Model model) {
        model.addAttribute("loginType", "security-login");

        String mail = emailDto.getMail();
        String verifyCode = emailDto.getVerifyCode();

        // 인증 코드 검증
        if (emailService.verifyEmailCode(mail, verifyCode)) {
            Optional<User> userOptional = userRepository.findByuEmailIgnoreCase(mail);

            // 이메일로 유저가 존재하는지 확인
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 임시 비밀번호 생성
                String tempPassword = emailService.generateTemporaryPassword();

                // 임시 비밀번호 암호화 및 사용자 정보 업데이트
                userService.updateUserPassword(user.getUserId(), tempPassword);

                try {
                    // 임시 비밀번호 이메일 발송
                    emailService.sendTemporaryPassword(mail, tempPassword);
                    model.addAttribute("message", "임시 비밀번호가 이메일로 발송되었습니다.");
                } catch (MessagingException e) {
                    model.addAttribute("error", "이메일 발송에 실패했습니다. 다시 시도해 주세요.");
                    return "find-password";  // 실패 시 다시 비밀번호 찾기 페이지로 이동
                }

                // 성공 시 결과 페이지로 이동
                return "redirect:/security-login/find-password-result"; 
            }

            // 유저가 존재하지 않는 경우
            model.addAttribute("error", "등록된 회원 정보가 없습니다.");
            return "find-password"; 
        }

        // 인증 코드가 틀린 경우
        model.addAttribute("error", "인증 코드가 올바르지 않습니다.");
        return "find-password";
    }


    // 비밀번호 찾기 결과 페이지 반환
    @GetMapping("/find-password-result")
    public String showFindPasswordResultPage(Model model) {
        model.addAttribute("loginType", "security-login");
        return "find-password-result"; // 비밀번호 찾기 결과 페이지로 이동
    }
    @PostMapping("/find-password-result")
    public String showFindPasswordResult(Model model) {
        model.addAttribute("loginType", "security-login");
        // 필요한 데이터 추가
        return "find-password-result"; // 비밀번호 찾기 결과 페이지로 이동
    }

}


