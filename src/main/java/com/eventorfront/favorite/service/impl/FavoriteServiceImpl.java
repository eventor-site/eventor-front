package com.eventorfront.favorite.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.favorite.client.FavoriteClient;
import com.eventorfront.favorite.dto.response.GetFavoriteResponse;
import com.eventorfront.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
	private final FavoriteClient favoriteClient;

	@Override
	public Page<GetFavoriteResponse> getFavoritesByUserId(Pageable pageable) {
		return favoriteClient.getFavoritesByUserId(pageable).getData();
	}

	@Override
	public String createOrDeleteFavorite(Long postId) {
		return favoriteClient.createOrDeleteFavorite(postId).getMessage();
	}

	@Override
	public String deleteFavorite(Long favoriteId) {
		return favoriteClient.deleteFavorite(favoriteId).getMessage();
	}
}
