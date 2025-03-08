package com.eventorfront.auth.aspect;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAuthorizationAspect {
	private final UserService userService;

	@Before("@annotation(authorizeRole)")
	public void checkUserRole(AuthorizeRole authorizeRole) {

		List<String> roles = userService.meRoles().getData();

		// @AuthorizeRole 에 정의된 역할 목록
		List<String> requiredRoles = Arrays.asList(authorizeRole.value());

		// 사용자가 요구되는 역할을 가지고 있는지 확인
		boolean hasRequiredRole = roles.stream().anyMatch(requiredRoles::contains);

		if (!hasRequiredRole) {
			throw new ForbiddenException();
		}
	}
}
