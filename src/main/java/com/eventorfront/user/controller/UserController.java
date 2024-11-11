package com.eventorfront.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sikyeojofront.user.dto.response.GetUserByAddShopResponse;
import com.sikyeojofront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@GetMapping("/search")
	public ResponseEntity<List<GetUserByAddShopResponse>> searchUserById(@RequestParam String keyword) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.searchUserById(keyword));
	}

}