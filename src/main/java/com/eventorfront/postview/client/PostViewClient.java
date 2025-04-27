package com.eventorfront.postview.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;

@FeignClient(name = "postView-client", url = "${feignClient.url}")
public interface PostViewClient {

	@GetMapping("/back/users/me/postViews/paging")
	ResponseEntity<ApiResponse<Page<GetMainPostResponse>>> getPostViewsByViewerId(
		@RequestParam(required = false) String uuid, @PageableDefault(page = 1, size = 10) Pageable pageable);

	@DeleteMapping("/back/{postViewId}")
	ResponseEntity<ApiResponse<Void>> deletePostView(@PathVariable Long postViewId);
}
