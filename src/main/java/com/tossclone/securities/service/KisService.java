package com.tossclone.securities.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tossclone.securities.confog.KisConfiguration;

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

    public Mono<String> getVolumeRank() {
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
                .bodyToMono(String.class);
    }
}

