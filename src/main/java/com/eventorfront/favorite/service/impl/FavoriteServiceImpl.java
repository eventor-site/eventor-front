package com.eventorfront.favorite.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
		return favoriteClient.getFavoritesByUserId(pageable).getBody();
	}

	@Override
	public ResponseEntity<String> createOrDeleteFavorite(Long postId) {
		return favoriteClient.createOrDeleteFavorite(postId);
	}

	@Override
	public ResponseEntity<String> deleteFavorite(Long favoriteId) {
		return favoriteClient.deleteFavorite(favoriteId);
	}
}
