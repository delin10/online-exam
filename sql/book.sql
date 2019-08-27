create database if not exists book;

use book;

-- drop all tables with same name

drop table if exists `t_user`;
drop table if exists `t_role`;
drop table if exists `t_resource`;
drop table if exists `t_role_resource`;
drop table if exists `t_book`;
drop table if exists `t_book_comment`;
drop table if exists `t_order`;

-- the base table for user information
create table `t_user`(
    `id` int unsigned auto_increment not null comment 'id',
    `nick_name` varchar(20) comment '',
    `role` int unsigned not null default 0 comment '2-normal user 1-manager',
    `password_md5` varchar(32) not null comment 'password hash by md5' ,
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    primary key(`id`)
)engine=innodb default character set utf8mb4 auto_increment=2;

-- role table, use to auth
create table `t_role`(
    `id` int unsigned auto_increment not null comment '0-manager 1-normal user other-',
    `name` varchar(20) not null comment '',
    `desc` varchar(50) comment 'description',
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`)
)engine=innodb default character set utf8mb4 auto_increment=3;

-- the base table for user information
create table `t_resource`(
    `id` int unsigned auto_increment not null comment 'id',
    `name` varchar(20) comment '',
    `uri` varchar(50) comment 'the uri to identify resource',
    `del_mark` tinyint not null default '0',
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`),
    unique key (`name`)
)engine=innodb default character set utf8mb4;

-- the mapping between roles and resources
create table `t_role_resource`(
    `role_id` int unsigned not null,
    `resource_id` int unsigned not null,
    `create_time` bigint(20) not null comment 'the time when create user',
    `creator` int unsigned not null,
    primary key (`role_id`,`resource_id`)
)engine=innodb default character set utf8mb4;

-- book base info
create table `t_book`(
    `id` int unsigned auto_increment not null comment 'id',
    `name` varchar(50) not null comment '',
    `category` tinyint not null comment '',
    `price` decimal(15,2) not null comment '',
    `special_price` decimal(15,2) default null comment '',
    `discount` tinyint default 100 comment '',
    `on_sell` tinyint default 0 comment '',
    `cover_url` varchar(50) comment 'cover image url(only one)',
    `desc` varchar(100) comment '',
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    primary key (id)
)engine=innodb default character set utf8mb4;

-- order table
create table`t_order`(
    `id` bigint(20) not null comment '',
    `book_id` int unsigned not null comment '',
    `user_id` int unsigned not null comment '',
    `order_status` tinyint default 0 comment '0-processing 1-refund 2-finish',
    `real_pay` decimal(15,2) not null comment 'real fee which the user has paid',
    `address` varchar(50) not null comment '',
    `amount` int unsigned not null default 1 comment '',
    `expect_arrival_time` bigint(20) comment '',
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    primary key (id)
)engine=innodb default character set utf8mb4;

-- book comment
create table `t_book_comment`(
    `id` int unsigned auto_increment not null comment 'id',
    `user_id` int unsigned not null comment '',
    `content` text comment '',
    `star` tinyint not null default 5 comment '1~5',
    `create_time` bigint(20) not null comment 'the time when create user',
    `last_update_time` bigint(20) not null comment 'the time the record is updated lastly',
    primary key (id)
)engine=innodb default character set utf8mb4;

insert into `t_user` values (0,'system manager',0,'e10adc3949ba59abbe56e057f20f883e',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000);

insert into `t_role` values(1,'manager','',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000,0);
insert into `t_role` values(2,'normal','',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000,0);

