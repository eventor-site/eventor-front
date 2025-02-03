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

@FeignClient(name = "userStop-client", url = "http://localhost:8090/back/userStops")
public interface UserStopClient {

	@GetMapping("/paging")
	ResponseEntity<Page<GetUserStopResponse>> getUserStops(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/users")
	ResponseEntity<List<GetUserStopByUserIdResponse>> getUserStopsByUserId(@RequestParam Long userId);

	@GetMapping("/{userStopId}")
	ResponseEntity<UserStopDto> getUserStop(@PathVariable Long userStopId);

	@PostMapping
	ResponseEntity<Void> createUserStop(@RequestBody UserStopDto request);

	@PutMapping("/{userStopId}")
	ResponseEntity<Void> updateUserStop(@PathVariable Long userStopId, @RequestBody UserStopDto request);

	@DeleteMapping("/{userStopId}")
	ResponseEntity<Void> deleteUserStop(@PathVariable Long userStopId);
}
