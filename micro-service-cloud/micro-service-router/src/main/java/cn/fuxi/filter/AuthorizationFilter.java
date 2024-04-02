package cn.fuxi.filter;

import cn.fuxi.common.keys.headers.RequestHeaders;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
                .map(authentication -> {
                    // authentication如果为空则不会进来，所以authentication.getName();不需要判断空
                    // CustomSecurityContextRepository 设置当前安全上下文中的用户信息
                    // UserInfo userInfo = authentication.getDetails();
                    ServerHttpRequest httpRequest = exchange.getRequest().mutate()
                            .header(RequestHeaders.X_AUTHENTICATED_USER_INFO, JSON.toJSONString(authentication.getDetails()))
                            .header(RequestHeaders.X_TRACE_ID, JSON.toJSONString(authentication.getDetails()))
                            .build();
                    exchange.mutate().request(httpRequest);
                    return exchange;
                })
                .switchIfEmpty(Mono.just(exchange))
                .flatMap(chain::filter);
    }

    @Override
    public int getOrder() {
        // 表示最高优先级执行
        return Ordered.HIGHEST_PRECEDENCE;
    }
}