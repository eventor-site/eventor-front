package com.eventorfront.userrole.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.role.dto.RoleDto;

@FeignClient(name = "userRole-client", url = "${feignClient.url}")
public interface UserRoleClient {

	@GetMapping("/back/users/{userId}/roles")
	ResponseEntity<List<RoleDto>> getUserRoles(@PathVariable Long userId);

	@GetMapping("/back/users/{userId}/roles/unassigned")
	ResponseEntity<List<RoleDto>> getUnassignedUserRoles(@PathVariable Long userId);

	@PostMapping("/back/users/{userId}/roles")
	ResponseEntity<Void> createUserRole(@PathVariable Long userId, @RequestParam Long roleId);

	@DeleteMapping("/back/users/{userId}/roles/{roleId}")
	ResponseEntity<Void> deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId);
}
