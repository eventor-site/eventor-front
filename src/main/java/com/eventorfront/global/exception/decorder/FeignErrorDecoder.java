package com.eventorfront.global.exception.decorder;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import com.eventorfront.global.exception.AlreadyExistsException;
import com.eventorfront.global.exception.BadRequestException;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.global.exception.NotFoundException;
import com.eventorfront.global.exception.ServerException;
import com.eventorfront.global.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		try {
			// Feign 응답 바디를 InputStream 으로 읽어 JSON 변환
			String errorMessage = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);

			//  Feign 응답 바디가 JSON 형식인 경우
			// String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
			// Map<String, String> errorMap = objectMapper.readValue(responseBody, new TypeReference<>() {
			// });
			//
			// String errorMessage = errorMap.get("message");

			return switch (response.status()) {
				case 400 -> new BadRequestException(errorMessage);
				case 401 -> new UnauthorizedException(errorMessage);
				case 403 -> new ForbiddenException(errorMessage);
				case 404 -> new NotFoundException(errorMessage);
				case 409 -> new AlreadyExistsException(errorMessage);
				case 500 -> new ServerException(errorMessage);
				default -> throw new ServerException(errorMessage);
			};
		} catch (Exception ex) {
			return new FeignException.FeignClientException(
				response.status(), "Feign 에러 디코딩 실패", response.request(), new byte[0], response.headers());
		}
	}
}
