package com.eventorfront.usergrade.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.usergrade.dto.UserGradeDto;

@FeignClient(name = "userGrade-client", url = "http://localhost:8090/back/userGrades")
public interface UserGradeClient {

	@GetMapping
	ResponseEntity<List<UserGradeDto>> getUserGrades();

	@GetMapping("/{userGradeId}")
	ResponseEntity<UserGradeDto> getUserGrade(@PathVariable Long userGradeId);

	@PostMapping
	ResponseEntity<Void> createUserGrade(@RequestBody UserGradeDto request);

	@PutMapping("/{userGradeId}")
	ResponseEntity<Void> updateUserGrade(@PathVariable Long userGradeId, @RequestBody UserGradeDto request);

	@DeleteMapping("/{userGradeId}")
	ResponseEntity<Void> deleteUserGrade(@PathVariable Long userGradeId);
}
