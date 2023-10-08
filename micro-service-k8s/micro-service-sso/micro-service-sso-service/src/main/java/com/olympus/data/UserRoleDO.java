package com.olympus.data;

import lombok.Data;

import java.util.List;

/**
 * 用户角色
 *
 * @author eddie.lys
 * @since 2023/10/8
 */
@Data
public class UserRoleDO {
    /**
     * 身份编码
     */
    private String identityCode;
    /**
     * 识别规则
     */
    private List<String> authoritiesRoles;
}
