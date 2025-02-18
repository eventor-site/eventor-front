package com.eventorfront.bookmark.client;

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

import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;

@FeignClient(name = "bookmark-client", url = "${feignClient.url}")
public interface BookmarkClient {

	@GetMapping("/back/users/me/bookmarks")
	ResponseEntity<List<GetBookmarkResponse>> getBookmarksByUserId();

	@GetMapping("/back/users/me/bookmarks/paging")
	ResponseEntity<Page<GetBookmarkResponse>> getBookmarksByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/back/categories/{categoryName}/bookmarks")
	ResponseEntity<String> createOrDeleteBookmark(@PathVariable String categoryName);

	@DeleteMapping("/back/bookmarks/{bookmarkId}")
	ResponseEntity<String> deleteBookmark(@PathVariable Long bookmarkId);
}
