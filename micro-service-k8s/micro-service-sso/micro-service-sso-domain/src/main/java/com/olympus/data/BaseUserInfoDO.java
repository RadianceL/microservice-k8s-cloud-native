package com.olympus.data;

import lombok.Data;

import java.util.Date;

/**
 * 基础用户信息
 *
 * @author eddie.lys
 * @since 2023/10/8
 */
@Data
public class BaseUserInfoDO {

    private String cid;

    private String account;

    private String password;

    private String userCode;

    private String nickName;

    private Date createTime;
}
