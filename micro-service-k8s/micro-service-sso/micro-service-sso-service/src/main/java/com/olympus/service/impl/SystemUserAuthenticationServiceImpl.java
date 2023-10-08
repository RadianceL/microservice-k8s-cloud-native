package com.olympus.service.impl;

import cn.fuxi.common.user.UserBaseInfo;
import com.google.common.collect.Lists;
import com.olympus.base.utils.support.exception.ExtendRuntimeException;
import com.olympus.base.utils.support.utils.Md5Utils;
import com.olympus.data.BaseUserInfoPO;
import com.olympus.data.SysUserInfoDO;
import com.olympus.repository.SystemUserRepository;
import com.olympus.service.SystemUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eddie.lys
 * @since 2023/10/8
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemUserAuthenticationServiceImpl implements SystemUserAuthenticationService {

    /**
     * 用户数据仓库
     */
    private final SystemUserRepository systemUserRepository;

    @Override
    public UserBaseInfo findUserByUserAccount(String account) {
        BaseUserInfoPO userInfo = systemUserRepository.findByAccount(account);
        if (Objects.isNull(userInfo)) {
            throw new ExtendRuntimeException("Cannot find user info by account");
        }
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUsername(userInfo.getAccount());
        userBaseInfo.setPassword(userInfo.getPassword());
        userBaseInfo.setAuthoritiesRoles(Lists.newArrayList("ROLE_CUSTOMER", "ROLE_ADMIN"));
        return userBaseInfo;
    }

    @Override
    public boolean registerUser(SysUserInfoDO sysUserInfo) {
        BaseUserInfoPO baseUserInfo = new BaseUserInfoPO();
        baseUserInfo.setCid(UUID.randomUUID().toString());
        baseUserInfo.setAccount(sysUserInfo.getAccount());
        baseUserInfo.setPassword(sysUserInfo.getPassword());
        baseUserInfo.setNickName(sysUserInfo.getNickName());
        baseUserInfo.setUserCode(Md5Utils.encode(sysUserInfo.getAccount()));
        baseUserInfo.setCreateTime(new Date());
        systemUserRepository.save(baseUserInfo);
        return true;
    }
}
