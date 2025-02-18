package com.eventorfront.userstop.client;

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

import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByUserIdResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;

@FeignClient(name = "userStop-client", url = "${feignClient.url}")
public interface UserStopClient {

	@GetMapping("/back/userStops/paging")
	ResponseEntity<Page<GetUserStopResponse>> getUserStops(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/userStops/users")
	ResponseEntity<List<GetUserStopByUserIdResponse>> getUserStopsByUserId(@RequestParam Long userId);

	@GetMapping("/back/userStops/{userStopId}")
	ResponseEntity<UserStopDto> getUserStop(@PathVariable Long userStopId);

	@PostMapping("/back/userStops")
	ResponseEntity<Void> createUserStop(@RequestBody UserStopDto request);

	@PutMapping("/back/userStops/{userStopId}")
	ResponseEntity<Void> updateUserStop(@PathVariable Long userStopId, @RequestBody UserStopDto request);

	@DeleteMapping("/back/userStops/{userStopId}")
	ResponseEntity<Void> deleteUserStop(@PathVariable Long userStopId);
}
