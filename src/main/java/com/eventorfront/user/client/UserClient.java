package com.eventorfront.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.user.dto.response.GetUserByAddShopResponse;

@FeignClient(name = "user-client", url = "http://localhost:8090/back/users")
public interface UserClient {

	@GetMapping("/search")
	ResponseEntity<List<GetUserByAddShopResponse>> searchUserById(@RequestParam String keyword);

}
