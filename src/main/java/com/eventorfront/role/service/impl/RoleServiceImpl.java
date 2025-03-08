package com.eventorfront.role.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.role.client.RoleClient;
import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleClient roleClient;

	@Override
	public Page<RoleDto> getRoles(Pageable pageable) {
		return roleClient.getRoles(pageable).getData();
	}

	@Override
	public RoleDto getRole(Long roleId) {
		return roleClient.getRole(roleId).getData();
	}

	@Override
	public String createRole(RoleDto request) {
		return roleClient.createRole(request).getMessage();
	}

	@Override
	public String updateRole(Long roleId, RoleDto request) {
		return roleClient.updateRole(roleId, request).getMessage();
	}

	@Override
	public String deleteRole(Long roleId) {
		return roleClient.deleteRole(roleId).getMessage();
	}
}
