package com.tossclone.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.dto.Wish;
import com.tossclone.securities.service.AuthService;
import com.tossclone.securities.service.WishService;
import com.tossclone.securities.util.JwtUtil;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class WishController {
	@Autowired
	WishService wishService;
	
	@Autowired
	AuthService authService;
	
	@Autowired
	JwtUtil jwtUtil;

	@GetMapping("/api/wishlist")
	public  ResponseEntity<List<Wish>> getWishList(@RequestHeader("Authorization") String authorization) {
		String token = authorization.substring(7);
		
		try {
			if (token == null || !authService.isTokenValid(token)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	        }

	        // 토큰에서 userId 추출
	        Long userId = jwtUtil.extractUserId(token);
	       System.out.println(userId);
	        
	        return ResponseEntity.ok(wishService.getWishList(userId));
	    
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

	@PostMapping("/api/wishlist")
	public ResponseEntity<String> addWish(@RequestHeader("Authorization") String authorization, @RequestBody Wish wish) {
		String token = authorization.substring(7);
		try {
	        if (token == null || !authService.isTokenValid(token)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
	        }

	        Long userId =jwtUtil.extractUserId(token);
	        wish.setUserId(userId); 

	        wishService.addWish(wish);
	        return ResponseEntity.ok("ok");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}

	@DeleteMapping("/api/wishlist/{stockCode}")
	public ResponseEntity<String> deleteWish(@RequestHeader("Authorization") String authorization, @PathVariable String stockCode) {
		String token = authorization.substring(7);
		try {
	        if (token == null || !authService.isTokenValid(token)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
	        }

	        Long userId =jwtUtil.extractUserId(token);

	        wishService.deleteWish(userId, stockCode);
	        return ResponseEntity.ok("ok");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
	    }
	}


}
