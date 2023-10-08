package com.olympus.controller.provider;

import cn.fuxi.common.response.ServiceResponse;
import cn.fuxi.common.user.UserBaseInfo;
import com.olympus.data.SysUserInfoDO;
import com.olympus.logger.event.annotation.EventTrace;
import com.olympus.logger.event.model.LoggerType;
import com.olympus.service.SystemUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务核心API
 *
 * @author eddie.lys
 * @since 2023/4/20
 */
@Slf4j
@RestController
@RequestMapping("/provider")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceProvider {

    private final SystemUserAuthenticationService systemUserAuthenticationService;

    /**
     * 内部接口 - 用户信息查询"
     */
    @PostMapping("/internal/user-info/query")
    @EventTrace(event = "内部接口 - 用户信息查询", loggerType = LoggerType.FORMAT)
    public ServiceResponse<UserBaseInfo> internalUserInfoQuery(@RequestParam("username") String username) {
        UserBaseInfo userByUserAccount = systemUserAuthenticationService.findUserByUserAccount(username);
        return ServiceResponse.ofSuccess(userByUserAccount);
    }

    /**
     * 内部用户注册 - 用于注册后直接登陆
     */
    @PostMapping("/internal/user/register")
    @EventTrace(event = "用户注册", loggerType = LoggerType.FORMAT)
    public ServiceResponse<String> internalUserInfoQuery(@RequestBody SysUserInfoDO userInfo) {
        boolean registerUserTag = systemUserAuthenticationService.registerUser(userInfo);
        return registerUserTag ? ServiceResponse.ofSuccess("注册成功") : ServiceResponse.ofError("注册失败");
    }
}
