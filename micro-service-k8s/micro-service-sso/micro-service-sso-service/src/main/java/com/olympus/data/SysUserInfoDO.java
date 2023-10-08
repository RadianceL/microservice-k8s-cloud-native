package com.olympus.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户对象
 *
 * @author eddie.lys
 * @since 2023/10/8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserInfoDO extends BaseUserInfoDO {

    /**
     * 用户角色
     */
    private List<UserRoleDO> userRole;
}
