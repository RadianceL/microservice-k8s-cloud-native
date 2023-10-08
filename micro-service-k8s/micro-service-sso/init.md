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