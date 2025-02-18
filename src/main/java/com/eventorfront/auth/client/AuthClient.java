package com.eventorfront.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;

@FeignClient(name = "auth-feign-client", url = "${feignClient.url}")
public interface AuthClient {

	@PostMapping("/auth/login")
	ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

	@PostMapping("/auth/logout")
	ResponseEntity<Void> logout();

	@GetMapping("/oauth2/authorization/{registrationId}")
	ResponseEntity<String> oauthAuthorization(@PathVariable String registrationId);
}
