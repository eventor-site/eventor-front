package com.eventorfront.bannickname.client;

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
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.bannickname.dto.BanNicknameDto;
import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "banNickname-client", url = "${feignClient.url}")
public interface BanNicknameClient {

	@GetMapping("/back/banNicknames")
	ResponseEntity<ApiResponse<List<BanNicknameDto>>> getBanNicknames();

	@GetMapping("/back/banNicknames/paging")
	ResponseEntity<ApiResponse<Page<BanNicknameDto>>> getBanNicknames(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/back/banNicknames")
	ResponseEntity<ApiResponse<Void>> createBanNickname(
		@RequestBody BanNicknameDto request);

	@DeleteMapping("/back/banNicknames/{banNicknameId}")
	ResponseEntity<ApiResponse<Void>> deleteBanNickname(@PathVariable Long banNicknameId);
}
