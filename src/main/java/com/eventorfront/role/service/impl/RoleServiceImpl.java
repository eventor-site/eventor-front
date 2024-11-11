package com.eventorfront.role.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sikyeojofront.role.client.RoleClient;
import com.sikyeojofront.role.dto.RoleDto;
import com.sikyeojofront.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	private final RoleClient roleClient;

	@Override
	public RoleDto getRole(Long roleId) {
		return roleClient.getRole(roleId).getBody();
	}

	@Override
	public List<RoleDto> getRoles() {
		return roleClient.getRoles().getBody();
	}

	@Override
	public void createRole(RoleDto request) {
		roleClient.createRole(request);
	}

	@Override
	public void updateRole(Long roleId, RoleDto request) {
		roleClient.updateRole(roleId, request);
	}

	@Override
	public void deleteRole(Long roleId) {
		roleClient.deleteRole(roleId);
	}
}
