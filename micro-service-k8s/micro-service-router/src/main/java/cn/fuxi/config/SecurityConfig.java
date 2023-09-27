package cn.fuxi.config;

import cn.fuxi.common.user.UserInfo;
import cn.fuxi.config.filter.SmsCodeAuthenticationFilter;
import cn.fuxi.config.handler.CustomAccessDeniedHandler;
import cn.fuxi.config.handler.CustomAuthenticationEntryPoint;
import cn.fuxi.config.handler.utils.ResponseHelper;
import cn.fuxi.config.repository.CustomSecurityContextRepository;
import cn.fuxi.config.sms.SmsCodeAuthenticationToken;
import cn.fuxi.data.LoginData;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorityReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.DelegatingServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

/**
 * @author eddie.lys
 * @since 2023/4/15
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {

    private final RedisTemplate<String,String> redisTemplate;

    private final ReactiveAuthenticationManager authenticationManager;

    private final SmsCodeAuthenticationFilter smsCodeAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http){
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/login"));
        authenticationFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), exception.getMessage()));
        authenticationFilter.setServerAuthenticationConverter(exchange -> {
            ServerRequest serverRequest = ServerRequest.create(exchange, Lists.newArrayList(new DecoderHttpMessageReader<>(new Jackson2JsonDecoder())));
            return serverRequest
                    .bodyToMono(LoginData.class) // 将 JSON 转换为自定义的 MyUserCredentials 类
                    .map(userCredentials -> {
                        // 根据 MyUserCredentials 创建 Authentication 对象
                        return new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword());
                    });
        });
        authenticationFilter.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), authentication.getName()));
        
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(smsCodeAuthenticationFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                .securityContextRepository(new CustomSecurityContextRepository())
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.pathMatchers(HttpMethod.POST, "/api/login")
                                .permitAll()
                                .pathMatchers("/provider/**")
                                .denyAll()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers(HttpMethod.POST, "/api/product/**").access((authentication, context) ->
                                        hasRole("GUEST").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("ADMIN").check(authentication, context)))
                                .pathMatchers(HttpMethod.POST, "/api/manager/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("DBA").check(authentication, context)))
                                .anyExchange().authenticated()
                )
                // 使用自定义body获取username & password
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                // 使用form-data获取username & password
                .formLogin(formLoginSpec ->
                        formLoginSpec.loginPage("/api/login")
                                .authenticationManager(authenticationManager)
                                .authenticationFailureHandler((webFilterExchange, exception) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), exception.getMessage()))
                                .authenticationSuccessHandler((webFilterExchange, authentication) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), authentication.getName())))
                .logout(logoutSpec -> {
                    logoutSpec.logoutUrl("/api/logout");
                    logoutSpec.logoutHandler(new SecurityContextServerLogoutHandler());
//                    logoutSpec.logoutSuccessHandler((exchange, authentication) -> null);
                })
                .exceptionHandling(exceptionHandlingSpec -> {
                    exceptionHandlingSpec.accessDeniedHandler(new CustomAccessDeniedHandler());
                    exceptionHandlingSpec.authenticationEntryPoint(new CustomAuthenticationEntryPoint());
                })
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder encoder) {
        UserDetailsRepositoryReactiveAuthenticationManager manager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        manager.setPasswordEncoder(encoder);
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    private String decodeDataBuffer(DataBuffer dataBuffer) {
        // 这个方法用于将DataBuffer解码为字符串，你可以根据具体情况实现
        // 这里简单地将DataBuffer中的字节数组转换为字符串
        byte[] bytes = new byte[dataBuffer.readableByteCount()];
        dataBuffer.read(bytes);
        return new String(bytes);
    }
}
