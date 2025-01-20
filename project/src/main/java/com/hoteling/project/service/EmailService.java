package com.hoteling.project.service;

import java.util.Random;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	private final RedisUtil redisUtil;
	private static final String senderEmail = "bxxh_@naver.com";
	
	private String createCode() {
		int leftLimit = 48;
		int rightLimit = 122;
		int targetStringLength = 6;
		Random random = new Random();
		
		return random.ints(leftLimit, rightLimit + 1)
				.filter(i -> (i<= 57 || i >=65) && (i<=90 | i>=97))
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
	}
	
	// 이메일 내용 초기화 (이메일 내용(html) 설정)
	private String setContext(String code) {
		// 이메일 템플릿에 데이터 담는 도구
		Context context = new Context();
		// 이메일 템플릿 처리 도구
		TemplateEngine templateEngine = new TemplateEngine();
		// 이메일 템플릿의 위치와 형식 설정
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		context.setVariable("code", code);
		
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		// 이메일 캐싱 비활성화하여 템플릿 수정 시 바로 반영될 수 있게 함
		templateResolver.setCacheable(false);
		
		templateEngine.setTemplateResolver(templateResolver);
		
		// "mail"이라는 템플릿 파일을 사용하여 이메일 내용 생성
		return templateEngine.process("mail", context);
	}
	
	// 이메일 폼 생성
	private MimeMessage createEmailForm(String email) throws MessagingException {
		// 인증코드 생성
		String authCode = createCode();
		
		MimeMessage message = javaMailSender.createMimeMessage();
		// 수신자의 이메일 주소 설정
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject("안녕하세요. 이메일 인증번호입니다.");
		//발신자 이메일 주소 설정
		message.setFrom(senderEmail);
		message.setText(setContext(authCode), "utf-8", "html");
		
		// Redis에 해당 인증코드를 저장하고, 인증 시간 설정 (30분)
		redisUtil.setDataExpire(email, authCode, 60*30L);
		
		return message;
	}
	
	// 인증코드 이메일 발송
	public void sendEmail(String toEmail) throws MessagingException {
		// redis에 이미 저장된 인증코드가 있는지 확인
		if(redisUtil.existData(toEmail)) {
			// 만약 있으면 삭제
			redisUtil.deleteData(toEmail);
		}
		
		//새로운 이메일 폼 생성
		MimeMessage emailForm = createEmailForm(toEmail);
		
		//이메일 발송
		javaMailSender.send(emailForm);
	}
	
	// 코드 검증
	public Boolean verifyEmailCode(String email, String code) {
		// Redis에서 이메일에 해당하는 인증 코드를 가져옴
		String codeFoundByEmail = redisUtil.getData(email);
		
		// 찾은 코드 로그 기록
		log.info("code found by email : " + codeFoundByEmail);
		
		// 입력한 코드와 redis에 저장된 코드가 같은지 비교하여 다르면 false, 같으면 true 반환
		if(codeFoundByEmail == null) {
			return false;
		}
		
		return codeFoundByEmail.equals(code);
	}
	public String generateTemporaryPassword() {
	    int length = 10;
	    String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    Random random = new Random();
	    StringBuilder tempPassword = new StringBuilder();
	    
	    for (int i = 0; i < length; i++) {
	        int randomIndex = random.nextInt(charSet.length());
	        tempPassword.append(charSet.charAt(randomIndex));
	    }
	    
	    return tempPassword.toString();
	}

	public void sendTemporaryPassword(String mail, String tempPassword) throws MessagingException {
	    String subject = "임시 비밀번호 발급 안내";
	    // 이메일 내용 생성: HTML 태그를 사용하여 포맷팅
	 // 이메일 내용 생성: HTML 태그와 스타일을 사용하여 포맷팅
	    String text = "<div style='font-size: 16px;'>"  // 글씨체 크기 설정
	                + "<p>임시 비밀번호는 <strong style='font-weight: bold; font-size: 20px;'>" + tempPassword + "</strong> 입니다.</p>"  // 볼드체 및 글씨 크기 조정
	                + "<p>로그인 후 반드시 비밀번호를 변경해주세요.</p>"
	                + "</div>";

	    sendEmail(mail, subject, text); // 기존 이메일 발송 메소드 사용
	}
	// 새로운 sendEmail 메서드: 제목과 내용을 포함한 이메일 발송
		public void sendEmail(String toEmail, String subject, String text) throws MessagingException {
			MimeMessage message = javaMailSender.createMimeMessage();
			message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
			message.setSubject(subject);
			message.setFrom(senderEmail);
			message.setText(text, "utf-8", "html");
			javaMailSender.send(message);
		}

}

