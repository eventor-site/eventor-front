package com.eventorfront.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;
import com.eventorfront.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/images")
public class ImageController {
	private final ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<List<GetImageResponse>> uploadImage(@RequestParam("file") MultipartFile file,
		@RequestParam Long postId, @RequestParam boolean isThumbnail, @RequestParam boolean isPasted) {
		return imageService.upload(file, postId, isThumbnail, isPasted); // 이미지를 저장하고 URL 반환하는 로직
	}

	@DeleteMapping
	public ResponseEntity<List<GetImageResponse>> deleteImages(@RequestBody DeleteImageRequest request) {
		return imageService.deleteImages(request);
	}

	@DeleteMapping("/temp")
	public ResponseEntity<Void> deleteTempImage() {
		return imageService.deleteTempImage();
	}

}