package com.tossclone.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.dto.Wish;
import com.tossclone.securities.service.WishService;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class WishController {
	@Autowired
	WishService wishService;
	
	@PostMapping("add-wish")
	public String addWish(@RequestBody Wish wish) {
		try {
			wishService.addWish(wish);
			return "ok";
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
