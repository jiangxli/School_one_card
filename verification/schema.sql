SET FOREIGN_KEY_CHECKS=0;
CREATE TABLE `book_borrow_record` (
  `id` bigint(16) NOT NULL COMMENT '记录编号',
  `book_id` bigint(16) DEFAULT NULL COMMENT '书籍',
  `user_id` bigint(16) DEFAULT NULL COMMENT '用户',
  `borrow_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '借书时间',
  `return_time` datetime DEFAULT NULL COMMENT '还书时间',
  `status` int(1) DEFAULT '0' COMMENT '状态0未还1已还',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='借阅记录';
CREATE TABLE `book_info` (
  `book_id` bigint(16) NOT NULL COMMENT '图书编号',
  `book_name` varchar(255) DEFAULT NULL COMMENT '图书名称',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `publisher` varchar(255) DEFAULT NULL COMMENT '出版社',
  `publish_date` date DEFAULT NULL COMMENT '出版时间',
  `kind` varchar(255) DEFAULT NULL COMMENT '图书类型',
  `book_num` int(11) DEFAULT NULL COMMENT '图书数量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='图书管理';
CREATE TABLE `campus_api_credential` (
  `merchant_id` bigint(20) NOT NULL,
  `public_id` varchar(36) NOT NULL,
  `secret` varchar(64) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '0',
  `allow_recharge` tinyint(4) NOT NULL DEFAULT '0',
  `allow_pay` tinyint(4) NOT NULL DEFAULT '1',
  `callback_url` varchar(500) NOT NULL DEFAULT '',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`merchant_id`),
  UNIQUE KEY `public_id` (`public_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_api_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trace_id` varchar(36) NOT NULL,
  `public_id` varchar(36) DEFAULT NULL,
  `order_no` varchar(48) DEFAULT NULL,
  `action` varchar(32) NOT NULL,
  `code` varchar(40) NOT NULL,
  `message` varchar(300) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_api_log` (`order_no`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_api_nonce` (
  `public_id` varchar(36) NOT NULL,
  `nonce` varchar(64) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`public_id`,`nonce`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_api_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(48) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `request_id` varchar(64) NOT NULL,
  `payload_hash` varchar(64) NOT NULL,
  `trade_type` varchar(12) NOT NULL,
  `card_no` varchar(34) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `occurred_at` datetime NOT NULL,
  `location` varchar(200) NOT NULL,
  `description` varchar(500) NOT NULL,
  `local_serial` varchar(64) DEFAULT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'NEW',
  `ledger_id` bigint(20) DEFAULT NULL,
  `error_message` varchar(300) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `completed_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  UNIQUE KEY `uk_api_request` (`merchant_id`,`request_id`),
  UNIQUE KEY `uk_api_serial` (`merchant_id`,`local_serial`),
  UNIQUE KEY `uk_api_ledger` (`ledger_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24746 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_api_outbox` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(48) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `body` text NOT NULL,
  `status` varchar(16) NOT NULL DEFAULT 'PENDING',
  `attempts` int(11) NOT NULL DEFAULT '0',
  `next_at` datetime NOT NULL,
  `locked_at` datetime DEFAULT NULL,
  `last_error` varchar(300) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_card` (
  `user_id` bigint(20) NOT NULL,
  `card_no` varchar(34) NOT NULL,
  `write_count` int(11) NOT NULL DEFAULT '0',
  `last_written_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `card_no` (`card_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_card_cancelled` (
  `card_no` varchar(34) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `write_count` int(11) NOT NULL,
  `last_written_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `cancelled_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cancelled_by` bigint(20) NOT NULL,
  PRIMARY KEY (`card_no`),
  KEY `ix_cancelled_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_card_write` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `card_no` varchar(34) NOT NULL,
  `request_key` varchar(36) NOT NULL,
  `operator_id` bigint(20) NOT NULL,
  `written_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `payload` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `request_key` (`request_key`)
) ENGINE=InnoDB AUTO_INCREMENT=235 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_finance_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(40) NOT NULL,
  `line_id` bigint(20) DEFAULT NULL,
  `action` varchar(20) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `note` varchar(1500) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_finance_audit` (`batch_id`,`id`),
  KEY `line_id` (`line_id`),
  CONSTRAINT `campus_finance_audit_ibfk_1` FOREIGN KEY (`batch_id`) REFERENCES `campus_finance_batch` (`id`),
  CONSTRAINT `campus_finance_audit_ibfk_2` FOREIGN KEY (`line_id`) REFERENCES `campus_finance_line` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_finance_batch` (
  `id` varchar(40) NOT NULL,
  `kind` varchar(16) NOT NULL,
  `merchant_id` bigint(20) DEFAULT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `status` varchar(20) NOT NULL DEFAULT 'OPEN',
  `source_name` varchar(150) DEFAULT NULL,
  `source_hash` varchar(64) DEFAULT NULL,
  `source_file_id` varchar(36) DEFAULT NULL,
  `total_count` int(11) NOT NULL DEFAULT '0',
  `difference_count` int(11) NOT NULL DEFAULT '0',
  `created_by` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `archived_by` bigint(20) DEFAULT NULL,
  `archived_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `source_file_id` (`source_file_id`),
  CONSTRAINT `campus_finance_batch_ibfk_1` FOREIGN KEY (`source_file_id`) REFERENCES `campus_finance_file` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_finance_file` (
  `id` varchar(36) NOT NULL,
  `mime` varchar(30) NOT NULL,
  `content` mediumblob NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_finance_line` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_id` varchar(40) NOT NULL,
  `category` varchar(24) NOT NULL,
  `difference_code` varchar(40) NOT NULL,
  `business_key` varchar(100) NOT NULL,
  `merchant_serial` varchar(64) DEFAULT NULL,
  `campus_order` varchar(48) DEFAULT NULL,
  `trade_type` varchar(16) DEFAULT NULL,
  `merchant_amount` decimal(16,2) DEFAULT NULL,
  `platform_amount` decimal(16,2) DEFAULT NULL,
  `merchant_status` varchar(20) DEFAULT NULL,
  `platform_status` varchar(20) DEFAULT NULL,
  `reason` varchar(1000) NOT NULL,
  `snapshot` mediumtext NOT NULL,
  `handling_status` varchar(20) NOT NULL,
  `handled_by` bigint(20) DEFAULT NULL,
  `handled_at` datetime DEFAULT NULL,
  `handle_note` varchar(1000) DEFAULT NULL,
  `evidence_id` varchar(36) DEFAULT NULL,
  `reviewed_by` bigint(20) DEFAULT NULL,
  `reviewed_at` datetime DEFAULT NULL,
  `review_note` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ix_finance_batch` (`batch_id`,`id`),
  KEY `evidence_id` (`evidence_id`),
  CONSTRAINT `campus_finance_line_ibfk_1` FOREIGN KEY (`batch_id`) REFERENCES `campus_finance_batch` (`id`),
  CONSTRAINT `campus_finance_line_ibfk_2` FOREIGN KEY (`evidence_id`) REFERENCES `campus_finance_file` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30547 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_ledger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `kind` varchar(12) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `balance_after` decimal(12,2) NOT NULL,
  `merchant_id` bigint(20) DEFAULT NULL,
  `original_id` bigint(20) DEFAULT NULL,
  `request_key` varchar(64) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `transaction_no` varchar(48) DEFAULT NULL,
  `merchant_name_snapshot` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_wallet_request` (`user_id`,`request_key`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`),
  KEY `ix_wallet_original` (`original_id`),
  KEY `merchant_id` (`merchant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26933 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `merchant_id` bigint(20) NOT NULL,
  `action` varchar(20) NOT NULL,
  `detail` text NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `ix_merchant_audit` (`merchant_id`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_category` (
  `code` varchar(20) NOT NULL,
  `name` varchar(40) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_extra` (
  `merchant_id` bigint(20) NOT NULL,
  `owner_name` varchar(50) NOT NULL DEFAULT '',
  `owner_phone` varchar(30) NOT NULL DEFAULT '',
  `license_no` varchar(100) NOT NULL DEFAULT '',
  `permit_no` varchar(100) NOT NULL DEFAULT '',
  `permit_expiry` date DEFAULT NULL,
  `introduction` varchar(2000) NOT NULL DEFAULT '',
  `license_image` varchar(36) DEFAULT NULL,
  `permit_image` varchar(36) DEFAULT NULL,
  `merchant_image` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_image` (
  `id` varchar(36) NOT NULL,
  `mime` varchar(30) NOT NULL,
  `content` mediumblob NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_profile` (
  `merchant_id` bigint(20) NOT NULL,
  `category` varchar(20) NOT NULL,
  `contact_name` varchar(50) NOT NULL,
  `phone` varchar(30) NOT NULL,
  `address` varchar(200) NOT NULL,
  `remark` varchar(500) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_merchant_sequence` (
  `id` int(11) NOT NULL,
  `seq` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_person_identity` (
  `user_id` bigint(20) NOT NULL,
  `identity_no` varchar(18) NOT NULL,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_recharge_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(48) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `status` varchar(16) NOT NULL,
  `ledger_id` bigint(20) NOT NULL,
  `request_key` varchar(64) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `paid_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ledger_id` (`ledger_id`),
  UNIQUE KEY `uk_recharge_request` (`user_id`,`request_key`),
  UNIQUE KEY `order_no` (`order_no`),
  CONSTRAINT `campus_recharge_order_ibfk_1` FOREIGN KEY (`ledger_id`) REFERENCES `campus_ledger` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2187 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `campus_wallet` (
  `user_id` bigint(20) NOT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'ACTIVE',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `student_no` varchar(30) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_student_no` (`student_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `cc_ledger` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `kind` varchar(12) NOT NULL,
  `amount` decimal(12,2) NOT NULL,
  `balance_after` decimal(12,2) NOT NULL,
  `merchant_id` bigint(20) DEFAULT NULL,
  `original_id` bigint(20) DEFAULT NULL,
  `request_key` varchar(64) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `settlement_id` varchar(36) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_request` (`user_id`,`request_key`),
  KEY `ix_original` (`original_id`),
  KEY `ix_merchant` (`merchant_id`,`settlement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `cc_merchant` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `cc_settlement` (
  `id` varchar(36) NOT NULL,
  `merchant_id` bigint(20) NOT NULL,
  `amount` decimal(14,2) NOT NULL,
  `actor_id` bigint(20) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `cc_wallet` (
  `user_id` bigint(20) NOT NULL,
  `card_no` varchar(40) NOT NULL,
  `status` varchar(12) NOT NULL DEFAULT 'ACTIVE',
  `balance` decimal(12,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `card_no` (`card_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `gate_in_out` (
  `id` bigint(16) NOT NULL COMMENT '主键',
  `user_id` bigint(16) DEFAULT NULL COMMENT '用户',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `kind` int(1) DEFAULT NULL COMMENT '1进入2外出',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='门禁记录';
CREATE TABLE `project_attend` (
  `id` bigint(16) NOT NULL COMMENT '主键',
  `user_id` bigint(16) DEFAULT NULL COMMENT '考勤人',
  `status` int(1) DEFAULT NULL COMMENT '0正常1迟到',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '考勤时间',
  `project_id` bigint(16) DEFAULT NULL COMMENT '考勤项目',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC COMMENT='考勤记录';
CREATE TABLE `project_info` (
  `project_id` bigint(16) NOT NULL COMMENT '考勤码',
  `project_name` varchar(255) DEFAULT NULL COMMENT '考勤项目',
  `start_time` varchar(255) DEFAULT NULL COMMENT '考勤开始时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '考勤结束时间',
  `remark` varchar(2000) DEFAULT NULL COMMENT '项目介绍',
  `user_id` bigint(16) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`project_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='考勤项目';
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(16) NOT NULL COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT '' COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT '' COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典数据表';
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(16) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` int(11) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `url` varchar(200) DEFAULT '#' COMMENT '请求地址',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `perms` varchar(100) DEFAULT '' COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8100506 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';
CREATE TABLE `sys_notice` (
  `notice_id` bigint(16) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(2) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longtext NOT NULL COMMENT '公告内容',
  `file_name` varchar(255) DEFAULT NULL COMMENT '附件名称',
  `file_url` varchar(255) DEFAULT NULL COMMENT '附件下载地址',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `is_all` char(1) DEFAULT NULL COMMENT '是否全部可看',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='通知公告表';
CREATE TABLE `sys_permission_audit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actor_id` bigint(20) NOT NULL,
  `action` varchar(32) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `detail` text NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_permission_lock` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `sys_role` (
  `role_id` bigint(16) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限）',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色信息表';
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(16) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(16) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';
CREATE TABLE `sys_user` (
  `user_id` bigint(16) NOT NULL COMMENT '用户ID',
  `login_name` varchar(30) NOT NULL COMMENT '学工号',
  `user_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像路径',
  `password` varchar(50) DEFAULT '' COMMENT '密码',
  `salt` varchar(20) DEFAULT '' COMMENT '盐加密',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(20) DEFAULT '' COMMENT '最后登陆IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `role_id` bigint(16) DEFAULT NULL COMMENT '角色编号',
  `birthday` date DEFAULT NULL COMMENT '出生日期',
  `mz` varchar(255) DEFAULT NULL COMMENT '民族',
  `xjzt` varchar(255) DEFAULT NULL COMMENT '学籍状态',
  `department` varchar(255) DEFAULT NULL COMMENT '院系',
  `profession` varchar(255) DEFAULT NULL COMMENT '专业',
  `classname` varchar(255) DEFAULT NULL COMMENT '班级',
  `studyyear` varchar(255) DEFAULT NULL COMMENT '学制',
  `identity` varchar(255) DEFAULT NULL COMMENT '身份',
  `address` varchar(255) DEFAULT NULL COMMENT '住址',
  `project_id` bigint(16) DEFAULT NULL COMMENT '考勤码',
  `project_join_time` datetime DEFAULT NULL COMMENT '加入考勤日期',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `uk_login_name` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `ix_role_users` (`role_id`,`user_id`),
  CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`),
  CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `user_secret_protect` (
  `id` bigint(16) NOT NULL COMMENT '编号',
  `question` varchar(255) DEFAULT NULL COMMENT '问题',
  `answer` varchar(255) DEFAULT NULL COMMENT '回答',
  `user_id` bigint(16) DEFAULT NULL COMMENT '用户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='密保问题';
SET FOREIGN_KEY_CHECKS=1;