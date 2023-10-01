package cn.fuxi.common.reids;

import com.alibaba.excel.util.StringUtils;

/**
 * 全局redis路径配置
 *
 * @author eddie.lys
 * @since 2023/10/1
 */
public class GlobalRedisKeys {

    /**
     * 全局认证KEY
     */
    public static final String REDIS_LOGIN_PREFIX = "SSO-USER:LOGIN-SESSION-ID:";

    public static String getRedisLoginByPrefix(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            throw new IllegalArgumentException("refresh token can`t be blank");
        }
        return REDIS_LOGIN_PREFIX.concat(refreshToken);
    }
}
