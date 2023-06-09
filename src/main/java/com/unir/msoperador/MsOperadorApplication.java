package com.unir.msoperador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsOperadorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOperadorApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTeamplate() {
	    RestTemplate restTemplate = new RestTemplate();
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	    restTemplate.setRequestFactory(requestFactory);
	    return restTemplate;
	}

}
