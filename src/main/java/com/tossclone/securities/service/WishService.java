package com.tossclone.securities.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tossclone.securities.dao.WishDao;
import com.tossclone.securities.dto.Wish;

@Service
public class WishService {
	@Autowired
	WishDao wishDao;

	public List<Wish> getWishList(int userId) throws Exception {
		return wishDao.getWishList(userId);
	}

	public void addWish(Wish wish) throws Exception {
		wishDao.addWish(wish);
	}

	public void deleteWish(int userId, String stockCode) throws Exception {
		wishDao.deleteWish(userId, stockCode);
	}

}
