package com.rajesh.keycloak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class UserResourceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserResourceClientApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public WebClient webClient(ClientRegistrationRepository clientRegistrationRepository,
							   OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository){

		ServletOAuth2AuthorizedClientExchangeFilterFunction oaauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
				clientRegistrationRepository, oAuth2AuthorizedClientRepository
		);

		oaauth2.setDefaultOAuth2AuthorizedClient(true);

		return WebClient.builder().apply(oaauth2.oauth2Configuration()).build();
	}

}
