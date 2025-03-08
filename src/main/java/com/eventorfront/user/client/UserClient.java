package com.eventorfront.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.global.dto.ApiResponse;
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
	ApiResponse<Page<GetUserListResponse>> getUsers(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/users/search")
	ApiResponse<List<GetUserByIdentifier>> searchUserByIdentifier(@RequestParam String keyword);

	@GetMapping("/back/users/search/userId")
	ApiResponse<List<GetUserByUserId>> searchUserByUserId(@RequestParam Long userId);

	@GetMapping("/back/users/{userId}")
	ApiResponse<GetUserResponse> getUser(@PathVariable Long userId);

	@PutMapping("/back/users/{userId}")
	ApiResponse<Void> updateUserByAdmin(@PathVariable Long userId, UpdateUserRequest request);

	@PutMapping("/back/users/{userId}/attribute")
	ApiResponse<Void> updateUserAttributeByAdmin(@PathVariable Long userId, UpdateUserAttributeRequest request);

	@GetMapping("/back/users/me")
	ApiResponse<GetUserResponse> getUserInfo();

	@PutMapping("/back/users/me")
	ApiResponse<Void> updateUser(UpdateUserRequest request);

	@DeleteMapping("/back/users/me")
	ApiResponse<Void> withdrawUser();

	@GetMapping("/back/users/me/checkRoles")
	ApiResponse<Boolean> meCheckRoles(@RequestParam String roleName);

	@GetMapping("/back/users/me/Roles")
	ApiResponse<List<String>> meRoles();

	@PostMapping("/back/users/me/checkNickname")
	ApiResponse<Void> meCheckNickname(@RequestBody CheckNicknameRequest request);

	@PutMapping("/back/users/me/password")
	ApiResponse<Void> modifyPassword(ModifyPasswordRequest request);

	@PostMapping("/back/users/signup")
	ApiResponse<Void> signup(@RequestBody SignUpRequest signupRequest);

	@PostMapping("/back/users/signup/checkIdentifier")
	ApiResponse<Void> checkIdentifier(@RequestBody CheckIdentifierRequest request);

	@PostMapping("/back/users/signup/checkNickname")
	ApiResponse<Void> checkNickname(@RequestBody CheckNicknameRequest request);

	@PostMapping("/back/users/signup/sendEmail")
	ApiResponse<Void> sendEmail(@RequestBody SendCodeRequest request);

	@GetMapping("/back/users/signup/checkEmail")
	ApiResponse<Void> checkEmail(@RequestParam("email") String email,
		@RequestParam("certifyCode") String certifyCode);

	@PostMapping("/back/users/recover/identifier")
	ApiResponse<Void> recoverIdentifier(@RequestParam String email);

	@PostMapping("/back/users/recover/password")
	ApiResponse<String> recoverPassword(@RequestParam String identifier);
}
