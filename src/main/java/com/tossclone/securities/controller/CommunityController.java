package com.tossclone.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.dto.Comment;
import com.tossclone.securities.service.AuthService;
import com.tossclone.securities.service.CommunityService;
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
	
	@Autowired
	CommunityService communityService;
	
	@GetMapping
	public ResponseEntity<List<Comment>> getComments(@PathVariable String stockCode) {
		try {
			List<Comment> comments = communityService.getComments(stockCode);
			return ResponseEntity.ok(comments);
		} catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	@PostMapping
	public ResponseEntity<String> createComment(@RequestHeader("Authorization") String authorization,
	        @RequestBody Comment comment) {

	    try {
	        // 토큰 검증
	        if (authorization == null || !authService.isTokenValid(authorization)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
	        }

	        // 토큰에서 userId 추출
	        Long userId = Long.parseLong(jwtUtil.extractUserId(authorization));

	        // userId로 사용자 이름 조회
	        String userName = memberService.getUserNameByUserId(userId);

	        // 댓글 작성 정보 설정
	        comment.setUserId(userId);
	        comment.setUserName(userName);

	        // 댓글 서비스 호출
	        communityService.createComment(comment);

	        // 성공 응답 반환
	        return ResponseEntity.status(HttpStatus.CREATED).body("Comment created successfully");

	    
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the comment");
	    }
	}

}
