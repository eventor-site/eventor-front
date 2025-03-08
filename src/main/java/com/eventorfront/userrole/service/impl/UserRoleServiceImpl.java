package com.eventorfront.userrole.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.userrole.client.UserRoleClient;
import com.eventorfront.userrole.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
	private final UserRoleClient userRoleClient;

	@Override
	public List<RoleDto> getUserRoles(Long userId) {
		return userRoleClient.getUserRoles(userId).getData();
	}

	@Override
	public List<RoleDto> getUnassignedUserRoles(Long userId) {
		return userRoleClient.getUnassignedUserRoles(userId).getData();
	}

	@Override
	public String createUserRole(Long userId, Long roleId) {
		return userRoleClient.createUserRole(userId, roleId).getMessage();
	}

	@Override
	public String deleteUserRole(Long userId, Long roleId) {
		return userRoleClient.deleteUserRole(userId, roleId).getMessage();
	}

}
