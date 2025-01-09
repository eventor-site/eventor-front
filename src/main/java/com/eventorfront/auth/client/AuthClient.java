package com.eventorfront.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;

@FeignClient(name = "auth-feign-client", url = "http://localhost:8090")
public interface AuthClient {

	@PostMapping("/auth/login")
	ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest);

	@PostMapping("/auth/logout")
	ResponseEntity<Void> logout();
}
