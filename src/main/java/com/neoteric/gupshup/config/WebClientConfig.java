package com.neoteric.gupshup.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {



    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.example.com")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
