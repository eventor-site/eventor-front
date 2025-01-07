package com.eventorfront.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	@PutMapping("/me")
	ResponseEntity<Void> updateUser(UpdateUserRequest request);

	@PutMapping("/me/password")
	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	@DeleteMapping("/me")
	ResponseEntity<Void> withdrawUser();

}
