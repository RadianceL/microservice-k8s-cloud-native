package cn.fuxi.inside.service;

import cn.fuxi.common.user.LoginTypeEnums;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * @author eddie.lys
 * @since 2023/5/11
 */
public interface AuthenticationService {

    /**
     * 通过用户名查询
     * @param loginRef      登录索引
     * @param loginType     登录方式
     */
    Mono<UserDetails> loginByMultipleWays(String loginRef, String loginCertificate, @NotNull LoginTypeEnums loginType);
}
