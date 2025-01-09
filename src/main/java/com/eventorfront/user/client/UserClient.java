package com.eventorfront.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
import com.eventorfront.user.dto.response.GetUserResponse;

@FeignClient(name = "user-client", url = "http://localhost:8090/back/users")
public interface UserClient {

	@GetMapping("/search")
	ResponseEntity<List<GetUserByAddShopResponse>> searchUserById(@RequestParam String keyword);

	@GetMapping("/me")
	ResponseEntity<GetUserResponse> getUserInfo();

	@PostMapping("/signUp/checkIdentifier")
	ResponseEntity<String> checkIdentifier(CheckIdentifierRequest request);

	@PutMapping("/me")
	ResponseEntity<Void> updateUser(UpdateUserRequest request);

	@PutMapping("/me/password")
	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	@DeleteMapping("/me")
	ResponseEntity<Void> withdrawUser();

	@PostMapping("/signUp")
	ResponseEntity<Void> signup(@RequestBody SignUpRequest signUpRequest);

	@PostMapping("/signUp/sendEmail")
	ResponseEntity<String> sendEmail(@RequestParam("email") String email);

	@GetMapping("/signUp/checkEmail")
	ResponseEntity<String> checkEmail(@RequestParam("email") String email,
		@RequestParam("certifyCode") String certifyCode);

}
