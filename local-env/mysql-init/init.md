## bootstrap.yml - on local
```yaml
spring:
  application:
    name: micro-service-sso
  profiles:
    #指定启动环境
    active: dev
  cloud:
    nacos:
      server-addr: 124.222.147.194:8848
      config:
        # 区分环境：开发环境、测试环境、预发布环境、⽣产环境
        # local-dev的namespace-id
        namespace: 6007135b-5d03-4e83-b338-387e4951b1e6
        # 区分不同应⽤：同⼀个环境内，不同应⽤的配置，通过group来区分。
        group: xw-local-sso
        # 文件格式
        file-extension: yaml
        # 自动刷新配置文件,默认true
        refresh-enabled: true
```

## application.yml - on nacos
```yaml
server:
  port: 8081
spring:
  data:
    redis:
      host: 124.222.147.194
      port: 6379
      password: Qwer7410
  shardingsphere:
    props:
      sql-show: true
    datasource:
      cloud_native_node_0:
        name: cloud_native_node_0
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://124.222.147.194:3306/cloud_native_master?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai
        username: root
        password: root
        validationQuery: SELECT 1 FROM DUAL
      cloud_native_node_1:
        name: cloud_native_node_1
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://124.222.147.194:3306/cloud_native_node_1?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai
        username: root
        password: root
        validationQuery: SELECT 1 FROM DUAL
      cloud_native_node_2:
        name: cloud_native_node_2
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://124.222.147.194:3306/cloud_native_node_2?useUnicode=true&characterEncoding=utf8&tinyInt1isBit=false&useSSL=false&allowMultiQueries=true&serverTimezone=Asia/Shanghai
        username: root
        password: root
        validationQuery: SELECT 1 FROM DUAL
      names: cloud_native_node_0,cloud_native_node_1,cloud_native_node_2
```
## cloud_native_master
```sql
create table sys_user
(
    cid         int                                 not null comment '雪花分片键',
    account     VARCHAR(32)                         not null comment '账号',
    password    VARCHAR(64)                         not null comment '密码',
    user_code   VARCHAR(32)                         not null comment '用户编码',
    nick_name   VARCHAR(32)                         not null comment '用户名',
    create_time TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint sys_user_pk
        primary key (cid),
    constraint sys_user_code_unique
        unique (user_code)
) comment '系统用户表';

alter table sys_user
    add frozen_flag boolean default false null comment '冻结状态' after nick_name;

alter table sys_user
    add frozen_time TIMESTAMP null comment '冻结时间' after frozen_flag;

alter table sys_user
    add frozen_by VARCHAR(64) null comment '冻结操作人(cid)' after frozen_time;

alter table sys_user
    add last_login_time TIMESTAMP null comment '上一次登陆时间' after frozen_by;


create table sys_user_identity
(
    identity_id   INTEGER                             not null comment '用户权限ID',
    user_cid      VARCHAR(64)                         not null comment '用户id',
    identity_code VARCHAR(64)                         not null comment '身份编码',
    is_enable     BOOLEAN   default TRUE              not null comment '是否启用',
    create_time   TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    del_flag      BOOLEAN                             null comment '删除标记'
) comment '用户权限表';

create table system_identity
(
    cid           VARCHAR(64)                         not null comment '用户身份ID',
    identity_code VARCHAR(32)                         not null comment '身份编码',
    identity_name VARCHAR(64)                         not null comment '身份描述',
    is_enable     Boolean   default 0                 not null comment '是否启用',
    create_time   TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint user_identity_pk
        primary key (cid)
) comment '用户身份表';

create table system_identity_role
(
    role_id     int auto_increment,
    role_code   VARCHAR(32)                         not null comment 'security权限编码',
    role_name   VARCHAR(64)                         not null comment '权限名称',
    remark      VARCHAR(32)                         null comment '备注',
    is_enable   Boolean   default TRUE              not null comment '是否启用',
    create_time TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint user_identity_role_pk
        primary key (role_id)
) comment '系统角色权限表';
```
## cloud_native_node_1
```sql
create table sys_user
(
    cid         int                                 not null comment '雪花分片键',
    account     VARCHAR(32)                         not null comment '账号',
    password    VARCHAR(64)                         not null comment '密码',
    user_code   VARCHAR(32)                         not null comment '用户编码',
    nick_name   VARCHAR(32)                         not null comment '用户名',
    create_time TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint sys_user_pk
        primary key (cid),
    constraint sys_user_code_unique
        unique (user_code)
) comment '系统用户表';

create table sys_user_identity
(
    identity_id   INTEGER                             not null comment '用户权限ID',
    user_cid      VARCHAR(64)                         not null comment '用户id',
    identity_code VARCHAR(64)                         not null comment '身份编码',
    is_enable     BOOLEAN   default TRUE              not null comment '是否启用',
    create_time   TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    del_flag      BOOLEAN                             null comment '删除标记'
) comment '用户权限表';
```
## cloud_native_node_2
```sql
create table sys_user
(
    cid         int                                 not null comment '雪花分片键',
    account     VARCHAR(32)                         not null comment '账号',
    password    VARCHAR(64)                         not null comment '密码',
    user_code   VARCHAR(32)                         not null comment '用户编码',
    nick_name   VARCHAR(32)                         not null comment '用户名',
    create_time TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    constraint sys_user_pk
        primary key (cid),
    constraint sys_user_code_unique
        unique (user_code)
) comment '系统用户表';

create table sys_user_identity
(
    identity_id   INTEGER                             not null comment '用户权限ID',
    user_cid      VARCHAR(64)                         not null comment '用户id',
    identity_code VARCHAR(64)                         not null comment '身份编码',
    is_enable     BOOLEAN   default TRUE              not null comment '是否启用',
    create_time   TIMESTAMP default CURRENT_TIMESTAMP not null comment '创建时间',
    del_flag      BOOLEAN                             null comment '删除标记'
) comment '用户权限表';

```
## NACOS
```sql
/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/******************************************/
/*   表名称 = config_info                  */
/******************************************/
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) DEFAULT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text COMMENT '配置的模式',
  `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

