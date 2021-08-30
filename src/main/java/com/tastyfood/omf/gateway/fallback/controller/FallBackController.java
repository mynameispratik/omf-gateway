package com.tastyfood.omf.gateway.fallback.controller;


import java.nio.charset.StandardCharsets;
import java.time.Instant;


import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallBackController {
	
	@RequestMapping("/failover")
	public Mono<Void> fallback(ServerHttpResponse httpResponse){
		httpResponse.setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
		String json = "{\"message\":\"service unavailable\",\"status\":\"503\", \"timestamp\":\""+ Instant.now()+"\"}";
		byte[] bytes=json.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer=(DataBuffer) httpResponse.bufferFactory().wrap(bytes);
		return httpResponse.writeWith(Mono.just(buffer));
	}

}
