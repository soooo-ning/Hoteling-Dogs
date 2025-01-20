package com.hoteling.project.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hoteling.project.domain.entity.User;
import com.hoteling.project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomSecurityUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 매개변수 username은 SecurityConfig에서 usernameParameter("uId") 값으로 받음
		// 즉, uId를 사용하여 사용자를 조회
		User user = userRepository.findByuId(username);
		if(user != null) {
			// 사용자 정보 반환
			return new CustomSecurityUserDetails(user);
		}
		
		// 사용자가 존재하지 않을 시 Exception return
		throw new UsernameNotFoundException("User not found with ID: " + username);
	}
	
	
}
