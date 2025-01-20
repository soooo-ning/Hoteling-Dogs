package com.hoteling.project.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Primary
@Component
@ConfigurationProperties(prefix = "iamport.api")
@Getter
@Setter
public class IamportProperties {

  private String key;
  private String secret;

}
