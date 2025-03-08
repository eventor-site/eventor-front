package com.eventorfront.usergrade.client;

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

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.usergrade.dto.GradeDto;

@FeignClient(name = "grade-client", url = "${feignClient.url}")
public interface GradeClient {

	@GetMapping("/back/grades")
	ApiResponse<List<GradeDto>> getGrades();

	@GetMapping("/back/grades/paging")
	ApiResponse<Page<GradeDto>> getGrades(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/grades/{gradeId}")
	ApiResponse<GradeDto> getGrade(@PathVariable Long gradeId);

	@PostMapping("/back/grades")
	ApiResponse<Void> createGrade(@RequestBody GradeDto request);

	@PutMapping("/back/grades/{gradeId}")
	ApiResponse<Void> updateGrade(@PathVariable Long gradeId, @RequestBody GradeDto request);

	@DeleteMapping("/back/grades/{gradeId}")
	ApiResponse<Void> deleteGrade(@PathVariable Long gradeId);
}
