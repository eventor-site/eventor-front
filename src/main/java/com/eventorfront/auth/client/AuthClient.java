package com.eventorfront.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;
import com.eventorfront.auth.dto.response.OauthRedirectUrlResponse;
import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "auth-feign-client", url = "${feignClient.url}")
public interface AuthClient {

	@PostMapping("/auth/login")
	ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest);

	@PostMapping("/auth/logout")
	ApiResponse<Void> logout();

	@GetMapping("/oauth2/authorization/{registrationId}")
	ApiResponse<OauthRedirectUrlResponse> oauthAuthorization(@PathVariable String registrationId);
}
