package com.ecommerce.apigateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // User Service
                .route("user", r -> r
                        .path("/users/**")
                        .filters(f -> f
                                .rewritePath("/users(?<segment>/?.*)", "/api/users${segment}")
                                .circuitBreaker(c -> c
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/userService")))
                        .uri("lb://USER"))

                // Product Service with Circuit Breaker
                .route("product", r -> r
                        .path("/products/**")
                        .filters(f -> f
                                .rewritePath("/products(?<segment>/?.*)", "/api/products${segment}")
                                .circuitBreaker(c -> c
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/productService")))
                        .uri("lb://PRODUCT"))

                // Order Service
                .route("order", r -> r
                        .path("/orders/**")
                        .filters(f -> f
                                .rewritePath("/orders(?<segment>/?.*)", "/api/orders${segment}")
                                .circuitBreaker(c -> c
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/orderService")))
                        .uri("lb://ORDER"))

                // Cart Service
                .route("cart", r -> r
                        .path("/cart/**")
                        .filters(f -> f
                                .rewritePath("/cart(?<segment>/?.*)", "/api/cart${segment}")
                                .circuitBreaker(c -> c
                                        .setName("ecomBreaker")
                                        .setFallbackUri("forward:/fallback/orderService")))
                        .uri("lb://ORDER"))
                .build();
    }
}