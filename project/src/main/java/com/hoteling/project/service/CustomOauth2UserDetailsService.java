package com.hoteling.project.service;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hoteling.project.domain.entity.UserRole;
import com.hoteling.project.domain.entity.User;
import com.hoteling.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOauth2UserDetailsService extends DefaultOAuth2UserService  {
	
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		log.info("getAttributes : {}", oAuth2User.getAttributes());
		
		// 현재 사용 중인 OAuth2 공급자의 ID를 가져옴
		String provider = userRequest.getClientRegistration().getRegistrationId();
		
		// 공급자 별로 사용자 정보를 담기 위한 객체 생성
		OAuth2UserInfo oAuth2UserInfo = null;
		
		// 구글
		if(provider.equals("google")) {
			log.info("구글 로그인");
			oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
		} else if (provider.equals("kakao")) {
			log.info("카카오 로그인");
			oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
		}
		
		String providerId = oAuth2UserInfo.getProviderId();
		String uEmail = oAuth2UserInfo.getEmail(); // 외부에서 가져온 이메일
		String uId = provider + providerId;
		String uName = oAuth2UserInfo.getName();
		
		User findUser = userRepository.findByuId(uId);
		User user;
		
		if(findUser == null) {
			user = User.builder()
					.uId(uId)
					.uName(uName)
					.uEmail(uEmail)
					.provider(provider)
					.providerId(providerId)
					.uPw("") // 비밀번호를 빈 문자열로 설정
					.uRole(UserRole.USER)
					.build();
			userRepository.save(user);
		} else {
			user = findUser;
		}
		
		return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
		
	}

}
