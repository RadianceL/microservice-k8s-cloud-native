package cn.fuxi.security.handler;

import cn.fuxi.common.reids.GlobalRedisKeys;
import cn.fuxi.common.user.UserInfo;
import cn.fuxi.data.WebAuthToken;
import cn.fuxi.utils.JwtHelper;
import cn.fuxi.utils.ResponseCookieHelper;
import cn.fuxi.utils.ResponseHelper;
import com.olympus.base.utils.support.utils.UncheckCastUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * 自定义认证成功流程
 *
 * @author eddie.lys
 * @since 2023/10/1
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    /**
     * redis 仓库
     */
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        UserInfo userInfo = UncheckCastUtil.castUncheckedObject(authentication.getPrincipal());
        WebAuthToken webAuthToken = JwtHelper.createToken(userInfo);

        // 配置认证key 3天过期
        redisTemplate.opsForValue().set(GlobalRedisKeys.getRedisLoginByPrefix(userInfo.getUsername()),
                webAuthToken.getRefreshToken(), 3, TimeUnit.DAYS);
        // 配置刷新key 30天过期
        redisTemplate.opsForValue().set(GlobalRedisKeys.getRedisLoginByPrefix(webAuthToken.getRefreshToken()), userInfo, 30, TimeUnit.DAYS);

        webFilterExchange.getExchange().getResponse().addCookie(ResponseCookieHelper.createCookie("X-Auth-Token", webAuthToken.getAuthToken()));
        webFilterExchange.getExchange().getResponse().addCookie(ResponseCookieHelper.createCookie("X-Refresh-Token", webAuthToken.getRefreshToken()));
        return ResponseHelper.writeWithSuccess(webFilterExchange.getExchange().getResponse(), webAuthToken);
    }
}
