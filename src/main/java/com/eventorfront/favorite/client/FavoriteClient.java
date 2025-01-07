package com.eventorfront.favorite.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.post.dto.response.GetPostSimpleResponse;

@FeignClient(name = "favorite-client", url = "http://localhost:8090/back")
public interface FavoriteClient {

	@GetMapping("/users/me/favorites")
	ResponseEntity<List<GetPostSimpleResponse>> getFavoritesByUserId();

	@PostMapping("/post/{postId}/favorites")
	ResponseEntity<String> createFavorite(@PathVariable Long postId);

	@DeleteMapping("/{favoriteId}")
	ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId);
}
