package cn.fuxi.config.handler;

import cn.fuxi.config.handler.utils.ResponseHelper;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 自定义无权限拦截器
 *
 * @author eddie.lys
 * @since 2023/9/27
 */
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        return ResponseHelper.writeWith(exchange.getResponse(), "自定义无权限拦截器");
    }
}
