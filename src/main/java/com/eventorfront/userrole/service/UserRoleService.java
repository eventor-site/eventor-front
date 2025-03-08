package com.eventorfront.userrole.service;

import java.util.List;

import com.eventorfront.role.dto.RoleDto;

public interface UserRoleService {

	List<RoleDto> getUserRoles(Long userId);

	List<RoleDto> getUnassignedUserRoles(Long userId);

	String createUserRole(Long userId, Long roleId);

	String deleteUserRole(Long userId, Long roleId);
}
