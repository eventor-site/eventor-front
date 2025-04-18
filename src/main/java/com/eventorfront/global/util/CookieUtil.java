package com.eventorfront.global.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

	/**
	 * 인스턴스화 방지를 위해 private 생성자를 추가합니다.
	 */
	private CookieUtil() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
		if (request.getCookies() == null)
			return Optional.empty();

		return Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals(name))
			.findFirst();
	}

	/**
	 * 주어진 키와 값을 사용하여 쿠키를 생성합니다.
	 */
	public static Cookie createCookie(String key, String value, int maxAge) {
		Cookie cookie = new Cookie(key, URLEncoder.encode(value, StandardCharsets.UTF_8));
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(true);
		return cookie;
	}

	/**
	 * 주어진 쿠키 이름을 사용하여 토큰을 무효화합니다.
	 */
	public static void revokeToken(HttpServletResponse response, String cookieName) {
		Cookie revokedTokenCookie = new Cookie(cookieName, "");
		revokedTokenCookie.setHttpOnly(true);
		revokedTokenCookie.setMaxAge(0);
		revokedTokenCookie.setPath("/");
		response.addCookie(revokedTokenCookie);
	}
}
