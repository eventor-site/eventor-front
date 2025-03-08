package com.eventorfront.role.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.role.dto.RoleDto;

@FeignClient(name = "role-client", url = "${feignClient.url}")
public interface RoleClient {

	@GetMapping("/back/roles/paging")
	ResponseEntity<ApiResponse<Page<RoleDto>>> getRoles(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/roles/{roleId}")
	ResponseEntity<ApiResponse<RoleDto>> getRole(@PathVariable Long roleId);

	@PostMapping("/back/roles")
	ResponseEntity<ApiResponse<Void>> createRole(@RequestBody RoleDto request);

	@PutMapping("/back/roles/{roleId}")
	ResponseEntity<ApiResponse<Void>> updateRole(@PathVariable Long roleId, @RequestBody RoleDto request);

	@DeleteMapping("/back/roles/{roleId}")
	ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Long roleId);
}
