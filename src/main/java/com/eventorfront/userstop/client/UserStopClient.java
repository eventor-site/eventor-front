package com.eventorfront.userstop.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;

@FeignClient(name = "userStop-client", url = "http://localhost:8090/back/userStops")
public interface UserStopClient {

	@GetMapping
	ResponseEntity<List<GetUserStopResponse>> getUserStops();

	@GetMapping("/{userStopId}")
	ResponseEntity<UserStopDto> getUserStop(@PathVariable Long userStopId);

	@PostMapping
	ResponseEntity<Void> createUserStop(@RequestBody UserStopDto request);

	@PutMapping("/{userStopId}")
	ResponseEntity<Void> updateUserStop(@PathVariable Long userStopId, @RequestBody UserStopDto request);

	@DeleteMapping("/{userStopId}")
	ResponseEntity<Void> deleteUserStop(@PathVariable Long userStopId);
}
