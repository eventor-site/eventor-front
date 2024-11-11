package com.eventorfront.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sikyeojofront.global.interceptor.FeignClientInterceptor;

import feign.Client;
import feign.okhttp.OkHttpClient;

@Configuration
public class FeignClientConfig {

	@Bean
	public Client okHttpClient() {
		return new OkHttpClient();
	}

	@Bean
	public FeignClientInterceptor feignClientInterceptor() {
		return new FeignClientInterceptor();
	}
}
