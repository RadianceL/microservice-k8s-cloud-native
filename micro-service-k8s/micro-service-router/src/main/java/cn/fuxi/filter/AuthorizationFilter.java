package cn.fuxi.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("AuthorizationFilter filter.....{}", exchange.getRequest().getPath());
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .switchIfEmpty(Mono.error(new BadCredentialsException("Authentication context is empty")))
                .map(authentication -> {
                    String username = authentication.getName();
                    ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                            .header("X-Authenticated-User-Info", username)
                            .build();
                    exchange.mutate().request(httpRequest);
                    return exchange;
                })
                .flatMap(chain::filter);
    }

    @Override
    public int getOrder() {
        return 0;
    }

}