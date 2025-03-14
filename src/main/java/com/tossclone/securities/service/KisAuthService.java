package com.tossclone.securities.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tossclone.securities.config.KisConfiguration;

@Service
public class KisAuthService {
	private final WebClient webClient;
	private final KisConfiguration kisConfiguration;

	private String accessToken;
	private LocalDateTime tokenExpiration;

	public KisAuthService(KisConfiguration kisConfiguration, WebClient.Builder webClientBuilder) {
		this.kisConfiguration = kisConfiguration;
		this.webClient = webClientBuilder.baseUrl(kisConfiguration.getBaseUrl()).build();
	}

	public String getAccessToken() {
		if (accessToken == null || isTokenExpired()) {
			requestAccessToken();
		}
		return accessToken;
	}

	private void requestAccessToken() {
		String uri = "/oauth2/tokenP";

		String response = webClient.post().uri(uri).bodyValue(Map.of("grant_type", "client_credentials", "appkey",
				kisConfiguration.getAppKey(), "appsecret", kisConfiguration.getAppSecret()

		)).retrieve().bodyToMono(String.class).block(); // 동기 처리

		parseAccessTokenResponse(response);
	}

	private void parseAccessTokenResponse(String response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(response);

			this.accessToken = jsonNode.get("access_token").asText();
			String expiredAtStr = jsonNode.get("access_token_token_expired").asText();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			this.tokenExpiration = LocalDateTime.parse(expiredAtStr, formatter);
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse access token response", e);
		}
	}

	private boolean isTokenExpired() {
		return tokenExpiration == null || LocalDateTime.now().isAfter(tokenExpiration);
	}
}
