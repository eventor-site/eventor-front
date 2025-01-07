package com.eventorfront.auth.dto.request;

import java.time.LocalDateTime;

public record UpdateLastLoginTimeRequest(
	LocalDateTime lastLoginTime) {
}
