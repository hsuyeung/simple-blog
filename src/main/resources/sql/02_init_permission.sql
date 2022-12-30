INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/page', 'GET', '查询用户分页列表', 1, '1', '2022-07-01 09:35:43', '1', '2022-07-04 15:20:32', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/*', 'DELETE', '删除指定的用户', 1, '1', '2022-07-01 22:57:21', '1', '2022-07-04 11:05:36', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/lock/*', 'POST', '锁定指定的用户', 1, '1', '2022-07-02 12:37:54', '1', '2022-07-04 11:05:37', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/unlock/*', 'POST', '解锁指定的用户', 1, '1', '2022-07-02 12:38:18', '1', '2022-07-03 13:02:14', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user', 'POST', '更新用户信息', 1, '1', '2022-07-02 17:35:57', '1', '2022-07-03 13:02:17', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/all/enabled', 'GET', '获取所有启用状态的角色', 1, '1', '2022-07-02 19:53:12', '1', '2022-07-03 13:02:19', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/*/all/enabled/role', 'GET', '获取指定用户拥有的所有启用状态的角色', 1, '1', '2022-07-02 20:19:50', '1',
        '2022-07-03 13:02:22', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/*/role', 'PUT', '给指定用户授予指定角色', 1, '1', '2022-07-02 20:53:25', '1', '2022-07-04 11:05:39', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user', 'PUT', '创建一个新的用户', 1, '1', '2022-07-03 13:03:17', '1', '2022-07-03 20:22:28', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/page', 'GET', '查询角色分页列表', 1, '1', '2022-07-03 15:45:49', '1', '2022-07-04 10:46:02', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/lock/*', 'POST', '锁定指定的角色', 1, '1', '2022-07-03 15:58:44', '1', '2022-07-03 15:58:44', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/unlock/*', 'POST', '解锁指定的角色', 1, '1', '2022-07-03 15:58:59', '1', '2022-07-03 15:58:59', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/*', 'DELETE', '删除指定的角色', 1, '1', '2022-07-03 16:50:45', '1', '2022-07-03 16:52:18', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role', 'POST', '更新角色信息', 1, '1', '2022-07-03 19:46:07', '1', '2022-07-03 19:46:07', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role', 'PUT', '创建一个新的角色', 1, '1', '2022-07-03 20:22:24', '1', '2022-07-03 20:27:47', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/*/permission', 'PUT', '给指定角色授予指定权限', 1, '1', '2022-07-03 20:35:45', '1', '2022-07-03 20:35:47', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission/all/enabled', 'GET', '获取所有启用状态的权限', 1, '1', '2022-07-03 20:38:57', '1', '2022-07-03 20:38:59',
        0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/role/*/all/enabled/permission', 'GET', '获取指定角色拥有的所有启用状态的权限', 1, '1', '2022-07-03 20:44:11', '1',
        '2022-07-03 20:44:12', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission/page', 'GET', '查询权限分页列表', 1, '1', '2022-07-04 10:45:53', '1', '2022-07-04 10:45:53', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission', 'PUT', '创建一个新的权限', 1, '1', '2022-07-04 14:27:19', '1', '2022-07-04 14:27:19', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission/lock/*', 'POST', '锁定指定的权限', 1, '1', '2022-07-04 14:33:02', '1', '2022-07-04 14:33:02', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission/unlock/*', 'POST', '解锁指定的权限', 1, '1', '2022-07-04 14:33:21', '1', '2022-07-04 14:33:21', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission/*', 'DELETE', '删除指定的权限', 1, '1', '2022-07-04 14:54:59', '1', '2022-07-04 14:54:59', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/permission', 'POST', '更新权限信息', 1, '1', '2022-07-04 15:40:02', '1', '2022-07-04 15:40:02', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/friend/link/page', 'GET', '查询友链分页列表', 1, '1', '2022-07-05 17:49:40', '1', '2022-07-05 17:49:40', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/friend/link/*', 'DELETE', '删除指一个权限', 1, '1', '2022-07-05 18:01:58', '1', '2022-07-05 18:01:58', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/friend/link', 'POST', '更新指定的友链', 1, '1', '2022-07-05 19:08:40', '1', '2022-07-05 19:08:40', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/friend/link/refresh/cache', 'POST', '刷新友链缓存', 1, '1', '2022-07-05 19:25:22', '1', '2022-07-05 19:25:22',
        0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/friend/link', 'PUT', '新增一个友链', 1, '1', '2022-07-05 19:39:38', '1', '2022-07-05 19:39:38', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/system/config/page', 'GET', '查询系统配置分页列表', 1, '1', '2022-07-06 10:44:03', '1', '2022-07-06 10:44:03', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/system/config', 'POST', '更新一个系统配置', 1, '1', '2022-07-06 15:23:34', '1', '2022-07-06 16:24:33', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/system/config/refresh/cache/*', 'POST', '刷新一个系统配置的缓存', 1, '1', '2022-07-06 16:04:51', '1',
        '2022-07-06 16:04:51', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/system/config/refresh/cache', 'POST', '刷新所有的系统配置缓存', 1, '1', '2022-07-06 16:06:26', '1',
        '2022-07-06 16:06:26', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/mail/page', 'GET', '查询邮件分页列表', 1, '1', '2022-07-06 19:26:46', '1', '2022-07-06 19:26:46', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/admin/preview/mail/text/*', 'GET', '查看邮件内容', 1, '1', '2022-07-07 09:26:06', '1', '2022-07-07 09:26:06', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article/title/list', 'GET', '获取文章的标题列表', 1, '1', '2022-07-07 10:43:56', '1', '2022-07-07 10:43:56', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/comment/page', 'GET', '查询评论分页列表', 1, '1', '2022-07-07 14:21:14', '1', '2022-07-07 14:21:14', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/comment/*', 'DELETE', '删除指定的评论', 1, '1', '2022-07-07 14:52:35', '1', '2022-07-07 14:52:35', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/file/page', 'GET', '查询文件上传记录分页列表', 1, '1', '2022-07-07 16:14:13', '1', '2022-07-07 16:14:13', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article/page', 'GET', '查询文章分页列表', 1, '1', '2022-07-08 09:55:56', '1', '2022-07-08 09:55:56', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article/*', 'DELETE', '删除指定的文章', 1, '1', '2022-07-08 10:54:57', '1', '2022-07-08 10:54:57', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/file/upload', 'POST', '文件上传', 1, '1', '2022-07-08 23:20:58', '1', '2022-07-08 23:20:58', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article', 'PUT', '新增一篇文章', 1, '1', '2022-07-09 00:05:15', '1', '2022-07-09 00:05:15', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article/*/md/content', 'GET', '获取指定文章的 markdown 格式的内容', 1, '1', '2022-07-10 13:14:41', '1',
        '2022-07-10 13:14:41', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/article', 'POST', '更新文章', 1, '1', '2022-07-10 14:47:57', '1', '2022-07-10 14:47:57', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/logout', 'POST', '用户退出登录', 1, '1', '2022-07-11 19:19:11', '1', '2022-07-11 19:19:11', 0);
INSERT INTO `blog`.`t_permission` (`path`, `method`, `permission_desc`, `is_enabled`, `create_by`, `create_time`,
                                   `update_by`, `update_time`, `is_deleted`)
VALUES ('/api/user/nickname', 'GET', '获取当前登录的用户昵称', 1, '1', '2022-07-11 19:19:11', '1', '2022-07-11 19:19:11', 0);
