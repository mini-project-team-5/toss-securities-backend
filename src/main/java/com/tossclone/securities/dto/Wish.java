package com.tossclone.securities.dto;

public class Wish {
	private int userId;
	private String stockCode;

	public Wish() {
		super();
	}

	public Wish(int userId, String stockCode) {
		this.userId = userId;
		this.stockCode = stockCode;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	@Override
	public String toString() {
		return "Wish [userId=" + userId + ", stockCode=" + stockCode + "]";
	}

}
