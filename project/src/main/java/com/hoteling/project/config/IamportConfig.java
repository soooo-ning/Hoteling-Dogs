package com.hoteling.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;


@Configuration
@ConfigurationProperties(prefix = "iamport")
public class IamportConfig {

	private final IamportProperties iamportProperties;

	public IamportConfig(IamportProperties iamportProperties) {
		this.iamportProperties = iamportProperties;
	}

	@Bean
	public IamportClient iamportClient() {
		return new IamportClient(iamportProperties.getKey(), iamportProperties.getSecret());
	}

}
