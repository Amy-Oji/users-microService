package com.amyojiakor.userMicroService.apiConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class ApiConfig {
    @Value("${myapp.api.base-url.accounts-service}")
    private String accountServiceBaseUrl;

    @Value("${myapp.api.base-url.transactions-service}")
    private String transactionsServiceBaseUrl;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced
    public WebClient accountsServiceWebClient() {
        return WebClient.builder()
                .baseUrl(getAccountServiceBaseUrl())
                .defaultHeader("Authorization", "Bearer token")
                .defaultHeader("Content-Type", (MediaType.APPLICATION_JSON_VALUE))
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }


    @Bean
    @LoadBalanced
    public WebClient transactionsServiceWebClient() {
        return WebClient.builder()
                .baseUrl(getTransactionsServiceBaseUrl())
                .defaultHeader("Authorization", "Bearer token")
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .filter(logRequest())
                .filter(logResponse())
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            System.out.println("Request: " + request.method() + " " + request.url());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            System.out.println("Response status: " + response.statusCode());
            return Mono.just(response);
        });
    }

    public String getAccountServiceBaseUrl() {
        return accountServiceBaseUrl;
    }

    private String getTransactionsServiceBaseUrl() {
        return transactionsServiceBaseUrl;
    }
}
