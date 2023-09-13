package cn.fuxi.authorization;

import cn.fuxi.common.user.LoginTypeEnums;
import cn.fuxi.inside.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author eddie.lys
 * @since 2023/4/15
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailServiceImpl implements ReactiveUserDetailsService {

    /**
     * SSO认证中心服务
     */
    private final AuthenticationService authenticationService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authenticationService.loginByMultipleWays(username, null, LoginTypeEnums.ACCOUNT);
    }
}