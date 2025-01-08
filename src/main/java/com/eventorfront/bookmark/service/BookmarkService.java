package com.eventorfront.bookmark.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;

public interface BookmarkService {

	List<GetBookmarkResponse> getBookmarksByUserId();

	ResponseEntity<String> createOrDeleteBookmark(String categoryName);

	ResponseEntity<String> deleteBookmark(Long bookmarkId);
}
