package com.tossclone.securities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:config/secu.properties")
public class TossSecuritiesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TossSecuritiesBackendApplication.class, args);
	}

}
