### bootstrap.yml - on local
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

### application.yml - on nacos
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
```sql
## cloud_native_master
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

## cloud_native_node_1
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


## cloud_native_node_2
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