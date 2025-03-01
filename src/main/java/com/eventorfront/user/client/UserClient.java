package com.eventorfront.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserAttributeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserByUserId;
import com.eventorfront.user.dto.response.GetUserListResponse;
import com.eventorfront.user.dto.response.GetUserResponse;

@FeignClient(name = "user-client", url = "${feignClient.url}")
public interface UserClient {

	@GetMapping("/back/users")
	ResponseEntity<Page<GetUserListResponse>> getUsers(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/users/search")
	ResponseEntity<List<GetUserByIdentifier>> searchUserByIdentifier(@RequestParam String keyword);

	@GetMapping("/back/users/search/userId")
	ResponseEntity<List<GetUserByUserId>> searchUserByUserId(@RequestParam Long userId);

	@GetMapping("/back/users/{userId}")
	ResponseEntity<GetUserResponse> getUser(@PathVariable Long userId);

	@PutMapping("/back/users/{userId}")
	ResponseEntity<Void> updateUserByAdmin(@PathVariable Long userId, UpdateUserRequest request);

	@PutMapping("/back/users/{userId}/attribute")
	ResponseEntity<Void> updateUserAttributeByAdmin(@PathVariable Long userId, UpdateUserAttributeRequest request);

	@GetMapping("/back/users/me")
	ResponseEntity<GetUserResponse> getUserInfo();

	@PutMapping("/back/users/me")
	ResponseEntity<Void> updateUser(UpdateUserRequest request);

	@DeleteMapping("/back/users/me")
	ResponseEntity<Void> withdrawUser();

	@GetMapping("/back/users/me/checkRoles")
	ResponseEntity<Boolean> meCheckRoles(@RequestParam String roleName);

	@GetMapping("/back/users/me/Roles")
	ResponseEntity<List<String>> meRoles();

	@PostMapping("/back/users/me/checkNickname")
	ResponseEntity<String> meCheckNickname(@RequestBody CheckNicknameRequest request);

	@PutMapping("/back/users/me/password")
	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	@PostMapping("/back/users/signup")
	ResponseEntity<Void> signup(@RequestBody SignUpRequest signupRequest);

	@PostMapping("/back/users/signup/checkIdentifier")
	ResponseEntity<String> checkIdentifier(@RequestBody CheckIdentifierRequest request);

	@PostMapping("/back/users/signup/checkNickname")
	ResponseEntity<String> checkNickname(@RequestBody CheckNicknameRequest request);

	@PostMapping("/back/users/signup/sendEmail")
	ResponseEntity<String> sendEmail(@RequestBody SendCodeRequest request);

	@GetMapping("/back/users/signup/checkEmail")
	ResponseEntity<String> checkEmail(@RequestParam("email") String email,
		@RequestParam("certifyCode") String certifyCode);

	@PostMapping("/back/users/recover/identifier")
	ResponseEntity<String> recoverIdentifier(@RequestParam String email);

	@PostMapping("/back/users/recover/password")
	ResponseEntity<String> recoverPassword(@RequestParam String identifier);
}
