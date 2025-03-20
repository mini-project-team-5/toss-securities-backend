package com.tossclone.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.dto.Comment;
import com.tossclone.securities.service.AuthService;
import com.tossclone.securities.service.MemberService;
import com.tossclone.securities.util.JwtUtil;

@RestController
@RequestMapping("/api/comments")
public class CommunityController {

	@Autowired
	AuthService authService;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	MemberService memberService;

	@PostMapping
	public ResponseEntity<String> createComment(@RequestHeader("Authorization") String authorization,
			@RequestBody Comment comment) {

		if (!authService.isTokenValid(authorization)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
		}

		Long userId = Long.parseLong(jwtUtil.extractUserId(authorization));
		String userName = memberService.getUserNameByUserId(userId);

		comment.setUserId(userId);
		comment.setUserName(userName);
		
		// commentService ~
		return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");
	}
}
