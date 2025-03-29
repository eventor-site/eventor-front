package com.eventorfront.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;
import com.eventorfront.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {
	private final ImageService imageService;

	@PostMapping("/upload")
	public List<GetImageResponse> uploadImage(@RequestParam("file") MultipartFile file,
		@RequestParam Long postId, @RequestParam String categoryName, @RequestParam boolean isThumbnail,
		@RequestParam boolean isPasted) {
		return imageService.upload(file, postId, categoryName, isThumbnail, isPasted).getData();
	}

	@DeleteMapping
	public List<GetImageResponse> deleteImages(@RequestBody DeleteImageRequest request) {
		return imageService.deleteImages(request).getData();
	}

	@DeleteMapping("/temp")
	public ResponseEntity<Void> deleteTempImage() {
		imageService.deleteTempImage();
		return ResponseEntity.ok().build();
	}

}