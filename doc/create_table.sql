create database if not exists teaai;

use teaai;

create table if not exists teaai.question
(
    id               bigint auto_increment comment 'id'
    primary key,
    question_content text                               null comment '题目内容（json格式）',
    bankId           bigint                             not null comment '对应题库 id',
    userId           bigint                             not null comment '创建用户 id',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint  default 0                 not null comment '是否删除'
)
    comment '题目';

create table if not exists teaai.question_bank
(
    id               bigint auto_increment comment 'id'
    primary key,
    bank_name        varchar(128)                       not null comment '题库名',
    bank_desc        varchar(2048)                      null comment '题库描述',
    bank_icon        varchar(1024)                      null comment '题库图标',
    bank_type        tinyint  default 0                 not null comment '题库类型（0-得分类，1-测评类）',
    scoring_strategy tinyint  default 0                 not null comment '评分策略（0-自定义，1-AI）',
    review_status    int      default 0                 not null comment '审核状态：0-待审核, 1-通过, 2-拒绝',
    review_message   varchar(512)                       null comment '审核信息',
    reviewerId       bigint                             null comment '审核人 id',
    review_time      datetime                           null comment '审核时间',
    user_Id          bigint                             not null comment '创建用户 id',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint  default 0                 not null comment '是否删除'
    )
    comment '题库表';

create index idx_bankName
    on teaai.question_bank (bank_name);

-- auto-generated definition
create table scoring_result
(
    id                 bigint auto_increment comment 'id'
        primary key,
    result_name        varchar(128)                       not null comment '结果名称，如物流师',
    result_desc        text                               null comment '结果描述',
    result_picture     varchar(1024)                      null comment '结果图片',
    result_prop        varchar(128)                       null comment '结果属性集合 JSON，如 [I,S,T,J]',
    result_score_range int                                null comment '结果得分范围，如 80，表示 80及以上的分数命中此结果',
    bankId             bigint                             not null comment '题库 id',
    userId             bigint                             not null comment '创建用户 id',
    create_time        datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time        datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete          tinyint  default 0                 not null comment '是否删除'
)
    comment '评分结果';

create index idx_bankId
    on scoring_result (bankId);

-- auto-generated definition
create table user
(
    id            bigint auto_increment comment 'id'
        primary key,
    user_account  varchar(256)                                                                                         not null comment '账号',
    user_password varchar(512)                                                                                         not null comment '密码',
    user_name     varchar(256)  default '无昵称'                                                                       null comment '用户昵称',
    user_avatar   varchar(1024) default 'https://2f7171c5.cloudflare-imgbed-bo7.pages.dev/file/1727918660793_头像.png' null comment '用户头像',
    user_profile  varchar(512)                                                                                         null comment '用户简介',
    user_role     varchar(256)  default 'user'                                                                         not null comment '用户角色：user/admin/ban',
    create_time   datetime      default CURRENT_TIMESTAMP                                                              not null comment '创建时间',
    update_time   datetime      default CURRENT_TIMESTAMP                                                              not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint       default 0                                                                              not null comment '是否删除'
)
    comment '用户表';

-- auto-generated definition
create table user_answer
(
    id               bigint auto_increment
        primary key,
    bankId           bigint                             not null comment '题库 id',
    bankType         tinyint  default 0                 not null comment '题库类型（0-得分类，1-角色测评类）',
    scoring_strategy tinyint  default 0                 not null comment '评分策略（0-自定义，1-AI）',
    choices          text                               null comment '用户答案（JSON 数组）',
    resultId         bigint                             null comment '评分结果 id',
    result_name      varchar(128)                       null comment '结果名称，如物流师',
    result_desc      text                               null comment '结果描述',
    result_picture   varchar(1024)                      null comment '结果图标',
    result_score     int                                null comment '得分',
    userId           bigint                             not null comment '用户 id',
    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint  default 0                 not null comment '是否删除'
)
    comment '用户答题记录';

create index idx_bankId
    on user_answer (bankId);

create index idx_userId
    on user_answer (userId);

