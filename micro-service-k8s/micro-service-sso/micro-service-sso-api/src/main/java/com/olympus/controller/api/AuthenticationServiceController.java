package com.olympus.controller.api;

import cn.fuxi.common.response.ServiceResponse;
import cn.fuxi.common.user.UserBaseInfo;
import com.olympus.data.SysUserInfoDO;
import com.olympus.logger.event.annotation.EventTrace;
import com.olympus.logger.event.model.LoggerType;
import com.olympus.service.SystemUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 认证服务核心API
 *
 * @author eddie.lys
 * @since 2023/4/20
 */
@Service
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceController {

    /**
     * 用户注册服务
     */
    private final SystemUserAuthenticationService systemUserAuthenticationService;

    /**
     * 用户注册
     */
    @PostMapping("/user/register")
    @EventTrace(event = "用户注册", loggerType = LoggerType.FORMAT)
    public ServiceResponse<String> internalUserInfoQuery(@RequestBody SysUserInfoDO userInfo) {
        boolean registerUserTag = systemUserAuthenticationService.registerUser(userInfo);
        return registerUserTag ? ServiceResponse.ofSuccess("register success") : ServiceResponse.ofError("register failure");
    }
}
