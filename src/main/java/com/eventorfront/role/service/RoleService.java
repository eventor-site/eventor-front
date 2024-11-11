package com.eventorfront.role.service;

import java.util.List;

import com.eventorfront.role.dto.RoleDto;

public interface RoleService {

	List<RoleDto> getRoles();

	RoleDto getRole(Long roleId);

	void createRole(RoleDto request);

	void updateRole(Long roleId, RoleDto request);

	void deleteRole(Long roleId);
}
