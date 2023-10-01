package cn.fuxi.config;

import cn.fuxi.common.user.LoginTypeEnums;
import cn.fuxi.config.authentication.UserAuthenticationToken;
import cn.fuxi.config.handler.CustomAccessDeniedHandler;
import cn.fuxi.config.handler.CustomAuthenticationEntryPoint;
import cn.fuxi.utils.ResponseHelper;
import cn.fuxi.config.repository.CustomSecurityContextRepository;
import cn.fuxi.data.LoginCredentials;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
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
import org.springframework.web.reactive.function.server.ServerRequest;

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

    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http){
        AuthenticationWebFilter authenticationFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/api/login"));
        authenticationFilter.setServerAuthenticationConverter(exchange -> {
            ServerRequest serverRequest = ServerRequest.create(exchange, Lists.newArrayList(new DecoderHttpMessageReader<>(new Jackson2JsonDecoder())));
            return serverRequest
                    // 将 JSON 转换为自定义的 LoginCredentials 类
                    .bodyToMono(LoginCredentials.class)
                    // 转换为登陆对象
                    .map(userCredentials -> {
                        LoginTypeEnums loginTypeEnums = LoginTypeEnums.findLoginTypeEnums(userCredentials.getLoginType());
                        // 根据 LoginCredentials 创建 Authentication 对象
                        return new UserAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword(), loginTypeEnums);
                    });
        });
        authenticationFilter.setAuthenticationSuccessHandler((webFilterExchange, authentication) -> ResponseHelper.writeWithSuccess(webFilterExchange.getExchange().getResponse(), authentication.getName()));
        authenticationFilter.setAuthenticationFailureHandler((webFilterExchange, exception) -> ResponseHelper.writeWithSuccess(webFilterExchange.getExchange().getResponse(), exception.getMessage()));

        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                // 获取上次登陆JWT token校验
                .securityContextRepository(customSecurityContextRepository)
                .authorizeExchange(authorizeExchangeSpec ->
                        authorizeExchangeSpec.pathMatchers(HttpMethod.POST, "/api/login")
                                .permitAll()
                                .pathMatchers("/provider/**")
                                .denyAll()
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers(HttpMethod.POST, "/api/product/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("CUSTOMER").check(authentication, context)))
                                .pathMatchers(HttpMethod.POST, "/api/manager/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted())
                                                .switchIfEmpty(hasRole("DBA").check(authentication, context)))
                                .pathMatchers(HttpMethod.POST, "/certification/**").access((authentication, context) ->
                                        hasRole("ADMIN").check(authentication, context)
                                                .filter(decision -> !decision.isGranted()))
                                .anyExchange().authenticated()
                )
                // 使用自定义body获取username & password
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.FORM_LOGIN)
                // 使用form-data获取username & password
//                .formLogin(formLoginSpec ->
//                        formLoginSpec.loginPage("/api/login")
//                                .authenticationManager(authenticationManager)
//                                .authenticationFailureHandler((webFilterExchange, exception) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), exception.getMessage()))
//                                .authenticationSuccessHandler((webFilterExchange, authentication) -> ResponseHelper.out(webFilterExchange.getExchange().getResponse(), authentication.getName())))
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
}
