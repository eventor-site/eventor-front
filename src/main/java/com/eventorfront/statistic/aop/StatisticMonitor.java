package com.eventorfront.statistic.aop;

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

			// 1. 쿠키 조회: uuid 가 있는지 확인
			Optional<Cookie> uuidCookieOpt = CookieUtil.getCookie(request, "uuid");

			// 2. uuid 쿠키가 없으면 새로 생성하고 방문 통계 처리
			if (uuidCookieOpt.isEmpty()) {
				String uuid = UUID.randomUUID().toString();

				// (1) 쿠키 생성 (1년 유효)
				Cookie uuidCookie = CookieUtil.createCookie("uuid", uuid, 60 * 60 * 24 * 365); // 1년
				response.addCookie(uuidCookie);

				// (2) 통계 저장 로직 호출
				statisticService.saveVisitor(uuid); // 사용자 방문 저장
			}
		}
	}
}

