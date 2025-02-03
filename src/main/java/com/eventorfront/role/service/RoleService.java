package com.eventorfront.role.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.role.dto.RoleDto;

public interface RoleService {

	List<RoleDto> getRoles();

	Page<RoleDto> getRoles(Pageable pageable);

	RoleDto getRole(Long roleId);

	void createRole(RoleDto request);

	void updateRole(Long roleId, RoleDto request);

	void deleteRole(Long roleId);
}
