package com.eventorfront.bookmark.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;
import com.eventorfront.global.dto.ApiResponse;

public interface BookmarkService {

	ApiResponse<List<GetBookmarkResponse>> getBookmarksByUserId();

	ApiResponse<Page<GetBookmarkResponse>> getBookmarksByUserId(Pageable pageable);

	ApiResponse<Void> createOrDeleteBookmark(String categoryName);

	ApiResponse<Void> deleteBookmark(Long bookmarkId);
}
