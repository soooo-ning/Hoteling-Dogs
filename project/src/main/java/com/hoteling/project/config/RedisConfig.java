package com.hoteling.project.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {
	
	//yml 에서 설정한 redis 서버의 주소와 포트를 가져와 변수에 저장
	@Value("${spring.data.redis.host}")
	private String host;
	
	@Value("${spring.data.redis.port}")
	private int port;
	
	// RedisConnectionFactory : Redis와 연결을 만들어주는 도구
	// LettuceConnectionFactory : Redis와 연결하기 위해 사용하는 구체적인 도구 host, port 변수를 사용해 Redis 서버 연결
	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}
	
	// RedisTemplate<?, ?> : Redis에서 데이터를 저장하고 꺼낼 때 사용하는 도구
	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}

}
