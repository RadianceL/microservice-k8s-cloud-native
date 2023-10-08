package com.olympus.data;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

/**
 * 基础用户信息
 *
 * @author eddie.lys
 * @since 2023/10/8
 */
@Data
@Entity
@Table(name = "sys_user")
public class BaseUserInfoPO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cid")
    private String cid;

    @Column(name = "account", nullable = false)
    private String account;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "user_code", nullable = false, unique = true)
    private String userCode;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;

}
