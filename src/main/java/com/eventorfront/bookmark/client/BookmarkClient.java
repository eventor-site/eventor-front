package com.eventorfront.bookmark.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;

@FeignClient(name = "bookmark-client", url = "http://localhost:8090/back")
public interface BookmarkClient {

	@GetMapping("/users/me/bookmarks")
	ResponseEntity<List<GetBookmarkResponse>> getBookmarksByUserId();

	@PostMapping("/categories/{categoryName}/bookmarks")
	ResponseEntity<String> createOrDeleteBookmark(@PathVariable String categoryName);

	@DeleteMapping("/bookmarks/{bookmarkId}")
	ResponseEntity<String> deleteBookmark(@PathVariable Long bookmarkId);
}
