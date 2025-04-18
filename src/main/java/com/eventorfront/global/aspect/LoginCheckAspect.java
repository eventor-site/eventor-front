package com.eventorfront.global.aspect;

import java.time.Duration;
import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.util.CookieUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAspect {
	private final AuthService authService;

	@AfterReturning(pointcut = "execution(* com.eventorfront.post.controller..*(..)) || execution(* com.eventorfront.global.controller.IndexController.mainPage(..))")
	public void loginCheckAspect() {
		HttpServletRequest request = getHttpServletRequest();
		HttpServletResponse response = getHttpServletResponse();

		// 로그인 쿠키가 없다면
		if (!authService.hasTokensInCookie(request)) {
			// 1. non-member 쿠키 없으면 생성
			Cookie[] cookies = request.getCookies();
			int count = 0;

			if (cookies != null) {
				for (Cookie cookie : cookies) {

					if ("guest-access-count".equals(cookie.getName())) {
						try {
							count = Integer.parseInt(cookie.getValue());
						} catch (NumberFormatException e) {
							count = 0;
						}
					}
				}
			}

			if (count < 0) {
				count = 0;
			}

			// 2. 방문 횟수 쿠키 갱신
			count++;

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
			int secondsUntilMidnight = (int)Duration.between(now, midnight).getSeconds();

			Cookie countCookie = new Cookie("guest-access-count", String.valueOf(count));
			countCookie.setPath("/");
			countCookie.setMaxAge(secondsUntilMidnight);
			response.addCookie(countCookie);
		}
	}

	@AfterReturning(pointcut = "execution(* com.eventorfront.auth.controller.AuthController.login(..))", returning = "redirectUrl")
	public void loginCheckAspect(String redirectUrl) {
		HttpServletResponse response = getHttpServletResponse();

		if (redirectUrl.equals("redirect:/")) {
			CookieUtil.revokeToken(response, "guest-access-count");
		}
	}

	@AfterReturning(pointcut = "execution(* com.eventorfront.auth.controller.AuthController.oauthLogin(..))")
	public void loginCheckAspect(JoinPoint joinPoint) {
		HttpServletResponse response = getHttpServletResponse();
		Object[] args = joinPoint.getArgs();
		if (args.length > 0) {
			Object arg = args[3];

			// error 가 null 이거나 빈 문자열("")일 경우
			if (!(arg instanceof String error) || error.isEmpty()) {
				CookieUtil.revokeToken(response, "guest-access-count");
			}
		}
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
