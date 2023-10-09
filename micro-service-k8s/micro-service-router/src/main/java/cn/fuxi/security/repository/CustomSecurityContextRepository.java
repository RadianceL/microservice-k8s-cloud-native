package cn.fuxi.security.repository;

import cn.fuxi.common.reids.GlobalRedisKeys;
import cn.fuxi.common.user.UserInfo;
import cn.fuxi.security.constant.PassThroughUrlConstant;
import cn.fuxi.utils.JwtHelper;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.common.collect.Lists;
import com.olympus.base.utils.collection.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
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
import java.util.Objects;

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

    /**
     * redis 仓库
     */
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // 不对登陆接口生效
        if (PassThroughUrlConstant.PASS_THROUGH_URL_LIST.contains(exchange.getRequest().getPath().toString())) {
            return Mono.empty();
        }
        HttpCookie authTokenCookie = exchange.getRequest().getCookies().getFirst("X-Auth-Token");
        HttpCookie refreshTokenCookie = exchange.getRequest().getCookies().getFirst("X-Auth-Token");
        if (Objects.isNull(authTokenCookie) || Objects.isNull(refreshTokenCookie)) {
            return Mono.empty();
        }
        // 不对未设置
        String authToken = authTokenCookie.getValue();
        String refreshToken = refreshTokenCookie.getValue();
        if (StringUtils.isBlank(authToken) || StringUtils.isBlank(refreshToken)) {
            return Mono.empty();
        }

        String refreshTokenKey;
        try {
            // 通常这里不会返回全量用户信息
            // 通过用户信息取 refreshTokenRedis
            UserInfo userInfo = JwtHelper.verifyToken(authToken);
            // 超过3天有效期 此时默认需要重新登陆
            Object refreshTokenRedis = redisTemplate.opsForValue().get(GlobalRedisKeys.getRedisLoginByPrefix(userInfo.getUsername()));
            if (Objects.isNull(refreshTokenRedis)) {
                return Mono.empty();
            }
            refreshTokenKey = refreshTokenRedis.toString();
            refreshTokenKey = refreshTokenKey.replaceAll("\"", "");
        }catch (SignatureVerificationException | AlgorithmMismatchException e) {
            // 签名不一致 && 算法错误
            throw new IllegalArgumentException("Signature Verification Error |  Algorithm Mismatch");
        }catch (TokenExpiredException tokenExpiredException) {
            // 令牌过期
            throw new IllegalArgumentException("Token Expired");
        }catch (InvalidClaimException e) {
            // 失效的Payload异常
            // 获取token的服务器比使用token的服务器时钟快，请求分发到时间慢的服务器上导致token还没生效
            throw new IllegalArgumentException("Invalid Claim, Please try again");
        }

        Object userInfoOriginalJson = redisTemplate.opsForValue().get(GlobalRedisKeys.getRedisLoginByPrefix(refreshTokenKey));
        if (Objects.isNull(userInfoOriginalJson)) {
            return Mono.empty();
        }
        UserInfo user = JSON.parseObject(userInfoOriginalJson.toString(), UserInfo.class);
        List<String> authoritiesList = user.getAuthoritiesRoles();
        List<SimpleGrantedAuthority> authorities;
        if(CollectionUtils.isNotEmpty(authoritiesList)) {
            authorities = authoritiesList.stream().map(SimpleGrantedAuthority::new).toList();
        }else {
            authorities = Lists.newArrayList();
        }
        ServerHttpRequest request = exchange.getRequest();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken
                .authenticated(user.getUsername(), user.getPassword(), authorities);
        usernamePasswordAuthenticationToken.setDetails(request);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        return Mono.just(securityContext);
    }
}
