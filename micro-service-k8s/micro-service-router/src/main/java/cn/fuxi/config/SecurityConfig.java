package cn.fuxi.config;

import cn.fuxi.config.handler.*;
import cn.fuxi.config.repository.CustomSecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;

/**
 * @author eddie.lys
 * @since 2023/4/15
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {
    /**
     * 自定义认证管理器
     */
    private final ReactiveAuthenticationManager authenticationManager;
    /**
     * 自定义认证上下文（JWT token鉴权）
     */
    private final CustomSecurityContextRepository customSecurityContextRepository;
    /**
     * 自定义认证成功流程
     */
    private final CustomServerAuthenticationSuccessHandler customServerAuthenticationSuccessHandler;

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http){
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authenticationManager);
        // 配置认证路径
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/login"));
        // 配置自定义转换器
        authenticationFilter.setServerAuthenticationConverter(new CustomServerAuthenticationConverter());
        // 认证成功处理器
        authenticationFilter.setAuthenticationSuccessHandler(customServerAuthenticationSuccessHandler);
        // 认证失败处理器
        authenticationFilter.setAuthenticationFailureHandler(new CustomServerAuthenticationFailureHandler());

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 获取上次登陆JWT token校验
                .securityContextRepository(customSecurityContextRepository)
                // 配置权限
                .authorizeExchange(authorizeExchangeSpec ->
                        // 登陆API 不设访问权限
                        authorizeExchangeSpec.pathMatchers(HttpMethod.POST, "/api/login")
                                .permitAll()
                                // 拒绝所有对provider 路径的访问
                                .pathMatchers("/provider/**")
                                .denyAll()
                                // 允许所有OPTIONS 的操作
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                // 要求所有/api/product/ 的访问具有 ROLE_ADMIN 权限 或者 CUSTOMER 权限
                                .pathMatchers(HttpMethod.POST, "/api/product/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("CUSTOMER").check(authentication, context)))
                                // 要求所有/api/manager/ 的访问具有 ROLE_ADMIN 权限 或者 DBA 权限
                                .pathMatchers(HttpMethod.POST, "/api/manager/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("DBA").check(authentication, context)))
                                // 要求所有/certification/** 的访问具有 ROLE_ADMIN 权限
                                .pathMatchers(HttpMethod.POST, "/certification/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted()))
                                // 其他所有路径则需要认证
                                .anyExchange().authenticated()
                )
                // 使用自定义body获取username & password
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                // 使用form-data获取username & password
//                .formLogin(formLoginSpec ->
//                        formLoginSpec.loginPage("/api/login")
//                                .authenticationManager(authenticationManager)
//                                .authenticationSuccessHandler(customServerAuthenticationSuccessHandler)
//                                .authenticationFailureHandler(new CustomServerAuthenticationFailureHandler())
                // 配置登出接口和处理器
                .logout(logoutSpec -> {
                    logoutSpec.logoutUrl("/api/logout");
                    logoutSpec.logoutHandler(new SecurityContextServerLogoutHandler());
//                    logoutSpec.logoutSuccessHandler((exchange, authentication) -> null);
                })
                // 配置异常处理
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
}
