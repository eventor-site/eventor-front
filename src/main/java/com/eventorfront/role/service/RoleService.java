package com.eventorfront.role.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.role.dto.RoleDto;

public interface RoleService {

	ApiResponse<Page<RoleDto>> getRoles(Pageable pageable);

	ApiResponse<RoleDto> getRole(Long roleId);

	ApiResponse<Void> createRole(RoleDto request);

	ApiResponse<Void> updateRole(Long roleId, RoleDto request);

	ApiResponse<Void> deleteRole(Long roleId);
}
