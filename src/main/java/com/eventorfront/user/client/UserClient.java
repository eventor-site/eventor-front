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
import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.user.dto.request.CertifyEmailRequest;
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
	ResponseEntity<ApiResponse<Page<GetUserListResponse>>> getUsers(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/users/search")
	ResponseEntity<ApiResponse<List<GetUserByIdentifier>>> searchUserByIdentifier(@RequestParam String keyword);

	@GetMapping("/back/users/search/userId")
	ResponseEntity<ApiResponse<List<GetUserByUserId>>> searchUserByUserId(@RequestParam Long userId);

	@GetMapping("/back/users/{userId}")
	ResponseEntity<ApiResponse<GetUserResponse>> getUser(@PathVariable Long userId);

	@PutMapping("/back/users/{userId}")
	ResponseEntity<ApiResponse<Void>> updateUserByAdmin(@PathVariable Long userId, UpdateUserRequest request);

	@PutMapping("/back/users/{userId}/attribute")
	ResponseEntity<ApiResponse<Void>> updateUserAttributeByAdmin(@PathVariable Long userId,
		UpdateUserAttributeRequest request);

	@GetMapping("/back/users/me")
	ResponseEntity<ApiResponse<GetUserResponse>> getUserInfo();

	@PutMapping("/back/users/me")
	ResponseEntity<ApiResponse<Void>> updateUser(UpdateUserRequest request);

	@DeleteMapping("/back/users/me")
	ResponseEntity<ApiResponse<Void>> withdrawUser();

	@GetMapping("/back/users/me/checkRoles")
	ResponseEntity<ApiResponse<Boolean>> meCheckRoles(@RequestParam String roleName);

	@GetMapping("/back/users/me/Roles")
	ResponseEntity<ApiResponse<List<String>>> meRoles();

	@PostMapping("/back/users/me/checkNickname")
	ResponseEntity<ApiResponse<Void>> meCheckNickname(@RequestBody CheckNicknameRequest request);

	@PutMapping("/back/users/me/password")
	ResponseEntity<ApiResponse<Void>> modifyPassword(ModifyPasswordRequest request);

	@PostMapping("/back/users/signup")
	ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignUpRequest signupRequest);

	@PostMapping("/back/users/signup/checkIdentifier")
	ResponseEntity<ApiResponse<Void>> checkIdentifier(@RequestBody CheckIdentifierRequest request);

	@PostMapping("/back/users/signup/checkNickname")
	ResponseEntity<ApiResponse<Void>> checkNickname(@RequestBody CheckNicknameRequest request);

	@PostMapping("/back/users/signup/sendEmail")
	ResponseEntity<ApiResponse<Boolean>> sendEmail(@RequestBody SendCodeRequest request);

	@PostMapping("/back/users/signup/certifyEmail")
	ResponseEntity<ApiResponse<Boolean>> certifyEmail(@RequestBody CertifyEmailRequest request);

	@PostMapping("/back/users/me/recover/identifier")
	ResponseEntity<ApiResponse<Void>> recoverIdentifier(@RequestParam String identifier);

	@PostMapping("/back/users/me/recover/password")
	ResponseEntity<ApiResponse<Void>> recoverPassword(@RequestParam String identifier);

	@PostMapping("/back/users/me/recover")
	ResponseEntity<ApiResponse<Void>> recover(@RequestParam String identifier);
}
