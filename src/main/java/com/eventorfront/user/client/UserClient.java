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
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserResponse;

@FeignClient(name = "user-client", url = "http://localhost:8090/back/users")
public interface UserClient {

	@GetMapping("/search")
	ResponseEntity<List<GetUserByIdentifier>> searchUserByIdentifier(@RequestParam String keyword);

	@GetMapping("/me")
	ResponseEntity<GetUserResponse> getUserInfo();

	@PutMapping("/me")
	ResponseEntity<Void> updateUser(UpdateUserRequest request);

	@DeleteMapping("/me")
	ResponseEntity<Void> withdrawUser();

	@PostMapping("/me/checkNickname")
	ResponseEntity<String> meCheckNickname(@RequestBody CheckNicknameRequest request);

	@PutMapping("/me/password")
	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	@PostMapping("/signup")
	ResponseEntity<Void> signup(@RequestBody SignUpRequest signupRequest);

	@PostMapping("/signup/checkIdentifier")
	ResponseEntity<String> checkIdentifier(@RequestBody CheckIdentifierRequest request);

	@PostMapping("/signup/checkNickname")
	ResponseEntity<String> checkNickname(@RequestBody CheckNicknameRequest request);

	@PostMapping("/signup/sendEmail")
	ResponseEntity<String> sendEmail(@RequestParam("email") String email);

	@GetMapping("/signup/checkEmail")
	ResponseEntity<String> checkEmail(@RequestParam("email") String email,
		@RequestParam("certifyCode") String certifyCode);

	@PostMapping("/recover/identifier")
	ResponseEntity<String> recoverIdentifier(@RequestParam String email);

	@PostMapping("/recover/password")
	ResponseEntity<String> recoverPassword(@RequestParam String identifier);
}
