package com.eventorfront.global.exception.decorder;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eventorfront.global.exception.AlreadyExistsException;
import com.eventorfront.global.exception.BadRequestException;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.global.exception.NotFoundException;
import com.eventorfront.global.exception.PayloadTooLargeException;
import com.eventorfront.global.exception.ServerException;
import com.eventorfront.global.exception.UnauthorizedException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FeignErrorDecoder implements ErrorDecoder {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public Exception decode(String methodKey, Response response) {
		try {
			// Feign 응답 바디를 InputStream 으로 읽어 JSON 변환
			String apiResponse = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);

			// JSON 파싱하여 message 필드 추출
			Map<String, Object> errorMap = objectMapper.readValue(apiResponse, new TypeReference<>() {
			});
			String errorMessage = (String)errorMap.getOrDefault("message", "알 수 없는 오류가 발생했습니다.");

			return switch (response.status()) {
				case 400 -> new BadRequestException(errorMessage);
				case 401 -> new UnauthorizedException(errorMessage);
				case 403 -> new ForbiddenException(errorMessage);
				case 404 -> new NotFoundException(errorMessage);
				case 409 -> new AlreadyExistsException(errorMessage);
				case 413 -> new PayloadTooLargeException(errorMessage);
				case 500 -> new ServerException(errorMessage);
				default -> throw new ServerException(errorMessage);
			};
		} catch (Exception ex) {
			return new FeignException.FeignClientException(
				response.status(), "Feign 에러 디코딩 실패", response.request(), new byte[0], response.headers());
		}
	}
}
