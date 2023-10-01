package cn.fuxi.config.repository;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.olympus.base.utils.collection.CollectionUtils;
import com.olympus.domain.MicroSsoUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义存储器
 *
 * @author eddie.lys
 * @since 2023/9/27
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomSecurityContextRepository implements ServerSecurityContextRepository {

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // 不对登陆接口生效
        if (exchange.getRequest().getPath().toString().equals("/api/login")) {
            return Mono.empty();
        }
        // 不对未设置
        String token = exchange.getRequest().getHeaders().getFirst("X-Auth-Token");
        if (token == null) {
            return Mono.empty();
        }

        MicroSsoUser user = JSON.parseObject("{}", MicroSsoUser.class);
        List<String> authoritiesList = user.getAuthorities();
        List<SimpleGrantedAuthority> authorities;
        if(CollectionUtils.isNotEmpty(authoritiesList)) {
            authorities = authoritiesList.stream().map(SimpleGrantedAuthority::new).toList();
        }else {
            authorities = Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        ServerHttpRequest request = exchange.getRequest();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken
                .authenticated("user.getUsername()", "0000", authorities);
        usernamePasswordAuthenticationToken.setDetails(request);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        return Mono.just(securityContext);
    }
}
