package com.eventorfront.bookmark.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.bookmark.client.BookmarkClient;
import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;
import com.eventorfront.bookmark.service.BookmarkService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
	private final BookmarkClient favoriteClient;

	@Override
	public List<GetBookmarkResponse> getBookmarksByUserId() {
		return favoriteClient.getBookmarksByUserId().getData();
	}

	@Override
	public Page<GetBookmarkResponse> getBookmarksByUserId(Pageable pageable) {
		return favoriteClient.getBookmarksByUserId(pageable).getData();
	}

	@Override
	public String createOrDeleteBookmark(String categoryName) {
		return favoriteClient.createOrDeleteBookmark(categoryName).getMessage();
	}

	@Override
	public String deleteBookmark(Long favoriteId) {
		return favoriteClient.deleteBookmark(favoriteId).getMessage();
	}
}
