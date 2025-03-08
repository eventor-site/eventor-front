package com.eventorfront.userrole.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.userrole.client.UserRoleClient;
import com.eventorfront.userrole.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
	private final UserRoleClient userRoleClient;

	@Override
	public ApiResponse<List<RoleDto>> getUserRoles(Long userId) {
		return userRoleClient.getUserRoles(userId).getBody();
	}

	@Override
	public ApiResponse<List<RoleDto>> getUnassignedUserRoles(Long userId) {
		return userRoleClient.getUnassignedUserRoles(userId).getBody();
	}

	@Override
	public ApiResponse<Void> createUserRole(Long userId, Long roleId) {
		return userRoleClient.createUserRole(userId, roleId).getBody();
	}

	@Override
	public ApiResponse<Void> deleteUserRole(Long userId, Long roleId) {
		return userRoleClient.deleteUserRole(userId, roleId).getBody();
	}

}
