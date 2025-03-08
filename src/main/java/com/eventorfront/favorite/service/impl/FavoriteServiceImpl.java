package com.eventorfront.favorite.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.favorite.client.FavoriteClient;
import com.eventorfront.favorite.dto.response.GetFavoriteResponse;
import com.eventorfront.favorite.service.FavoriteService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
	private final FavoriteClient favoriteClient;

	@Override
	public ApiResponse<Page<GetFavoriteResponse>> getFavoritesByUserId(Pageable pageable) {
		return favoriteClient.getFavoritesByUserId(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createOrDeleteFavorite(Long postId) {
		return favoriteClient.createOrDeleteFavorite(postId).getBody();
	}

	@Override
	public ApiResponse<Void> deleteFavorite(Long favoriteId) {
		return favoriteClient.deleteFavorite(favoriteId).getBody();
	}
}
