package cn.fuxi.common.user;

import com.alibaba.excel.util.StringUtils;

import java.util.Arrays;

/**
 * 登录方式
 *
 * @author eddie.lys
 * @since 2023/5/11
 */
public enum LoginTypeEnums {
    /**
     * 账号密码登录
     */
    ACCOUNT,
    /**
     * 短信验证码登录
     */
    PHONE_VERIFY,
    /**
     * 微信扫码登录
     */
    WECHAT;

    public static LoginTypeEnums findLoginTypeEnums(String loginType) {
        if (StringUtils.isBlank(loginType)) {
            return LoginTypeEnums.ACCOUNT;
        }
        return Arrays.stream(LoginTypeEnums.values())
                .filter(loginTypeEnum -> loginTypeEnum.name().equals(loginType))
                .findAny()
                .orElse(null);
    }

}
