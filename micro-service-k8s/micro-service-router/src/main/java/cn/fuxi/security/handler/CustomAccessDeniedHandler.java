package cn.fuxi.security.handler;

import cn.fuxi.utils.ResponseHelper;
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
        return ResponseHelper.writeWithFail(exchange.getResponse(), "自定义无权限拦截器");
    }
}
