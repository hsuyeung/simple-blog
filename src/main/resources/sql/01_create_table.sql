/*
 Navicat Premium Data Transfer

 Source Server         : blog@121.43.168.195
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 121.43.168.195:3306
 Source Schema         : blog

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 26/07/2022 19:01:09
*/

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `title`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '文章标题',
  `route`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章路由',
  `author`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '文章作者',
  `keywords`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章关键词，多个用逗号分隔',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文章描述',
  `content_id`  bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '内容 id',
  `is_pin`      tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶（0: 不置顶，1: 置顶）',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_route`(`route`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '博客文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`
(
  `id`              bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `nickname`        varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '评论者昵称',
  `avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论者头像',
  `email`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '评论者邮箱',
  `website`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '评论者网站地址',
  `content`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `parent_id`       bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论所属的一级评论 id，为 0 表示当前评论为一级评论',
  `reply_id`        bigint(20) NOT NULL DEFAULT 0 COMMENT '当前评论回复的其他评论 id，为 0 或者是等于 parent_id 表示当前评论为一级评论',
  `article_id`      bigint(20) UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论的文章 id',
  `ip`              varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'ip 地址',
  `is_notification` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否通过邮件接收回复提醒',
  `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`      tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX             `idx_article_id`(`article_id`) USING BTREE COMMENT '文章 id'
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章评论表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_content
-- ----------------------------
DROP TABLE IF EXISTS `t_content`;
CREATE TABLE `t_content`
(
  `id`           bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `md_content`   longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'markdown 格式文章内容',
  `html_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'html 格式文章内容',
  `create_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time`  datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`   tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章内容表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `url`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件访问全路径',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_friend_link
-- ----------------------------
DROP TABLE IF EXISTS `t_friend_link`;
CREATE TABLE `t_friend_link`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `link_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链名称',
  `link_url`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链链接',
  `link_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链头像',
  `link_desc`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '一句话描述',
  `link_group`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '友链分组',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友链表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_mail
-- ----------------------------
DROP TABLE IF EXISTS `t_mail`;
CREATE TABLE `t_mail`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `m_from`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '发件人',
  `m_to`        varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '收件人，多个用逗号隔开',
  `m_subject`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '主题',
  `m_text`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `m_cc`        varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '抄送，多个用逗号隔开',
  `m_bcc`       varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密送，多个用逗号隔开',
  `m_status`    tinyint(4) NOT NULL DEFAULT 0 COMMENT '发送状态',
  `m_err_msg`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '错误信息',
  `m_type`      tinyint(4) NOT NULL DEFAULT 0 COMMENT '邮件类型',
  `send_time`   datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`
(
  `id`              bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `path`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接口路径',
  `method`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'HTTP 方法类型',
  `permission_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接口描述',
  `is_enabled`      tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否可用',
  `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`      tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_path_method`(`path`, `method`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台接口路径级权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `role_code`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '角色编码',
  `role_desc`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色描述',
  `is_enabled`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否可用',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `rid`         bigint(20) NOT NULL DEFAULT 0 COMMENT '角色 id',
  `pid`         bigint(20) NOT NULL DEFAULT 0 COMMENT '权限 id',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色-权限关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_system_config
-- ----------------------------
DROP TABLE IF EXISTS `t_system_config`;
CREATE TABLE `t_system_config`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `conf_key`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '配置 key',
  `conf_value`  varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置 value',
  `conf_group`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '配置分组',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '配置描述',
  `is_enabled`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否有效（0:无效；1:有效）',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_key_group`(`conf_key`, `conf_group`) USING BTREE COMMENT '同一分组下的 key 惟一'
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `username`    varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '用户名',
  `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录密码',
  `nickname`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '昵称',
  `is_enabled`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否可用',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
  `id`          bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `uid`         bigint(20) NOT NULL DEFAULT 0 COMMENT '用户 id',
  `rid`         bigint(20) NOT NULL DEFAULT 0 COMMENT '角色 id',
  `create_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`   varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`  tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色关系表' ROW_FORMAT = DYNAMIC;

SET
FOREIGN_KEY_CHECKS = 1;

ALTER TABLE `blog`.`t_system_config`
  MODIFY COLUMN `conf_group` varchar (255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '配置分组' AFTER `conf_value`;
