package com.tastyfood.omf.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancedRoutesConfig {

	@Bean
	public RouteLocator loadBalancedRoutes(RouteLocatorBuilder builder) {
		return builder.routes().route(r -> r
				.path("/api/v1/customer*", "/api/v1/customer/*", "/api/v1/order/*", "/api/v1/order*")
				.filters(f -> f.circuitBreaker(c -> c.setName("circuit-breaker").setFallbackUri("forward:/failover")))
				.uri("lb://omf-order-management")).route(r -> r.path("/failover**").uri("lb://omf-gateway"))
				.route(r -> r.path("/api/v1/restaurant/**", "/api/v1/menu/**")
						.filters(f -> f.circuitBreaker(c -> c.setName("circuit-breaker")
								.setFallbackUri("forward:/failover").setRouteId("service-failover")))
						.uri("lb://omf-restaurants"))
				.route(r -> r.path("/failover**").uri("lb://omf-gateway")).build();
	}

}
