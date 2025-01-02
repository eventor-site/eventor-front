package com.eventorfront.global.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign 클라이언트의 요청을 가로채고 수정하는 인터셉터입니다.
 * 이 클래스는 현재 요청의 쿠키에서 `Access-Token` 및 `Refresh-Token` 값을 추출하여
 * Feign 클라이언트의 요청 헤더에 추가합니다.
 */
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

	/**
	 * 요청 템플릿에 현재 요청의 쿠키에서 추출한 `Access-Token` 및 `Refresh-Token` 값을
	 * 헤더로 추가합니다.
	 */
	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			Cookie[] cookies = request.getCookies();

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("Access-Token".equals(cookie.getName())) {
						template.header("Access-Token", cookie.getValue());
					} else if ("Refresh-Token".equals(cookie.getName())) {
						template.header("Refresh-Token", cookie.getValue());
					}
				}
			}
		}
	}
}
