package com.hoteling.project.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hoteling.project.domain.dto.JoinRequest;
import com.hoteling.project.domain.dto.LoginRequest;
import com.hoteling.project.domain.dto.UserUpdateRequest;
import com.hoteling.project.domain.entity.DogInfo;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.domain.entity.UserModify;
import com.hoteling.project.repository.DogInfoRepository;
import com.hoteling.project.repository.UserModifyRepository;
import com.hoteling.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

public class UserService {
	
	private final Logger logger = LoggerFactory.getLogger(UserService.class);
	 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DogInfoRepository dogInfoRepository;
    private final UserModifyRepository userModifyRepository;
    private final EmailService emailService;

    // 아이디 중복 체크
    public boolean checkUIdDuplicate(String uId) {
        return userRepository.existsByuId(uId);
    }

    // 회원가입
    public void securityJoin(JoinRequest joinRequest) {
        try {
            if (userRepository.existsByuId(joinRequest.getUId())) {
                throw new RuntimeException("아이디가 이미 존재합니다.");
            }

            String encodedPassword = passwordEncoder.encode(joinRequest.getUPw());
            joinRequest.setUPw(encodedPassword);

            User user = joinRequest.toUserEntity();
            userRepository.save(user);
            System.out.println("User saved: " + user);
            
            List<DogInfo> dogInfos = joinRequest.toDogInfoEntities(user);
            dogInfoRepository.saveAll(dogInfos);
            System.out.println("DogInfos saved: " + dogInfos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 로그인
    public User login(LoginRequest loginRequest) {
        User findUser = userRepository.findByuId(loginRequest.getUId());

        if (findUser == null || !passwordEncoder.matches(loginRequest.getUPw(), findUser.getUPw())) {
            return null;
        }

        return findUser;
    }

 // User 객체를 통해 강아지 정보를 가져오는 메서드
    public List<DogInfo> getDogInfosByUser(User user) {
        return dogInfoRepository.findByOwner(user);
    }
    
    // uId로 회원찾기
    public User getLoginUserByUId(String uId) {
        return userRepository.findByuId(uId);
    }
    
    // 고유 ID(users 테이블의 PK)로 회원 찾기
    public User getLoginUserById(Long userId) {
        if (userId == null) return null;

        Optional<User> findUser = userRepository.findById(userId);
        return findUser.orElse(null);
    }
    
    // 유저 아이디로 고유 ID 찾기
    public Long getUserIdByUId(String uId) {
        return userRepository.findUserIdByUId(uId);
    }
    
    //사용자 정보 수정 (비밀번호 제외)
    public User updateUser (Long userId, UserUpdateRequest updateRequest) {
    	User user = userRepository.findById(userId)
    			.orElseThrow(() -> new RuntimeException("User not found"));
    	
    	// 이메일 업데이트
    	boolean emailChanged = updateRequest.getNewUEmail() != null && !updateRequest.getNewUEmail().equals(user.getUEmail());
    	
    	if(emailChanged) {
    		String oldUEmail = user.getUEmail();
    		String newUEmail = updateRequest.getNewUEmail();
    		String codeFromUser = updateRequest.getEmailCode();
    		
    		if (!emailService.verifyEmailCode(newUEmail, codeFromUser)) {
    			throw new RuntimeException("이메일 인증을 완료해 주세요");
    		}
    		
    		if (!oldUEmail.equals(newUEmail)) {
    			UserModify modify = UserModify.builder()
    					.modifyUser(user)
    					.uColName("u_email")
    					.uOldValue(oldUEmail)
    					.uNewValue(newUEmail)
    					.build();
    			
    			userModifyRepository.save(modify);
    			user.setUEmail(newUEmail);
    		}
    	}
    	
    	// 전화번호 업데이트
    	if(updateRequest.getNewUPhoneNum() != null) {
    		String oldUPhoneNum = user.getUPhoneNum();
    		String newUPhoneNum = updateRequest.getNewUPhoneNum();
    		
    		if (!oldUPhoneNum.equals(newUPhoneNum)) {
    			UserModify modify = UserModify.builder()
    					.modifyUser(user)
    					.uColName("u_phone_num")
    					.uOldValue(oldUPhoneNum)
    					.uNewValue(newUPhoneNum)
    					.build();
    			
    			userModifyRepository.save(modify);
    			user.setUPhoneNum(newUPhoneNum);
    		}
    	}
    	
    	// 사용자 정보 업데이트
    	userRepository.save(user);
    	return user;
    } 
    
 // 사용자 비밀번호 수정
    public User updateUserPassword(Long userId, String currentUPassword, String newUPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(currentUPassword, user.getUPw())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // 새 비밀번호 암호화
        String encodedNewPassword = passwordEncoder.encode(newUPassword);
        
        // 비밀번호 변경기록 저장
        UserModify modify = UserModify.builder()
                .modifyUser(user)
                .uColName("u_pw")
                .uOldValue(user.getUPw())
                .uNewValue(encodedNewPassword)
                .build();

        userModifyRepository.save(modify);
        user.setUPw(encodedNewPassword);

        // 사용자 정보 업데이트
        userRepository.save(user);
        return user;
    }
    public String findIdByEmail(String uEmail) {
        Optional<User> userOptional = userRepository.findByuEmailIgnoreCase(uEmail);

        if (userOptional.isPresent()) {
            User user = userOptional.get(); // Optional에서 User 추출
            return user.getUId(); // userId 반환
        } else {
            // 유저를 찾지 못한 경우 예외 대신 에러 메시지 반환
            return "해당 이메일로 등록된 사용자가 없습니다.";
        }
    }
    // 이메일로 사용자 존재 여부 확인하는 메서드
    public int findIdCheck(String uEmail) {
        // 이메일로 사용자를 조회하고, 있으면 1을 반환, 없으면 0을 반환
        return userRepository.existsByuEmail(uEmail) ? 1 : 0;
    }
    // 비밀번호 찾기 시 임시비밀번호 
    public String generateTempPassword() {
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호 생성
    }

    public void updateUserPassword(Long userId, String tempPassword) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUPw(passwordEncoder.encode(tempPassword)); // 비밀번호 암호화 후 저장
            userRepository.save(user);
        }
    }


} 

