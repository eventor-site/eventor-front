package com.eventorfront.image.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;

public interface ImageService {

	List<GetImageResponse> upload(MultipartFile file, Long postId, boolean isThumbnail,
		boolean isPasted);

	List<GetImageResponse> deleteImages(DeleteImageRequest request);

	void deleteTempImage();
}
