package com.eventorfront.favorite.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.favorite.service.FavoriteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {
	private final FavoriteService favoriteService;

	@GetMapping("/users/me/favorites")
	public String getFavoritesByUserId(Model model) {
		model.addAttribute("posts", favoriteService.getFavoritesByUserId());
		return "favorite/list";
	}

	@PostMapping("/posts/{postId}/favorites")
	public ResponseEntity<String> createFavorite(@PathVariable Long postId) {
		return favoriteService.createFavorite(postId);
	}

	@DeleteMapping("/{favoriteId}")
	public ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId) {
		return favoriteService.deleteFavorite(favoriteId);
	}
}
