package com.tossclone.securities.dto;

public class Stock {
	private String code, name;
	private int rank;
	private long price, volume;
	private double rate;

	public Stock() {
		super();
	}

	public Stock(String code, String name, int rank, long price, long volume, double rate) {
		super();
		this.code = code;
		this.name = name;
		this.rank = rank;
		this.price = price;
		this.volume = volume;
		this.rate = rate;
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

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "Stock [code=" + code + ", name=" + name + ", rank=" + rank + ", price=" + price + ", volume=" + volume
				+ ", rate=" + rate + "]";
	}
}
