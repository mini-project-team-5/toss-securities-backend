package com.tossclone.securities.service;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tossclone.securities.dao.WishDao;
import com.tossclone.securities.dto.Wish;

@Service
public class WishService {
	@Autowired
	WishDao wishDao;
	
	public void addWish(Wish wish) throws SQLException {
		wishDao.addWish(wish);
	}
}
