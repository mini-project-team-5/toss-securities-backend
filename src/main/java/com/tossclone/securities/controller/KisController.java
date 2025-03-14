package com.tossclone.securities.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tossclone.securities.service.KisService;

import reactor.core.publisher.Mono;

@RestController
public class KisController {
	@Autowired
    private KisService kisService;

    @GetMapping("/volume-rank")
    public Mono<String> getVolumeRank() {
        return kisService.getVolumeRank();
    }
}
