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
 * 이 클래스는 현재 요청의 쿠키에서 `access-token` 및 `refresh-token` 값을 추출하여
 * Feign 클라이언트의 요청 헤더에 추가합니다.
 */
@Slf4j
@Component
public class FeignClientInterceptor implements RequestInterceptor {

	/**
	 * 요청 템플릿에 현재 요청의 쿠키에서 추출한 `access-token` 및 `refresh-token` 값을
	 * 헤더로 추가합니다.
	 */
	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

		if (attributes != null) {
			HttpServletRequest request = attributes.getRequest();
			Cookie[] cookies = request.getCookies();

			if (request.getAttribute("access-token") != null) {
				template.header("access-token", request.getAttribute("access-token").toString());
				template.header("refresh-token", request.getAttribute("access-token").toString());
			} else if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("access-token".equals(cookie.getName())) {
						template.header("access-token", cookie.getValue());
					} else if ("refresh-token".equals(cookie.getName())) {
						template.header("refresh-token", cookie.getValue());
					}
				}
			}

			// X-Ajax-Request 헤더 추가
			String ajaxHeader = request.getHeader("X-Ajax-Request");
			if (ajaxHeader != null) {
				template.header("X-Ajax-Request", ajaxHeader);
			}

		}
	}
}
