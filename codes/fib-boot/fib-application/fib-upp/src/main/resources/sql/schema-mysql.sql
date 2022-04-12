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

DROP TABLE IF EXISTS message_pack_rule;
CREATE TABLE message_pack_rule  (
  pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
  message_type_code VARCHAR(20)  NOT NULL COMMENT '报文类型',
  rule_value int(11) NOT NULL COMMENT '规则值',
  remark VARCHAR(255)  COMMENT '备注',
  del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (pk_id) 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '报文组包规则';


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


DROP TABLE IF EXISTS table_cols_dic;
CREATE TABLE table_cols_dic  (
   pk_id bigint(0) NOT NULL COMMENT '主键编码',
   col_en_name VARCHAR(50) NOT NULL  COMMENT '列英名',
   col_cn_name varchar(20) NOT NULL COMMENT '列中文名',
   data_type varchar(50) NOT NULL COMMENT '数据类型',
   remark varchar(100)  COMMENT '备注',
   del_flag smallint(0)  DEFAULT 0 COMMENT '删除标志',
   create_by bigint(0)  DEFAULT 0 COMMENT '创建人',
   create_dt timestamp(0)  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   update_by bigint(0)  DEFAULT 0 COMMENT '更新人',
   update_dt timestamp(0) COMMENT '更新时间',
  PRIMARY KEY (pk_id)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '表字段字典' ;

CREATE TABLE BEPS_QUEUE
    (
        queue_id VARCHAR(20) NOT null,
        queue_type VARCHAR(20) NOT NULL,
        CURRENCY VARCHAR(10),
        queue_status VARCHAR(10), 
        SINGLE_MULTI VARCHAR(1), 
        MANUALLY_FLG VARCHAR(1), 
        START_TIME TIMESTAMP, 
        tm_INTERVAL BIGINT, 
        FREQUENCY VARCHAR(1), 
        LAST_UPDATED_STAMP TIMESTAMP,
        LAST_UPDATED_TX_STAMP TIMESTAMP, 
        CREATED_STAMP TIMESTAMP, 
        CREATED_TX_STAMP TIMESTAMP, 
        PRIMARY KEY (queue_id)
    )

    CREATE TABLE BEPS_QUEUE_HEADER
    (
        queue_ID VARCHAR(20) NOT NULL, queue_TYPE VARCHAR(10), CURRENCY VARCHAR(10), STATUS VARCHAR(10), AMOUNT DECIMAL(19,2),
        TOTAL BIGINT, LAST_UPDATED_STAMP TIMESTAMP, LAST_UPDATED_TX_STAMP TIMESTAMP, CREATED_STAMP TIMESTAMP,
        CREATED_TX_STAMP TIMESTAMP, PRIMARY KEY (queue_ID)
    )
    
    
    CREATE TABLE BEPS_QUEUE_ITEM
    (
        QUEUE_ID VARCHAR(20) NOT NULL, SEQ_NO VARCHAR(20) NOT NULL, RECORD_TYPE VARCHAR(10), RECORD_ID VARCHAR(20),
        MESSAGE_TYPE VARCHAR(20), STATUS VARCHAR(10), RESULT CHARACTER(1), CANCEL_REASON_CD VARCHAR(10), PRODUCT_ID
        VARCHAR(10), LAST_UPDATED_STAMP TIMESTAMP, LAST_UPDATED_TX_STAMP TIMESTAMP, CREATED_STAMP TIMESTAMP,
        CREATED_TX_STAMP TIMESTAMP, PRIMARY KEY (QUEUE_ID, SEQ_NO)
    )