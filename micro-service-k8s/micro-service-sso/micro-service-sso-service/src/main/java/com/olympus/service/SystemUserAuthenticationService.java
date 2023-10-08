package com.olympus.service;

import cn.fuxi.common.user.UserBaseInfo;

/**
 * 系统用户认证服务
 *
 * @author eddie.lys
 * @since 2023/10/8
 */
public interface SystemUserAuthenticationService {

    /**
     * 根据账号查询认证信息
     */
    UserBaseInfo findUserByUserAccount(String account);
}
