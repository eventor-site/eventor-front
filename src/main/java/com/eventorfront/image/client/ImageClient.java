package com.eventorfront.image.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;

@FeignClient(name = "image-client", url = "${feignClient.url}")
public interface ImageClient {

	@PostMapping(value = "/back/images/upload", consumes = "multipart/form-data")
	ResponseEntity<ApiResponse<List<GetImageResponse>>> upload(@RequestPart("file") MultipartFile file,
		@RequestParam String folderName, @RequestParam Long postId,
		@RequestParam boolean isThumbnail,
		@RequestParam boolean isPasted);

	@DeleteMapping("/back/images")
	ResponseEntity<ApiResponse<List<GetImageResponse>>> deleteImages(@RequestBody DeleteImageRequest request);

	@DeleteMapping("/back/images/temp")
	ResponseEntity<ApiResponse<Void>> deleteTempImage();

}
