package cn.fuxi.config.authentication;

import cn.fuxi.common.user.LoginTypeEnums;
import cn.fuxi.inside.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

/**
 * 授权认证管理器
 * @author eddie.lys
 * @since 2023/5/11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationProvider implements ReactiveAuthenticationManager {

    /**
     * 登录鉴权
     */
    private final AuthenticationService authenticationService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication instanceof UserAuthenticationToken userAuthenticationToken) {
            String loginAccount = userAuthenticationToken.getName();
            String credentials = userAuthenticationToken.getCredentials().toString();

            return authenticationService.loginByMultipleWays(loginAccount, credentials, userAuthenticationToken.getLoginType())
                    .filter((userDetails -> userDetails.getPassword().equals(credentials)))
                    .doOnNext(userDetails -> {})
                    .switchIfEmpty(Mono.error(new BadCredentialsException("Invalid login user or password")))
                    .map(this::createUsernamePasswordAuthenticationToken);
        }
        return Mono.empty();
    }

    private UsernamePasswordAuthenticationToken createUsernamePasswordAuthenticationToken(UserDetails userDetails) {
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, userDetails.getPassword(),
                userDetails.getAuthorities());
    }
}
