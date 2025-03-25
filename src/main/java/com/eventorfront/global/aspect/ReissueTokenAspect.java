package com.eventorfront.global.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eventorfront.global.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
	@AfterReturning(pointcut = "execution(* com.eventorfront..client..*(..))", returning = "responseEntity")
	public void afterReturning(ResponseEntity<?> responseEntity) {
		if (responseEntity != null) {

			String newAccessToken = responseEntity.getHeaders().getFirst("new-access-token");
			String newRefreshToken = responseEntity.getHeaders().getFirst("new-refresh-token");

			if (newAccessToken != null) {
				Cookie accessTokenCookie = CookieUtil.createCookie("access-token", newAccessToken, 60 * 10);
				getHttpServletResponse().addCookie(accessTokenCookie);

				// 현재 요청에 새로운 쿠키 정보 반영
				updateRequestCookies("access-token", newAccessToken);
			}

			if (newRefreshToken != null) {
				Cookie refreshTokenCookie = CookieUtil.createCookie("refresh-token", newRefreshToken, 60 * 60 * 24);
				getHttpServletResponse().addCookie(refreshTokenCookie);

				// 현재 요청에 새로운 쿠키 정보 반영
				updateRequestCookies("refresh-token", newRefreshToken);
			}
		}
	}

	/**
	 * 현재 요청에 새로운 토큰 정보를 반영합니다.
	 */
	private void updateRequestCookies(String cookieName, String cookieValue) {
		HttpServletRequest request = getHttpServletRequest();

		// 요청에 업데이트된 토큰 정보 반영
		request.setAttribute(cookieName, cookieValue);
	}

	private HttpServletRequest getHttpServletRequest() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return attr.getRequest();
	}

	/**
	 * 현재 HTTP 응답 객체를 가져옵니다.
	 */
	private HttpServletResponse getHttpServletResponse() {
		ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		return attr.getResponse();
	}
}
