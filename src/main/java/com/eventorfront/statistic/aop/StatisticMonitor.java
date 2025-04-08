package com.eventorfront.statistic.aop;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
public class StatisticMonitor {
	private final StatisticService statisticService;

	@AfterReturning("execution(* com.eventorfront.global.controller.IndexController.mainPage(..))")
	public void collectInfoAfterReturningGetMainPage(JoinPoint joinPoint) {

		Object[] args = joinPoint.getArgs();
		if (args.length > 0 && args[0] instanceof HttpServletRequest request
			&& args[1] instanceof HttpServletResponse response) {

			String userAgent = request.getHeader("User-Agent");
			if (userAgent != null && userAgent.toLowerCase().matches(".*(bot|spider|crawl).*")) {
				return;
			}

			// 1. 쿠키 조회: uuid 가 있는지 확인
			Optional<Cookie> uuidCookieOpt = CookieUtil.getCookie(request, "uuid");

			if (uuidCookieOpt.isEmpty()) {
				reissueUuidCookie(response); // 쿠키가 없을 때
				return;
			}

			String value = uuidCookieOpt.get().getValue(); // 예: uuid or uuid_날짜

			if (!value.contains("_")) {
				// 기존 형식(uuid만 있는 경우) -> 재발급
				reissueUuidCookie(response);
			} else {
				// 이미 uuid_날짜 형식이면 재발급 필요 없음
			}

		}
	}

	private void reissueUuidCookie(HttpServletResponse response) {
		String uuid = UUID.randomUUID().toString();

		// 현재 시각을 yyyyMMddHHmmss 형식으로
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String cookieValue = uuid + "_" + timestamp;

		// 자정까지 남은 시간 계산
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
		long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();

		// 쿠키 생성 및 저장
		Cookie uuidCookie = CookieUtil.createCookie("uuid", cookieValue, (int) secondsUntilMidnight);
		response.addCookie(uuidCookie);

		// 통계 저장 등 부가 처리
		statisticService.saveVisitor(uuid);
	}
}

