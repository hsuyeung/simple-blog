-- ====================================
-- ！！！注意：只有版本大于等于 v.1.4.0 的才需要执行该 sql！！！
-- 操作步骤：
--   1. 执行下列所有 sql
--   2. 将 redis 中关于权限的缓存（默认 key 是：blog:rbac:userPermission）清空
--   3. 将系统配置缓存（默认 key 是：blog:systemConfig:system）清空
-- =====================================

DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`
(
  `id`              bigint(20) UNSIGNED                                           NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `path`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接口路径',
  `method`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT 'HTTP 方法类型',
  `permission_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '接口描述',
  `is_enabled`      tinyint(4)                                                    NOT NULL DEFAULT 0 COMMENT '是否可用',
  `create_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '创建者',
  `create_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL DEFAULT '' COMMENT '更新人',
  `update_time`     datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted`      tinyint(3) UNSIGNED                                           NOT NULL DEFAULT 0 COMMENT '逻辑删除字段（0: 未删除；1：已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_path_method` (`path`, `method`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '后台接口路径级权限表'
  ROW_FORMAT = DYNAMIC;


INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (1, '/api/user/actions/page', 'POST', '查询用户分页列表', 1, '1', '2022-07-01 09:35:43', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (2, '/api/user/*', 'DELETE', '删除指定的用户', 1, '1', '2022-07-01 22:57:21', '1', '2022-07-04 11:05:36', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (3, '/api/user/actions/lock/*', 'POST', '锁定指定的用户', 1, '1', '2022-07-02 12:37:54', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (4, '/api/user/actions/unlock/*', 'POST', '解锁指定的用户', 1, '1', '2022-07-02 12:38:18', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (5, '/api/user', 'POST', '新增用户', 1, '1', '2022-07-02 17:35:57', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (6, '/api/role/all/enabled', 'GET', '获取所有启用状态的角色', 1, '1', '2022-07-02 19:53:12', '1',
        '2022-07-03 13:02:19', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (7, '/api/user/*/all/enabled/role', 'GET', '获取指定用户拥有的所有启用状态的角色', 1, '1', '2022-07-02 20:19:50',
        '1', '2022-07-03 13:02:22', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (8, '/api/user/*/role', 'PUT', '给指定用户授予指定角色', 1, '1', '2022-07-02 20:53:25', '1',
        '2022-07-04 11:05:39', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (9, '/api/user', 'PUT', '更新用户信息', 1, '1', '2022-07-03 13:03:17', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (10, '/api/role/actions/page', 'POST', '查询角色分页列表', 1, '1', '2022-07-03 15:45:49', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (11, '/api/role/actions/lock/*', 'PUT', '锁定指定的角色', 1, '1', '2022-07-03 15:58:44', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (12, '/api/role/actions/unlock/*', 'PUT', '解锁指定的角色', 1, '1', '2022-07-03 15:58:59', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (13, '/api/role/*', 'DELETE', '删除指定的角色', 1, '1', '2022-07-03 16:50:45', '1', '2022-07-03 16:52:18', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (14, '/api/role', 'POST', '创建角色', 1, '1', '2022-07-03 19:46:07', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (15, '/api/role', 'PUT', '更新角色信息', 1, '1', '2022-07-03 20:22:24', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (16, '/api/role/*/permission', 'PUT', '给指定角色授予指定权限', 1, '1', '2022-07-03 20:35:45', '1',
        '2022-07-03 20:35:47', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (17, '/api/permission/all/enabled', 'GET', '获取所有启用状态的权限', 1, '1', '2022-07-03 20:38:57', '1',
        '2022-07-03 20:38:59', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (18, '/api/role/*/all/enabled/permission', 'GET', '获取指定角色拥有的所有启用状态的权限', 1, '1',
        '2022-07-03 20:44:11', '1', '2022-07-03 20:44:12', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (19, '/api/permission/actions/page', 'POST', '查询权限分页列表', 1, '1', '2022-07-04 10:45:53', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (20, '/api/permission', 'PUT', '更新权限信息', 1, '1', '2022-07-04 14:27:19', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (21, '/api/permission/actions/lock/*', 'PUT', '锁定指定的权限', 1, '1', '2022-07-04 14:33:02', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (22, '/api/permission/actions/unlock/*', 'PUT', '解锁指定的权限', 1, '1', '2022-07-04 14:33:21', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (23, '/api/permission/*', 'DELETE', '删除指定的权限', 1, '1', '2022-07-04 14:54:59', '1', '2022-07-04 14:54:59',
        0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (24, '/api/permission', 'POST', '新增权限信息', 1, '1', '2022-07-04 15:40:02', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (25, '/api/friend/link/actions/page', 'POST', '查询友链分页列表', 1, '1', '2022-07-05 17:49:40', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (26, '/api/friend/link/*', 'DELETE', '删除指一个权限', 1, '1', '2022-07-05 18:01:58', '1', '2022-07-05 18:01:58',
        0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (27, '/api/friend/link', 'POST', '新增友链', 1, '1', '2022-07-05 19:08:40', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (28, '/api/friend/link/actions/refresh/cache', 'PUT', '刷新友链缓存', 1, '1', '2022-07-05 19:25:22', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (29, '/api/friend/link', 'PUT', '更新友链信息', 1, '1', '2022-07-05 19:39:38', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (30, '/api/system/config/actions/page', 'POST', '查询系统配置分页列表', 1, '1', '2022-07-06 10:44:03', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (31, '/api/system/config', 'PUT', '更新一个系统配置', 1, '1', '2022-07-06 15:23:34', '1', '2023-08-26 09:13:58',
        0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (32, '/api/system/config/actions/refresh/cache/*', 'PUT', '刷新一个系统配置的缓存', 1, '1',
        '2022-07-06 16:04:51', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (33, '/api/system/config/actions/refresh/cache', 'PUT', '刷新所有的系统配置缓存', 1, '1', '2022-07-06 16:06:26',
        '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (34, '/api/mail/actions/page', 'POST', '查询邮件分页列表', 1, '1', '2022-07-06 19:26:46', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (35, '/admin/preview/mail/text/*', 'GET', '查看邮件内容', 1, '1', '2022-07-07 09:26:06', '1',
        '2022-07-07 09:26:06', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (36, '/api/article/title/list', 'GET', '获取文章的标题列表', 1, '1', '2022-07-07 10:43:56', '1',
        '2022-07-07 10:43:56', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (37, '/api/comment/actions/page', 'POST', '查询评论分页列表', 1, '1', '2022-07-07 14:21:14', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (38, '/api/comment/*', 'DELETE', '删除指定的评论', 1, '1', '2022-07-07 14:52:35', '1', '2022-07-07 14:52:35', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (39, '/api/file/actions/page', 'POST', '查询文件上传记录分页列表', 1, '1', '2022-07-07 16:14:13', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (40, '/api/article/actions/page', 'POST', '查询文章分页列表', 1, '1', '2022-07-08 09:55:56', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (41, '/api/article/*', 'DELETE', '删除指定的文章', 1, '1', '2022-07-08 10:54:57', '1', '2022-07-08 10:54:57', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (42, '/api/file/actions/upload', 'POST', '文件上传', 1, '1', '2022-07-08 23:20:58', '1', '2022-07-08 23:20:58',
        0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (43, '/api/article', 'POST', '编辑文章', 1, '1', '2022-07-09 00:05:15', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (44, '/api/article/*/md/content', 'GET', '获取指定文章的 markdown 格式的内容', 1, '1', '2022-07-10 13:14:41',
        '1', '2022-07-10 13:14:41', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (45, '/api/article', 'PUT', '新增文章', 1, '1', '2022-07-10 14:47:57', '1', '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (46, '/api/user/actions/logout', 'POST', '用户退出登录', 1, '1', '2022-07-11 19:19:11', '1',
        '2023-08-26 09:13:58', 0);
INSERT INTO t_permission (id, path, method, permission_desc, is_enabled, create_by, create_time, update_by, update_time,
                          is_deleted)
VALUES (47, '/api/user/nickname', 'GET', '获取当前登录的用户昵称', 1, '1', '2022-07-11 19:19:11', '1',
        '2022-07-11 19:19:11', 0);

UPDATE t_system_config
SET is_deleted = 1
WHERE is_deleted = 0
  AND conf_group = 'system'
  AND conf_key IN ('qqNumExchangeKUrl', 'qqAvatarRequestUrl');
