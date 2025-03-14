package com.tossclone.securities.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.tossclone.securities.config.KisConfiguration;
import com.tossclone.securities.dto.Stock;

import reactor.core.publisher.Mono;

@Service
public class KisService {

    private final KisConfiguration kisConfiguration;
    private final WebClient webClient;

    public KisService(KisConfiguration kisConfiguration, WebClient.Builder webClientBuilder) {
        this.kisConfiguration = kisConfiguration;
        this.webClient = webClientBuilder.baseUrl(kisConfiguration.getBaseUrl()).build();
    }

    private HttpHeaders createVolumeRankHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(kisConfiguration.getAccessToken());
        headers.set("appKey", kisConfiguration.getAppKey());
        headers.set("appSecret", kisConfiguration.getAppSecret());
        headers.set("tr_id", "FHPST01710000");
        headers.set("custtype", "P");
        return headers;
    }

    public Mono<List<Stock>> getVolumeRank() {
        String uri = "/uapi/domestic-stock/v1/quotations/volume-rank";
        
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(uri)
                        .queryParam("FID_COND_MRKT_DIV_CODE", "J")
                        .queryParam("FID_COND_SCR_DIV_CODE", "20171")
                        .queryParam("FID_INPUT_ISCD", "0000")
                        .queryParam("FID_DIV_CLS_CODE", "0")
                        .queryParam("FID_BLNG_CLS_CODE", "0")
                        .queryParam("FID_TRGT_CLS_CODE", "111111111")
                        .queryParam("FID_TRGT_EXLS_CLS_CODE", "0000000000")
                        .queryParam("FID_INPUT_PRICE_1", "")
                        .queryParam("FID_INPUT_PRICE_2", "")
                        .queryParam("FID_VOL_CNT", "")
                        .queryParam("FID_INPUT_DATE_1", "")
                        .build())
                .headers(headers -> headers.addAll(createVolumeRankHttpHeaders()))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(this::parseVolumeRankResponse);
    }
    
    private List<Stock> parseVolumeRankResponse(JsonNode response) {
        List<Stock> stockList = new ArrayList<>();
        
        JsonNode dataArray = response.get("output");
        
        if (dataArray != null && dataArray.isArray()) {
            for (JsonNode stockData : dataArray) {
                Stock stock = new Stock();
                stock.setCode(stockData.get("mksc_shrn_iscd").asText());
                stock.setRank(stockData.get("data_rank").asInt()); 
                stock.setName(stockData.get("hts_kor_isnm").asText()); 
                stock.setPrice(stockData.get("stck_prpr").asLong());
                stock.setRate(Math.round(stockData.get("prdy_ctrt").asDouble() * 10.0) / 10.0);
                stock.setVolume(stockData.get("acml_vol").asLong() / 100); 
                
                stockList.add(stock);
            }
        }
        
        return stockList;
    }
}

