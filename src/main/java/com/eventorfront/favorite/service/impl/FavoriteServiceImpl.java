package com.eventorfront.favorite.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.favorite.client.FavoriteClient;
import com.eventorfront.favorite.service.FavoriteService;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
	private final FavoriteClient favoriteClient;

	@Override
	public List<GetPostSimpleResponse> getFavoritesByUserId() {
		return favoriteClient.getFavoritesByUserId().getBody();
	}

	@Override
	public ResponseEntity<String> createFavorite(Long postId) {
		return favoriteClient.createFavorite(postId);
	}

	@Override
	public ResponseEntity<String> deleteFavorite(Long favoriteId) {
		return favoriteClient.deleteFavorite(favoriteId);
	}
}
