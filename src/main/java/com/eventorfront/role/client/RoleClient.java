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

import com.eventorfront.role.dto.RoleDto;

@FeignClient(name = "role-client", url = "http://localhost:8090/back/roles")
public interface RoleClient {

	@GetMapping("/paging")
	ResponseEntity<Page<RoleDto>> getRoles(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/{roleId}")
	ResponseEntity<RoleDto> getRole(@PathVariable Long roleId);

	@PostMapping
	ResponseEntity<Void> createRole(@RequestBody RoleDto request);

	@PutMapping("/{roleId}")
	ResponseEntity<Void> updateRole(@PathVariable Long roleId, @RequestBody RoleDto request);

	@DeleteMapping("/{roleId}")
	ResponseEntity<Void> deleteRole(@PathVariable Long roleId);
}
