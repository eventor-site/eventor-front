package com.eventorfront.bookmark.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.bookmark.client.BookmarkClient;
import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;
import com.eventorfront.bookmark.service.BookmarkService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final BookmarkClient favoriteClient;

	@Override
	public ApiResponse<List<GetBookmarkResponse>> getBookmarksByUserId() {
		return favoriteClient.getBookmarksByUserId().getBody();
	}

	@Override
	public ApiResponse<Page<GetBookmarkResponse>> getBookmarksByUserId(Pageable pageable) {
		return favoriteClient.getBookmarksByUserId(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createOrDeleteBookmark(String categoryName) {
		return favoriteClient.createOrDeleteBookmark(categoryName).getBody();
	}

	@Override
	public ApiResponse<Void> deleteBookmark(Long favoriteId) {
		return favoriteClient.deleteBookmark(favoriteId).getBody();
	}
}
