package cn.fuxi.security.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 放行URL路径
 *
 * @author eddie.lys
 * @since 2023/10/9
 */
@Getter
@AllArgsConstructor
public enum PassThroughUrlConstant {

    PASS_THROUGH_LOGIN("/api/login"),

    PASS_THROUGH_REGISTER("/sso/api/user/register");

    private final String url;

}
