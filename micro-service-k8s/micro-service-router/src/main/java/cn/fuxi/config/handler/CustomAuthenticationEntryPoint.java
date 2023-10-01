package cn.fuxi.config.handler;

import cn.fuxi.utils.ResponseHelper;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义无权限拦截器
 *
 * @author eddie.lys
 * @since 2023/9/27
 */
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        return ResponseHelper.writeWithFail(response, "自定义未授权拦截器");
    }
}
