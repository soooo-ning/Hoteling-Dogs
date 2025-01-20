package com.hoteling.project.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import com.hoteling.project.domain.entity.UserRole;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthenticationConfiguration configuration; // AuthenticationConfiguration

	@Bean
public SpringSecurityDialect springSecurityDialect() {
    return new SpringSecurityDialect();
}
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
		.csrf((csrf) -> csrf.ignoringRequestMatchers("/api/**"))  // API 엔드포인트에 대해서만 CSRF 비활성화
		.formLogin(auth -> auth
				.loginPage("/security-login/login")
				.loginProcessingUrl("/security-login/loginProc")
				.failureUrl("/security-login/login?error=true")
				.defaultSuccessUrl("/security-login", true)
				.usernameParameter("uId")
				.passwordParameter("uPw")
				.permitAll())
		.httpBasic((auth)->auth.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/css/**", "/js/**", "/images/**", "/assets/**", "/fonts/**", "/favicon.ico").permitAll()
				.requestMatchers("/security-login/","/security-login", "/security-login/login", "/security-login/join",
								"/security-login/find/find-id-result", "/security-login/find-id", "/security-login/userReviewList",
								"/security-login/find-password", "/security-login/find-password-result").permitAll()
				.requestMatchers("/security-login/send-email", "/security-login/verify").permitAll()
				.requestMatchers("/api/v1/email/send", "/api/v1/email/verify").permitAll()
				.requestMatchers("/hotelList", "/hotelAllList","/hotel/**").permitAll()
				.requestMatchers("/wishlist/**").permitAll() // 임시로 모든 접근 허용
				.requestMatchers("/eventform",  "/hotelSubmit", "/hotelForm").permitAll() // 임시로 모든 접근 허용
				.requestMatchers("/eventlist", "/eventread").permitAll()
				.requestMatchers("/security-login/admin").permitAll() // 임시로 모든 접근 허용
				.requestMatchers("/security-login/my/**", "/security-login/info/**").permitAll() // 임시로 모든 접근 허용
				.requestMatchers("/hotel").permitAll()
				.requestMatchers("/api/auth/**", "/file/**").permitAll()
				.requestMatchers("/error").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/hotel/**").permitAll()
				.requestMatchers("/reservation/**").authenticated()
				.anyRequest().permitAll()) // 임시로 모든 요청에 대해 접근 허용
		.oauth2Login(oauth2 -> oauth2
				.loginPage("/security-login/login")
				.defaultSuccessUrl("/security-login", true)
				.failureUrl("/security-login")
				.permitAll())
		.logout(auth -> auth
				.logoutUrl("/security-login/logout")
				.logoutSuccessUrl("/security-login/login?logout=true"))
		.exceptionHandling(exceptions -> exceptions
				.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
				.sessionManagement(session -> session
    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
    .invalidSessionUrl("/login")

				.sessionFixation().migrateSession()
        .maximumSessions(1)
        .expiredUrl("/login?expired")
				
				
    )
		.rememberMe(rememberMe -> rememberMe  // 여기에 rememberMe 설정 추가
                .key("uniqueAndSecret")
                .tokenValiditySeconds(86400)
            );
		
		

		return http.build();
	}

	class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{ \"code\": \"AF\", \"message\": \"Authorization Failed.\" }");
		}
	}
}