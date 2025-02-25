package com.eventorfront.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")  // 모든 URL 에 대해 적용
			.allowedOrigins("https://www.eventor.store.com")  // 특정 도메인만 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE")  // 허용할 HTTP 메서드
			.allowedHeaders("*")  // 모든 헤더 허용
			.allowCredentials(true);  // 쿠키 포함 허용
	}
}