package com.microservice.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayConfig {
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        return new CustomAuthenticationFilter(webClientBuilder, objectMapper);
    }
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, CustomAuthenticationFilter customAuthenticationFilter) {
        return builder.routes()
                .route(predicateSpec -> predicateSpec
                        .path("/admin/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://admin-service")
                )
                .route(predicateSpec -> predicateSpec
                        .path("/report/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://report-service")
                )
                .route(predicateSpec -> predicateSpec
                        .path("/personnel/**")
                        .filters(f -> f.filter(customAuthenticationFilter.apply(new CustomAuthenticationFilter.Config())))
                        .uri("lb://personnel-service")
                )
                .build();
    }
}
