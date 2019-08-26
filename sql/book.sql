-- the base table for user information
create table `user`(
    `id` int unsigned auto_increment not null comment 'id',
    `nick_name` varchar(20) comment '',
    `role` int unsigned not null default 0 comment '0-normal user 1-manager',
    `password_md5` varchar(32) not null comment 'password hash by md5' ,
    `create_time` long not null comment 'the time when create user',
    `last_update_time` long not null comment 'the time the record is updated lastly',
    primary key(`id`)
)engine=innodb default character set utf8mb4;

-- role table, use to auth
create table `role`(
    `id` int unsigned auto_increment not null comment '',
    `name` varchar(20) not null comment '',
    `desc` varchar(50) comment 'description',
    `create_time` long not null comment 'the time when create user',
    `last_update_time` long not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`)
)engine=innodb default character set utf8mb4;

-- the base table for user information
create table `resource`(
    `id` int unsigned auto_increment not null comment 'id',
    `name` varchar(20) comment '',
    `uri` varchar(50) comment 'the uri to identify resource',
    `del_mark` tinyint not null default '0',
    `create_time` long not null comment 'the time when create user',
    `last_update_time` long not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`),
    unique key (`name`)
)engine=innodb default character set utf8mb4;

-- the mapping between roles and resources
create table `role_resource`(
    `role_id` int unsigned not null,
    `resource_id` int unsigned not null,
    `create_time` long not null comment 'the time when create user',
    `creator` int unsigned not null,
    primary key (`role_id`,`resource_id`)
)engine=innodb default character set utf8mb4;


create table book(
    `id` int unsigned auto_increment not null comment 'id',
    `name` varchar(50) not null comment '',
    `category` tinyint not null comment '',
    `price` decimal(15,2) not null comment '',
    'special_price' decimal(15,2) default null comment '',
    `on_sell` tinyint default 0 comment ''
)engine=innodb default character set utf8mb4;



