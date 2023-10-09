package cn.fuxi.security.constant;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * 放行URL路径
 *
 * @author eddie.lys
 * @since 2023/10/9
 */
public class PassThroughUrlConstant {

    public static final List<String> PASS_THROUGH_URL_LIST = Lists.newArrayList(
            "/api/login",
            "/sso/api/user/register"
    );
}
