package com.tossclone.securities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.tossclone.securities.config.KisConfiguration;

@SpringBootApplication
@PropertySource("classpath:config/secu.properties")
@EnableConfigurationProperties(KisConfiguration.class)
@EnableScheduling 
public class TossSecuritiesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TossSecuritiesBackendApplication.class, args);
	}

}
