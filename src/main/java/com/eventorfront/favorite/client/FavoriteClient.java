package com.eventorfront.favorite.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;

@FeignClient(name = "favorite-client", url = "http://localhost:8090/back")
public interface FavoriteClient {

	@GetMapping("/users/me/favorites")
	ResponseEntity<List<GetFavoriteResponse>> getFavoritesByUserId();

	@PostMapping("/post/{postId}/favorites")
	ResponseEntity<String> createOrDeleteFavorite(@PathVariable Long postId);

	@DeleteMapping("/{favoriteId}")
	ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId);
}
