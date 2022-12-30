-- 增加邮件发送失败最大重试次数系统配置
INSERT INTO `t_system_config` (`conf_key`, `conf_value`, `conf_group`, `description`, `is_enabled`,
                               `create_by`, `create_time`, `update_by`, `update_time`, `is_deleted`)
VALUES ('mailRetryMaxNum', '3', 'system', '发送失败的邮件最大重试次数', 1, '1', '2022-10-31 19:26:34', '1',
        '2022-10-31 19:26:34', 0);
-- 新增邮件失败重试次数字段
ALTER TABLE `t_mail`
  ADD COLUMN `retry_num` tinyint NOT NULL DEFAULT 0 COMMENT '失败重试次数' AFTER `send_time`;
