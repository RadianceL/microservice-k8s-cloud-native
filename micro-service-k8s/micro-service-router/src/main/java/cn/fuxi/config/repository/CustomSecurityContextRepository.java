package cn.fuxi.config.repository;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义存储器
 *
 * @author eddie.lys
 * @since 2023/9/27
 */
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated("user.getUsername()", "password",
                List.of(new SimpleGrantedAuthority("GUEST")));
        authentication.setDetails(request);
        securityContext.setAuthentication(authentication);
        // 返回
        return Mono.just(securityContext);
    }
}
