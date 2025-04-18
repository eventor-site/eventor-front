package com.eventorfront.statistic.aspect;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eventorfront.global.util.CookieUtil;
import com.eventorfront.statistic.service.StatisticService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Transactional
public class StatisticAspect {
	private final StatisticService statisticService;

	@Before("execution(* com.eventorfront..controller..*(..))")
	public void checkAndReissueUuidCookie() {

		HttpServletRequest request = getHttpServletRequest();

		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null && userAgent.toLowerCase().matches(".*(bot|spider|crawl|bytespider|ahrefsbot).*")) {
			return;
		}

		// 1. 쿠키 조회: uuid 가 있는지 확인
		Optional<Cookie> uuidCookieOpt = CookieUtil.getCookie(request, "uuid");

		if (uuidCookieOpt.isEmpty()) {
			reissueUuidCookie(); // 쿠키가 없을 때
			return;
		}

		String value = uuidCookieOpt.get().getValue(); // 예: uuid or uuid_날짜

		if (!value.contains("_")) {
			// 기존 형식(uuid 만 있는 경우) -> 재발급
			reissueUuidCookie();
		}

	}

	private void reissueUuidCookie() {
		HttpServletResponse response = getHttpServletResponse();
		String uuid = UUID.randomUUID().toString();

		// 현재 시각을 yyyyMMddHHmmss 형식으로
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String cookieValue = uuid + "_" + timestamp;

		// 자정까지 남은 시간 계산
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
		long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();

		// 쿠키 생성 및 저장
		Cookie uuidCookie = CookieUtil.createCookie("uuid", cookieValue, (int)secondsUntilMidnight);
		response.addCookie(uuidCookie);

		// 통계 저장 등 부가 처리
		statisticService.increaseVisitor();
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

