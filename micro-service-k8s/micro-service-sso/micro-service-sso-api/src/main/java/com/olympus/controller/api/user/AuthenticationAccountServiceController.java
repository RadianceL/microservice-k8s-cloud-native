package com.olympus.controller.api.user;

import cn.fuxi.common.response.ServiceResponse;
import com.olympus.data.SysUserInfoDO;
import com.olympus.logger.event.annotation.EventTrace;
import com.olympus.logger.event.model.LoggerType;
import com.olympus.service.SystemUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * 用户认证服务核心API
 *
 * @author eddie.lys
 * @since 2023/4/20
 */
@RestController
@RequestMapping("/api/user/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationAccountServiceController {

    /**
     * 用户注册服务
     */
    private final SystemUserAuthenticationService systemUserAuthenticationService;

    @PostMapping("/register")
    @EventTrace(event = "用户注册", loggerType = LoggerType.FORMAT)
    public ServiceResponse<String> userRegister(@RequestBody SysUserInfoDO userInfo) {
        boolean registerUserTag = systemUserAuthenticationService.registerUser(userInfo);
        return registerUserTag ? ServiceResponse.ofSuccess("register success") : ServiceResponse.ofError("register failure");
    }


    @PostMapping("/frozen/self")
    @EventTrace(event = "用户冻结", loggerType = LoggerType.FORMAT)
    public ServiceResponse<String> userFrozen(@RequestBody SysUserInfoDO userInfo) {
        boolean registerUserTag = systemUserAuthenticationService.frozenUser(userInfo);
        return registerUserTag ? ServiceResponse.ofSuccess("frozen success") : ServiceResponse.ofError("register failure");
    }
}
