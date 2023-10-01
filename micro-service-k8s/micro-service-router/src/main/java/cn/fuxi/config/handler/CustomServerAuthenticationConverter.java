package cn.fuxi.config.handler;

import cn.fuxi.common.user.LoginTypeEnums;
import cn.fuxi.config.authentication.UserAuthenticationToken;
import cn.fuxi.data.LoginCredentials;
import com.google.common.collect.Lists;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 自定义参数转换器
 * 从request body中获取认证信息
 * @author eddie.lys
 * @since 2023/10/1
 */
public class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        ServerRequest serverRequest = ServerRequest.create(exchange, Lists.newArrayList(new DecoderHttpMessageReader<>(new Jackson2JsonDecoder())));
        return serverRequest
                // 将 JSON 转换为自定义的 LoginCredentials 类
                .bodyToMono(LoginCredentials.class)
                // 处理 LoginCredentials -> UserAuthenticationToken
                .handle((userCredentials, sink) -> {
                    // 获取登陆方式
                    LoginTypeEnums loginTypeEnums = LoginTypeEnums.findLoginTypeEnums(userCredentials.getLoginType());
                    if (Objects.isNull(loginTypeEnums)) {
                        // 如果参数获取非法参数抛出认证异常
                        sink.error(new AuthenticationCredentialsNotFoundException("not support login-type"));
                        return;
                    }
                    // 根据 LoginCredentials 创建 Authentication 对象
                    sink.next(new UserAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword(), loginTypeEnums));
                });
    }
}
