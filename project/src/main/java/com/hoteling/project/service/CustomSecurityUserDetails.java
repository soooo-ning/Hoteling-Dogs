package com.hoteling.project.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hoteling.project.domain.entity.User;

/* Spring Security는 요청을 가로채 자동으로 인증을 처리하기 때문에,
Spring Security 에서 기본으로 제공하는 사용자 정보를 커스터마이즈(Override)하여 인증 및 권한 부여를 관리*/

public class CustomSecurityUserDetails implements UserDetails, Serializable {
	
	private static final long serialVersionUID = 1L; // serialVersionUID 필드 추가 (직렬화된 객체의 버전을 관리하는 숫자)
	
	private final User user;
	
	public CustomSecurityUserDetails(User user) {
		this.user = user;
	}
	
	
	// 회원의 권한 반환 (ROLE_USER 등)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    List<GrantedAuthority> authorities = new ArrayList<>();
	    
	    // 사용자의 역할을 "ROLE_" 접두사와 함께 추가
	    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getURole().name()));
	    
	    return authorities;
	}
	
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
