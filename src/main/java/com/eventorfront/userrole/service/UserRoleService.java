package com.eventorfront.userrole.service;

import java.util.List;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.role.dto.RoleDto;

public interface UserRoleService {

	ApiResponse<List<RoleDto>> getUserRoles(Long userId);

	ApiResponse<List<RoleDto>> getUnassignedUserRoles(Long userId);

	ApiResponse<Void> createUserRole(Long userId, Long roleId);

	ApiResponse<Void> deleteUserRole(Long userId, Long roleId);
}
