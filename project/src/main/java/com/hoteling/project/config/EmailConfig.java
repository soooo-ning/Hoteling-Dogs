package com.hoteling.project.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {
	
	@Value("${spring.mail.host}")
	private String host;
	
	@Value("${spring.mail.port}")
	private int port;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;
	
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private boolean auth;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private boolean starttlsEnable;
	
	@Value("${spring.mail.properties.mail.smtp.starttls.required}")
	private boolean starttlsRequired;
	
	@Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
	private int connectionTimeout;
	
	@Value("${spring.mail.properties.mail.smtp.timeout}")
	private int timeout;
	
	@Value("${spring.mail.properties.mail.smtp.writetimeout}")
	private int writeTimeout;
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);
		mailSender.setDefaultEncoding("utf-8");
		// 이메일 보내기 위해 필요한 추가 설정 적용
		mailSender.setJavaMailProperties(getMailProperties());
		
		return mailSender;
	}
	
	private Properties getMailProperties() {
		Properties properties = new Properties();
		//SMTP 서버에서 인증을 사용할지 여부 설정
		// 이메일이 유효한 사용자인지를 확인하기 위한 인증
		properties.put("mail.smtp.auth", auth);
		// StarTTLS를 사용해서 SMTP 서버와의 연결을 암호화할지 설정
		// STARTTLS : 이메일 클라이언트(예: 내가 개발하는 이메일 프로그램)와 이메일 서버 간의 통신을 암호화하는 방법
		// 이메일서버와 클라이언트 간의 연결을 암호화하여 중간에 누군가가 데이터(이멜 내용) 엿보는 걸 방지
		properties.put("mail.smtp.starttls.enable", starttlsEnable);
		// StarTTLS가 필수인지 설정
		properties.put("mail.smtp.starttls.required", starttlsRequired);
		// SMTP 서버와의 연결을 시도할 때 밀리초 시간 설정
		// 연결시도가 일정 시간 내에 완료되지 않으면 연결을 포기
		properties.put("mail.smtp.connectiontimeout", connectionTimeout);
		// 서버로부터 응답을 기다릴 때의 밀리초 시간 설정
		// 서버가 응답하지 않는 경우 특정 시간 동안 응답을 기다린 후 타임아웃 발생
		properties.put("mail.smtp.timeout", timeout);
		// 이메일을 서버에 작성할 때 타임아웃 시간 설정
		// 이메일 전송이 너무 오래 걸리지 않도록 보장
		properties.put("mail.smtp.writeimeout", writeTimeout);
		
		return properties;
	}
	
	
}
