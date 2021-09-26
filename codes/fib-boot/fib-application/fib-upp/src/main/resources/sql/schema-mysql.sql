DROP TABLE IF EXISTS communication_event;
CREATE TABLE communication_event  (
  pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
  comm_event_id VARCHAR(20)  NOT NULL COMMENT '交流事件编号',
  comm_event_type_code VARCHAR(20)  NOT NULL COMMENT '交流事件类型',
  parent_comm_event_id VARCHAR(20)  COMMENT '上一级交流事件号',
  system_code VARCHAR(10)  COMMENT '系统编号',
  message_status VARCHAR(20)  NOT NULL COMMENT '报文状态',
  party_id_from VARCHAR(20)  NOT NULL DEFAULT '-1' COMMENT '发起机构',
  party_id_to VARCHAR(20)  NOT NULL DEFAULT '-1' COMMENT '接收机构',
  entry_date TIMESTAMP NOT NULL COMMENT '录入日期',
  datetime_started TIMESTAMP(0) NULL DEFAULT NULL COMMENT '开始时间',
  datetime_ended TIMESTAMP(0) NULL DEFAULT NULL COMMENT '结束时间',
  process_date TIMESTAMP(0) NULL DEFAULT NULL COMMENT '处理日期,大额清算日期，小额轧差日期',
  message_id VARCHAR(20)  NOT NULL COMMENT '报文标识号',
  message_type_code VARCHAR(20)  NOT NULL COMMENT '报文类型',
  message_content text  NULL COMMENT '报文内容',
  remarks VARCHAR(255)  COMMENT '备注',
  channel_serial_no VARCHAR(255)  NOT NULL COMMENT '渠道流水号',
  message_ref_num VARCHAR(20) COMMENT '通讯参考号',
  del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (pk_id) 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '交流事件';

DROP TABLE IF EXISTS beps_message_pack_rule;
CREATE TABLE beps_message_pack_rule  (
  pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
  message_type_code VARCHAR(20)  NOT NULL COMMENT '报文类型',
  transaction_type CHAR(1) NOT NULL DEFAULT 'N'  COMMENT '业务类型',
  send_clearing_bank CHAR(1) NOT NULL DEFAULT 'N'  COMMENT '发起清算行',
  receive_clearing_bank CHAR(1) NOT NULL DEFAULT 'N'   COMMENT '接收清算行',
  return_limited CHAR(1) NOT NULL DEFAULT 'N'  COMMENT '回执期',
  original_message_id CHAR(1) NOT NULL DEFAULT 'N'  COMMENT '原报文标识号',
  batch_id CHAR(1) NOT NULL DEFAULT 'N' COMMENT '批次号',
  del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (pk_id) 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '小额组包规则';


DROP TABLE IF EXISTS message_service_mapping;
CREATE TABLE message_service_mapping  (
   pk_id bigint(0) NOT NULL COMMENT '主键编码',
   system_code VARCHAR(10) NOT NULL  COMMENT '系统编号',
   message_type_code varchar(20) NOT NULL COMMENT '报文类型',
   service_name varchar(100) COMMENT '服务名',
   message_version char(1) NOT NULL COMMENT '报文版本',
   operate_type char(1) NOT NULL COMMENT '操作类型,N正常业务报文,R业务回执报文,I信息报文',
   acct_chck_type char(1) NOT NULL COMMENT '对账类型,B业务报文对账,I信息报文对账',
   del_flag smallint(0)  DEFAULT 0 COMMENT '删除标志',
   create_by bigint(0)  DEFAULT 0 COMMENT '创建人',
   create_dt timestamp(0)  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   update_by bigint(0)  DEFAULT 0 COMMENT '更新人',
   update_dt timestamp(0) COMMENT '更新时间',
  PRIMARY KEY (pk_id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '报文服务映射' ;




