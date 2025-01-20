package com.hoteling.project.service;

// OAuth2 인증 프로세스에서 회원의 정보를 통합적으로 다루기 위한 인터페이스

public interface OAuth2UserInfo {
    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
}