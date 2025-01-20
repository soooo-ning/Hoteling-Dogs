package com.hoteling.project.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.hoteling.project.domain.entity.User;

public class CustomOauth2UserDetails implements UserDetails, OAuth2User {
	
	private static final long serialVersionUID = 1L; // serialVersionUID 필드 추가 (직렬화된 객체의 버전을 관리하는 숫자)
	
	private final User user;
	// OAuth2 공급자(ex. google, kakao등)로부터 받아 온 정보들
	private Map<String, Object> attributes;
	
	// 생성자 생성, User 객체와 OAuth2 공급자로부터 받은 사용자 정보를 초기화.
	public CustomOauth2UserDetails(User user, Map<String, Object> attributes) {
		this.user=user;
		this.attributes=attributes;
	}
	
	/* OAuth2User Interface 구현*/
	
	//OAuth2 공급자로부터 받은 사용자 정보를 반환
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	//OAuth2 공급자로부터 사용자 이름을 반환, 여기선 Null 리턴(필요에 따라 구현하면 됨)
	@Override
	public String getName() {
		return null;
	}
	
	// 권한 관련
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 해당 리스트에 사용자의 권한을 담음
		Collection<GrantedAuthority> collection = new ArrayList<>();
		// GrantedAuthority의 인터페이스를 구현한 익명 클래스를 만들어 권한 추가
		collection.add(new GrantedAuthority() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L; // serialVersionUID 필드 추가 (직렬화된 객체의 버전을 관리하는 숫자)

			@Override
			public String getAuthority() {
				// ROLE_"권한"으로 반환
				return user.getURole().name();
			}
		});
		
		// 권한이 담긴 리스트 반환
		return collection;
	}
	
	/* 아래부턴 UserDetails Interface 구현*/
	
	// 회원의 비밀번호 return
	@Override
	public String getPassword() {
		return user.getUPw();
	}
	
	//회원의 이름 반환 (username라는 파라미터는 uId로 받을 거기 때문에 uId로 수정)
	@Override
	public String getUsername() {
		return user.getUId();
	}
	
	@Override
    public boolean isAccountNonExpired() {
        // 계정 만료 여부 반환, true 반환하여 만료 없음
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 여부 반환, true 반환하여 잠김 없음
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 자격 증명 만료 여부 반환, true 반환하여 만료 없음
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 여부 반환, true 반환하여 사용 가능
        return true;
    }

}
