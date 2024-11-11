package com.eventorfront.global.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 클라이언트의 응답에서 새로운 토큰을 추출하고 이를 쿠키에 설정하는 기능을 제공합니다.
 */
@Slf4j
@Aspect
@Component
public class ReissueTokenAspect {

	/**
	 * Feign 클라이언트의 메서드가 반환된 후 실행되는 애프터 리턴 어드바이스입니다.
	 * 응답에서 새로운 액세스 토큰과 리프레시 토큰을 추출하여 쿠키에 설정합니다.
	 */
	@AfterReturning(pointcut = "execution(* com.sikyeojofront..client..*(..))", returning = "responseEntity")
	public void afterReturning(ResponseEntity<?> responseEntity) {
		if (responseEntity != null) {

			String newAccessToken = responseEntity.getHeaders().getFirst("New-Authorization");
			String newRefreshToken = responseEntity.getHeaders().getFirst("New-Refresh-Token");

			if (newAccessToken != null) {
				Cookie accessTokenCookie = new Cookie("Authorization", newAccessToken);
				accessTokenCookie.setHttpOnly(true);
				accessTokenCookie.setPath("/");
				getHttpServletResponse().addCookie(accessTokenCookie);
			}

			if (newRefreshToken != null) {
				Cookie refreshTokenCookie = new Cookie("Refresh-Token", newRefreshToken);
				refreshTokenCookie.setHttpOnly(true);
				refreshTokenCookie.setPath("/");
				getHttpServletResponse().addCookie(refreshTokenCookie);
			}
		}
	}

	/**
	 * 현재 HTTP 응답 객체를 가져옵니다.
	 */
	private HttpServletResponse getHttpServletResponse() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getResponse();
	}
}
