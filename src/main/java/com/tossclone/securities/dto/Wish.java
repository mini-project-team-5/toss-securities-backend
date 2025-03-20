package com.tossclone.securities.dto;

public class Wish {
	private int wishId;
	private Long userId;
	private Stock stock;

	public Wish() {
		super();
	}

	public Wish(int wishId, Long userId, Stock stock) {
		super();
		this.wishId = wishId;
		this.userId = userId;
		this.stock = stock;
	}

	public int getWishId() {
		return wishId;
	}

	public void setWishId(int wishId) {
		this.wishId = wishId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Wish [wishId=" + wishId + ", userId=" + userId + ", stock=" + stock + "]";
	}

}
