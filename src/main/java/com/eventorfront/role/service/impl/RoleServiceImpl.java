package com.eventorfront.role.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.role.client.RoleClient;
import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleClient roleClient;

	@Override
	public ApiResponse<Page<RoleDto>> getRoles(Pageable pageable) {
		return roleClient.getRoles(pageable).getBody();
	}

	@Override
	public ApiResponse<RoleDto> getRole(Long roleId) {
		return roleClient.getRole(roleId).getBody();
	}

	@Override
	public ApiResponse<Void> createRole(RoleDto request) {
		return roleClient.createRole(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateRole(Long roleId, RoleDto request) {
		return roleClient.updateRole(roleId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteRole(Long roleId) {
		return roleClient.deleteRole(roleId).getBody();
	}
}
