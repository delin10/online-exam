create database if not exists onlineexamdb;

use onlineexamdb;

-- drop all tables with same name

drop table if exists `t_user`;
drop table if exists `t_role`;
drop table if exists `t_resource`;
drop table if exists `t_role_resource`;
drop table if exists `t_question`;
drop table if exists `t_course`;
drop table if exists `t_test_paper`;
drop table if exists `t_joined_course`;
drop table if exists `t_committed_answer`;
drop table if exists `t_test_paper_content`;


-- the base table for user information
create table `t_user`(
    `id` int unsigned auto_increment not null comment 'id',
    `nick_name` varchar(20) comment '',
    `role` int unsigned not null default 0 comment '2-normal user 1-manager',
    `password_md5` varchar(32) not null comment 'password hash by md5' ,
    `create_time` bigint(20) unsigned not null comment 'the time when create user',
    `update_time` bigint(20) unsigned not null comment 'the time the record is updated lastly',
    primary key(`id`)
)engine=innodb default character set utf8mb4 auto_increment=2;

-- role table, use to auth
create table `t_role`(
    `id` int unsigned auto_increment not null comment '0-manager 1-normal user other-',
    `name` varchar(20) not null comment '',
    `desc` varchar(50) comment 'description',
    `create_time` bigint(20) unsigned not null comment 'the time when create user',
    `update_time` bigint(20) unsigned not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`)
)engine=innodb default character set utf8mb4 auto_increment=3;

-- the base table for user information
create table `t_resource`(
    `id` int unsigned auto_increment not null comment 'id',
    `name` varchar(20) comment '',
    `uri` varchar(50) comment 'the uri to identify permission',
    `del_mark` tinyint not null default '0',
    `create_time` bigint(20) unsigned not null comment 'the time when create user',
    `update_time` bigint(20) unsigned not null comment 'the time the record is updated lastly',
    `creator` int unsigned not null,
    primary key(`id`),
    unique key (`name`)
)engine=innodb default character set utf8mb4;

-- the mapping between roles and resources
create table `t_role_resource`(
    `id` int unsigned auto_increment not null,
    `role_id` int unsigned not null,
    `resource_id` int unsigned not null,
    `create_time` bigint unsigned not null comment 'the time when create user',
    `creator` int unsigned not null,
    primary key (id),
    key (role_id),
    key (resource_id),
    unique key (role_id, resource_id)
)engine=innodb default character set utf8mb4;

-- 题库表
create table `t_question`(
    `id` int unsigned auto_increment not null comment '题目id',
    `type` tinyint not null comment '题目类型：0-选择题 1-填空题 2-主观题',
    `content` varchar(300) not null comment '题目内容',
    `options` varchar(200) default null comment '如果是选择题，各个选项的内容用逗号分开',
    `answer` varchar(300) default null comment '问题的答案，如果是选择题，则保存的是题目选项的下标',
    `creator` int unsigned not null comment '创建题目的人的id',
    `updater` int unsigned not null comment '更新题目内容或者修正题目',
    `create_time` bigint unsigned not null comment '创建时间',
    `update_time` bigint unsigned not null comment '更新时间',
    primary key (id),
    key (creator),
    key (updater)
)engine=innodb default character set utf8mb4;

-- 课程表
create table `t_course`(
    `id` int unsigned auto_increment not null comment '课程id',
    `name` varchar(50) not null comment '课程名称',
    `teacher` int unsigned not null comment '教师id',
    `create_time` bigint unsigned not null comment '创建时间',
    `update_time` bigint unsigned not null comment '更新时间',
    `start_time` bigint unsigned not null comment '课程开始时间',
    `end_time` bigint unsigned not null comment '课程结束时间',
    `status` tinyint not null comment '课程状态：0-未开始 1-进行中 2-考试中 3-发布成绩 4-结课',
    primary key(id),
    key (teacher)
)engine=innodb default character set utf8mb4;

-- 试卷表
create table `t_test_paper`(
   `id` int unsigned auto_increment not null comment '记录id',
   `cid`int unsigned not null comment '课程id',
   `name` varchar(50) not null comment '试卷标题',
   `start_time` bigint unsigned not null comment '考试开始时间',
   `end_time` bigint unsigned not null comment  '考试结束时间',
   `test_duration` int unsigned not null comment '考试时长,单位s',
   `score` mediumint not null comment '卷面总分',
   `creator` int unsigned not null comment '创建试卷的人的id',
   `create_time` bigint unsigned not null comment '创建试卷的时间',
   `published` tinyint not null comment '是否发布通知,0-未发布，1-已发布',
   primary key (id),
   key (creator)
)engine=innodb default character set utf8mb4;

-- 参加课程的表
create table `t_joined_course`(
    `id` int unsigned auto_increment not null comment '起标志作用',
    `uid` int unsigned not null comment '参与课程的用户id',
    `cid` int unsigned not null comment '课程id',
    `score` mediumint default -1 comment '课程分数，不一定为试卷分数，初始化为-1',
    `join_test_time` bigint unsigned default null comment '参加考试的时间',
    `create_time` bigint unsigned not null comment '记录创建时间',
    `update_time` bigint unsigned not null comment '记录更新时间',
    primary key(id),
    key (uid),
    key (cid)
)engine=innodb default character set utf8mb4;

alter table `t_joined_course` drop column `join_test_time`;
alter table `t_joined_course` add unique key t_joined_course_unique_key_uid_cid(`uid`,`cid`);

-- 参加考试的表
create table `t_joined_test`(
    `id` int unsigned auto_increment not null comment '起标志作用',
    `uid` int unsigned not null comment '参与考试的用户id',
    `tid` int unsigned not null comment '试卷id',
    `score` mediumint default -1 comment '分数，初始化为-1',
    `create_time` bigint unsigned not null comment '记录创建时间',
    `update_time` bigint unsigned not null comment '记录更新时间',
    primary key(id),
    key (uid),
    key (tid)
)engine=innodb default character set utf8mb4;

alter table `t_joined_test` add unique index t_joined_test_unique_index_uid_tid(`uid`, `tid`);

-- 提交答案的表
create table `t_submitted_answer`(
    `id` int unsigned auto_increment not null comment '记录id',
    `uid` int unsigned not null comment '用户id',
    `pid` int unsigned not null comment '试卷id',
    `qid` int unsigned not null comment '问题id',
    `answer` varchar(300) not null comment '提交的答案',
    `score` mediumint default -1 not null comment '分数，-1表示人工未审核',
    `create_time` bigint unsigned not null comment '记录创建时间',
    primary key (id),
    key (uid),
    key (pid),
    key (qid)
)engine=innodb default character set utf8mb4;

create table `t_test_paper_content_item`(
    `id` int unsigned auto_increment not null comment '记录id',
    `pid` int unsigned not null comment '试卷id',
    `qid` int unsigned not null comment '问题id',
    `first_seq` mediumint unsigned not null comment '第一序号',
    `sec_seq` mediumint unsigned not null comment '第二级序号',
    `score` mediumint not null comment '题目分值，所有题目分值加起来刚好等于卷面总分值',
    `create_time` bigint unsigned not null comment '创建时间',
    `update_time` bigint unsigned not null comment '更新时间',
    primary key (id),
    key (pid),
    key (qid)
)engine=innodb default character set utf8mb4;



insert into `t_user` values (0,'system manager',0,'e10adc3949ba59abbe56e057f20f883e',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000);

insert into `t_role` values(1,'manager','',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000,0);
insert into `t_role` values(2,'normal','',unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000,0);

insert into `t_resource` values (NULL, 'test', '/api/test/{id}', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'listQuestion', '/exam/question/list', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'addTestPaper', '/exam/testPaper/add', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'listTests', '/exam/testPaper/list', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'joinCourse', '/exam/course/join/{cid}', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'listAllResource', '/exam/resource/list/all', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'authRoleResource', '/exam/role/auth', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);
insert into `t_resource` values (NULL, 'addResource', '/exam/resource/add', 0,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000, 0);

insert into `t_joined_course` values (NULL, 2, 10 , -1, 1,unix_timestamp(current_timestamp(3))*1000,unix_timestamp(current_timestamp(3))*1000);

insert into `t_role_resource` values (NULL, 2, 1, unix_timestamp(current_timestamp(3))*1000, 0);

/*

-- left join 要用别名
select `trs`.*
from `t_role` tr
left join `t_role_resource` trr on `tr`.`id` = `trr`.`role_id`
left join `t_resource` trs on `trs`.`id` = `trr`.`resource_id`
where `tr`.`id` = 2;
*/

select 1 from (select count(*) as c from `t_resource` where id in (1,2)) t where t.c == 2 ;