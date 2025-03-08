package com.eventorfront.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;

public interface ImageService {

	ApiResponse<List<GetImageResponse>> upload(MultipartFile file, Long postId, boolean isThumbnail,
		boolean isPasted);

	ApiResponse<List<GetImageResponse>> deleteImages(DeleteImageRequest request);

	void deleteTempImage();
}
