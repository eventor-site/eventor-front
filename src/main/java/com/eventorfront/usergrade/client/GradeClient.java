package com.eventorfront.usergrade.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.usergrade.dto.GradeDto;

@FeignClient(name = "grade-client", url = "http://localhost:8090/back/grades")
public interface GradeClient {

	@GetMapping
	ResponseEntity<List<GradeDto>> getGrades();

	@GetMapping("/paging")
	ResponseEntity<Page<GradeDto>> getGrades(Pageable pageable);

	@GetMapping("/{gradeId}")
	ResponseEntity<GradeDto> getGrade(@PathVariable Long gradeId);

	@PostMapping
	ResponseEntity<Void> createGrade(@RequestBody GradeDto request);

	@PutMapping("/{gradeId}")
	ResponseEntity<Void> updateGrade(@PathVariable Long gradeId, @RequestBody GradeDto request);

	@DeleteMapping("/{gradeId}")
	ResponseEntity<Void> deleteGrade(@PathVariable Long gradeId);
}
