package com.hoteling.project.service;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class KakaoUserDetails implements OAuth2UserInfo{
	// OAuth에서 받아온 정보
    private Map<String, Object> attributes;
    
    // OAuth2 로그인 제공자의 이름을 반환
    @Override
    public String getProvider() {
        return "kakao";
    }
    
    // 사용자를 식별하는 고유한 ID를 반환
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map) attributes.get("properties")).get("nickname");
    }
}
