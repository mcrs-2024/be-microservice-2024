package com.microservice.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.core.admin.constant.dto.request.IntrospectRequest;
import com.microservice.core.admin.constant.dto.response.IntrospectResponse;
import com.microservice.core.constant.ApiResponse;
import com.microservice.core.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class CustomAuthenticationFilter extends AbstractGatewayFilterFactory<CustomAuthenticationFilter.Config> {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public CustomAuthenticationFilter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getPath().value();
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(!path.equals("/admin/login")){

                if (authHeader == null || authHeader.isEmpty()) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                return webClientBuilder.build()
                        .post()
                        .uri("http://localhost:8081/admin/introspect-request")
                        .header(HttpHeaders.AUTHORIZATION, authHeader)
                        .bodyValue(new IntrospectRequest(getTokenFromHeader(authHeader)))
                        .retrieve()
                        .bodyToMono(IntrospectResponse.class)
                        .flatMap(response -> {
                            if (response.isValid()) {
                                return chain.filter(exchange);
                            } else {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }
                        })
                        .onErrorResume(e -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        });
            }

            return extractBody(exchange)
                    .flatMap(body ->
                             webClientBuilder.build()
                            .post()
                            .uri("http://localhost:8081/admin/login")
                            .header(HttpHeaders.AUTHORIZATION, authHeader)
                            .bodyValue(new LoginRequest(body.getUsername(),body.getPassword()))
                            .retrieve()
                            .bodyToMono(ApiResponse.class)
                            .flatMap(response -> {
                                ServerHttpResponse serverResponse = exchange.getResponse();
                                serverResponse.setStatusCode(HttpStatus.OK);
                                try {
                                    byte[] jsonResponseBytes = objectMapper.writeValueAsBytes(
                                            ApiResponse.builder()
                                                    .data(response.getData())
                                                    .message(response.getMessage())
                                                    .code(response.getCode())
                                                    .build()
                                    );
                                    return serverResponse.writeWith(Mono.just(serverResponse.bufferFactory().wrap(jsonResponseBytes)));
                                } catch (Exception e) {
                                    serverResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                    return serverResponse.setComplete();
                                }
                            })
                            .onErrorResume(e -> {
                                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                return exchange.getResponse().setComplete();
                            })
                    );
        };
    }

    public Mono<LoginRequest> extractBody(ServerWebExchange exchange) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
                .map(dataBuffer -> {
                    byte[] bodyBytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bodyBytes);
                    DataBufferUtils.release(dataBuffer);
                    String body = new String(bodyBytes);
                    try {
                        return objectMapper.readValue(body, LoginRequest.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Lỗi xảy ra khi format ", e);
                    }
                });
    }

    public static class Config {
    }

    private String getTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        } else {
            throw new IllegalArgumentException("Lỗi!");
        }
    }
}
