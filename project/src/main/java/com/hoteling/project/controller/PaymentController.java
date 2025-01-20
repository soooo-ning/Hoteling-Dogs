package com.hoteling.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hoteling.project.domain.dto.request.payment.PaymentRequestDto;
import com.hoteling.project.domain.dto.response.payment.PaymentResponseDto;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.service.PaymentService;
import com.hoteling.project.service.UserService;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final UserService userService;

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

    @GetMapping
    public String paymentPage(Model model, Authentication auth) {
        addUserAttributes(model, auth);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "결제 페이지");
        return "payment";
    }

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequestDto requestDto, Authentication auth) {
        try {
            String userId = auth.getName();
            ResponseEntity<? super PaymentResponseDto> response = paymentService.processPayment(requestDto);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(response.getBody());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 처리 중 오류가 발생했습니다.");
            }
        } catch (Exception e) {
            logger.error("Error processing payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 처리 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/complete")
    public String paymentCompletePage(Model model, Authentication auth) {
        addUserAttributes(model, auth);
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName", "결제 완료");
        return "payment-complete";
    }
}