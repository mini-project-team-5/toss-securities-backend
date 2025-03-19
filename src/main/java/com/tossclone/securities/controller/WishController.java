package com.tossclone.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/wish/{userId}")
	public List<Wish> getWishList(@PathVariable int userId) {
		try {
			return wishService.getWishList(userId);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@PostMapping("add-wish")
	public String addWish(@RequestBody Wish wish) {
		try {
			wishService.addWish(wish);
			return "ok";

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@DeleteMapping("/wish/{userId}/{stockCode}")
	public String deleteWish(@PathVariable int userId, @PathVariable String stockCode) {
		try {
			wishService.deleteWish(userId, stockCode);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
