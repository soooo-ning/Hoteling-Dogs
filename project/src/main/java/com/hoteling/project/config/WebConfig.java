package com.hoteling.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	registry.addViewController("/").setViewName("home");
        registry.addViewController("/reviewForm").setViewName("review_form");
        registry.addViewController("/reviewList").setViewName("review_list");
        registry.addViewController("/reviewListAdmin").setViewName("review_list_admin");
        registry.addViewController("/userReviewList").setViewName("user_review_list");
        
        // del
        registry.addViewController("/reviewFormAdmin").setViewName("review_form");
        registry.addViewController("/reviewFormUser").setViewName("review_form");
        // del
    }
    
}