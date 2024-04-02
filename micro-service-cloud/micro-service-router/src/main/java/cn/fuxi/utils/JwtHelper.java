package cn.fuxi.utils;

import cn.fuxi.common.user.UserInfo;
import cn.fuxi.data.WebAuthToken;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.olympus.base.utils.support.utils.Md5Utils;

import java.util.Calendar;

/**
 * jwt 生成器
 *
 * @author eddie.lys
 * @since 2023/10/1
 */
public class JwtHelper {
    /**
     * 密钥
     */
    private static final String SECRET = "4f9a143487bf08b77e984936e56c22e4c633c527073c80ab697e36f59f8be7d4";
    /**
     * 用户信息
     */
    private static final String CLAIM_KEY = "USER-INFO";

    /**
     * 生成认证token对象
     */
    public static WebAuthToken createToken(UserInfo userInfo) {
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, 12);

        String authToken = JWT.create()
                // 第二部分Payload (这部分一般不给全量信息)
                .withClaim(CLAIM_KEY, JSONObject.toJSONString(userInfo))
                .withExpiresAt(expires.getTime())
                // 第三部分Signature
                .sign(Algorithm.HMAC256(SECRET));
        WebAuthToken webAuthToken = new WebAuthToken();
        webAuthToken.setAuthToken(authToken);
        webAuthToken.setRefreshToken(Md5Utils.encode(authToken));
        return webAuthToken;
    }

    public static UserInfo verifyToken(String authToken) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT decodedToken = jwtVerifier.verify(authToken);
        String userInfo = decodedToken.getClaim(CLAIM_KEY).asString();
        if (StringUtils.isNotBlank(userInfo)) {
            return JSONObject.parseObject(userInfo, UserInfo.class);
        }
        throw new InvalidClaimException("auth token can`t find user-info");
    }
}
