package com.eventorfront.image.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "image-client", url = "${feignClient.url}")
public interface ImageClient {

	@PostMapping(value = "/back/images/upload", consumes = "multipart/form-data")
	ResponseEntity<Void> upload(@RequestPart("files") List<MultipartFile> files, @RequestParam String folderName,
		@RequestParam Long postId);

	@DeleteMapping("/back/images")
	ResponseEntity<Void> deleteImage(@RequestParam Long postId, @RequestParam List<Long> deleteImageIds);

}
