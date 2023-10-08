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
);

```