/******************************************/
/*   表名称 = config_info_aggr             */
/******************************************/
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) NOT NULL COMMENT 'datum_id',
  `content` longtext NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';


/******************************************/
/*   表名称 = config_info_beta             */
/******************************************/
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

/******************************************/
/*   表名称 = config_info_tag              */
/******************************************/
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

/******************************************/
/*   表名称 = config_tags_relation         */
/******************************************/
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

/******************************************/
/*   表名称 = group_capacity               */
/******************************************/
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

/******************************************/
/*   表名称 = his_config_info              */
/******************************************/
CREATE TABLE `his_config_info` (
  `id` bigint(20) unsigned NOT NULL COMMENT 'id',
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COMMENT 'source user',
  `src_ip` varchar(50) DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) DEFAULT '' COMMENT '租户字段',
   `encrypted_data_key` varchar(1024) NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';


/******************************************/
/*   表名称 = tenant_capacity              */
/******************************************/
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';


CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) default '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) default '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

CREATE TABLE `users` (
	`username` varchar(50) NOT NULL PRIMARY KEY COMMENT 'username',
	`password` varchar(500) NOT NULL COMMENT 'password',
	`enabled` boolean NOT NULL COMMENT 'enabled'
);

CREATE TABLE `roles` (
	`username` varchar(50) NOT NULL COMMENT 'username',
	`role` varchar(50) NOT NULL COMMENT 'role',
	UNIQUE INDEX `idx_user_role` (`username` ASC, `role` ASC) USING BTREE
);

CREATE TABLE `permissions` (
    `role` varchar(50) NOT NULL COMMENT 'role',
    `resource` varchar(128) NOT NULL COMMENT 'resource',
    `action` varchar(8) NOT NULL COMMENT 'action',
    UNIQUE INDEX `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
);
```
## SEATA
```sql
--
-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

-- -------------------------------- The script used when storeMode is 'db' --------------------------------
-- the table to store GlobalSession data
CREATE TABLE IF NOT EXISTS `global_table`
(
    `xid`                       VARCHAR(128) NOT NULL,
    `transaction_id`            BIGINT,
    `status`                    TINYINT      NOT NULL,
    `application_id`            VARCHAR(32),
    `transaction_service_group` VARCHAR(32),
    `transaction_name`          VARCHAR(128),
    `timeout`                   INT,
    `begin_time`                BIGINT,
    `application_data`          VARCHAR(2000),
    `gmt_create`                DATETIME,
    `gmt_modified`              DATETIME,
    PRIMARY KEY (`xid`),
    KEY `idx_status_gmt_modified` (`status` , `gmt_modified`),
    KEY `idx_transaction_id` (`transaction_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- the table to store BranchSession data
CREATE TABLE IF NOT EXISTS `branch_table`
(
    `branch_id`         BIGINT       NOT NULL,
    `xid`               VARCHAR(128) NOT NULL,
    `transaction_id`    BIGINT,
    `resource_group_id` VARCHAR(32),
    `resource_id`       VARCHAR(256),
    `branch_type`       VARCHAR(8),
    `status`            TINYINT,
    `client_id`         VARCHAR(64),
    `application_data`  VARCHAR(2000),
    `gmt_create`        DATETIME(6),
    `gmt_modified`      DATETIME(6),
    PRIMARY KEY (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- the table to store lock data
CREATE TABLE IF NOT EXISTS `lock_table`
(
    `row_key`        VARCHAR(128) NOT NULL,
    `xid`            VARCHAR(128),
    `transaction_id` BIGINT,
    `branch_id`      BIGINT       NOT NULL,
    `resource_id`    VARCHAR(256),
    `table_name`     VARCHAR(32),
    `pk`             VARCHAR(36),
    `status`         TINYINT      NOT NULL DEFAULT '0' COMMENT '0:locked ,1:rollbacking',
    `gmt_create`     DATETIME,
    `gmt_modified`   DATETIME,
    PRIMARY KEY (`row_key`),
    KEY `idx_status` (`status`),
    KEY `idx_branch_id` (`branch_id`),
    KEY `idx_xid` (`xid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `distributed_lock`
(
    `lock_key`       CHAR(20) NOT NULL,
    `lock_value`     VARCHAR(20) NOT NULL,
    `expire`         BIGINT,
    primary key (`lock_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);


CREATE TABLE IF NOT EXISTS `vgroup_table`
(
    `vGroup`    VARCHAR(255),
    `namespace` VARCHAR(255),
    `cluster`   VARCHAR(255),
  UNIQUE KEY `idx_vgroup_namespace_cluster` (`vGroup`,`namespace`,`cluster`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
```