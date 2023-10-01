package cn.fuxi.config.handler;

import cn.fuxi.utils.ResponseHelper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import reactor.core.publisher.Mono;

/**
 * 自定义认证失败返回
 *
 * @author eddie.lys
 * @since 2023/10/1
 */
public class CustomServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return ResponseHelper.writeWithFail(webFilterExchange.getExchange().getResponse(), exception.getMessage());
    }

}
