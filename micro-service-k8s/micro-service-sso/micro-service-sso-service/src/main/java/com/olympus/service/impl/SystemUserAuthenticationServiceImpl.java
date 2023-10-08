package com.olympus.service.impl;

import cn.fuxi.common.user.UserBaseInfo;
import com.google.common.collect.Lists;
import com.olympus.data.BaseUserInfoPO;
import com.olympus.repository.SystemUserRepository;
import com.olympus.service.SystemUserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @author eddie.lys
 * @since 2023/10/8
 */

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemUserAuthenticationServiceImpl implements SystemUserAuthenticationService {

    private final SystemUserRepository systemUserRepository;

    @Override
    public UserBaseInfo findUserByUserAccount(String account) {
        BaseUserInfoPO userInfo = systemUserRepository.findByAccount(account);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        userBaseInfo.setUsername(userInfo.getAccount());
        userBaseInfo.setPassword(userInfo.getPassword());

        userBaseInfo.setAuthoritiesRoles(Lists.newArrayList("ROLE_CUSTOMER", "ROLE_ADMIN"));
        return userBaseInfo;
    }


}
