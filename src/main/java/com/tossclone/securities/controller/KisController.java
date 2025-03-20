package com.tossclone.securities.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.dto.Stock;
import com.tossclone.securities.dto.StockQuote;
import com.tossclone.securities.service.KisService;

import reactor.core.publisher.Mono;

@RestController
public class KisController {
	@Autowired
	private KisService kisService;

	@GetMapping("/api/volume-rank")
	public Mono<List<Stock>> getVolumeRank() {
		return kisService.getVolumeRank();
	}
	
	@GetMapping("/api/stock-quote/{stockCode}")
	public Mono<StockQuote> getStockQuote(@PathVariable String stockCode) {
		return kisService.getStockQuote(stockCode);
	}
}
