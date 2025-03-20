package com.tossclone.securities.dto;

public class StockQuote {
	private String code, name;
	private long price, changeAmount;
	private double changeRate;

	public StockQuote() {
		super();
	}

	public StockQuote(String code, String name, long price, long changeAmount, double changeRate) {
		super();
		this.code = code;
		this.name = name;
		this.price = price;
		this.changeAmount = changeAmount;
		this.changeRate = changeRate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(long changeAmount) {
		this.changeAmount = changeAmount;
	}

	public double getChangeRate() {
		return changeRate;
	}

	public void setChangeRate(double changeRate) {
		this.changeRate = changeRate;
	}

}
