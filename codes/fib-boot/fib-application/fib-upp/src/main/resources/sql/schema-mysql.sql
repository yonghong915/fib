DROP TABLE IF EXISTS `communication_event`;
CREATE TABLE `communication_event`  (
  `pk_id` bigint(0) NOT NULL COMMENT '主键编码',
  `comm_event_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交流事件编号',
  `comm_event_type_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交流事件类型',
  `parent_comm_event_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上一级交流事件号',
  `message_status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报文状态',
  `party_id_from` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '发起机构',
  `party_id_to` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '-1' COMMENT '接收机构',
  `entry_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '录入日期',
  `datetime_started` timestamp(0) NULL DEFAULT NULL COMMENT '开始时间',
  `datetime_ended` timestamp(0) NULL DEFAULT NULL COMMENT '结束时间',
  `message_id` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报文标识号',
  `message_type_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报文类型',
  `message_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '报文内容',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `channel_serial_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道流水号',
  `message_ref_num` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通讯参考号',
  `del_flag` smallint(0) NOT NULL DEFAULT 0 COMMENT '删除标志',
  `create_by` bigint(0) NOT NULL DEFAULT 0 COMMENT '创建人',
  `create_dt` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` bigint(0) NOT NULL DEFAULT 0 COMMENT '更新人',
  `update_dt` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`pk_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '交流事件' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;