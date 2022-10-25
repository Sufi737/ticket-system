package com.system.gatewayserver.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import reactor.core.publisher.Mono;

@Configuration
public class ResponseFilter {

	@Autowired
	Tracer tracer;
	
	@Autowired
	FilterUtils filterUtils;
	
	final Logger logger =LoggerFactory.getLogger(ResponseFilter.class);
	
	@Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
        	final String traceId = Optional.ofNullable(tracer.currentSpan())
                    .map(Span::context)
                    .map(TraceContext::traceIdString)
                    .orElse("null");
            
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            	logger.debug("Adding the correlation id to the outbound headers. {}", traceId);
                exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
                logger.debug("Completing outgoing request for {}.",
                        exchange.getRequest().getURI());
            }));
        };
    }
}
