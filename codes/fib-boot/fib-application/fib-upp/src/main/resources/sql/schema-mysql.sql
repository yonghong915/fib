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
    );

    CREATE TABLE BEPS_QUEUE_HEADER
    (
        queue_ID VARCHAR(20) NOT NULL, queue_TYPE VARCHAR(10), CURRENCY VARCHAR(10), STATUS VARCHAR(10), AMOUNT DECIMAL(19,2),
        TOTAL BIGINT, LAST_UPDATED_STAMP TIMESTAMP, LAST_UPDATED_TX_STAMP TIMESTAMP, CREATED_STAMP TIMESTAMP,
        CREATED_TX_STAMP TIMESTAMP, PRIMARY KEY (queue_ID)
    );
    
    
    CREATE TABLE BEPS_QUEUE_ITEM
    (
        QUEUE_ID VARCHAR(20) NOT NULL, SEQ_NO VARCHAR(20) NOT NULL, RECORD_TYPE VARCHAR(10), RECORD_ID VARCHAR(20),
        MESSAGE_TYPE VARCHAR(20), STATUS VARCHAR(10), RESULT CHARACTER(1), CANCEL_REASON_CD VARCHAR(10), PRODUCT_ID
        VARCHAR(10), LAST_UPDATED_STAMP TIMESTAMP, LAST_UPDATED_TX_STAMP TIMESTAMP, CREATED_STAMP TIMESTAMP,
        CREATED_TX_STAMP TIMESTAMP, PRIMARY KEY (QUEUE_ID, SEQ_NO)
    );
    
    
  	CREATE TABLE BEPS_MESSAGE_SERVICE_MAP
    (
        MESSAGE_TYPE VARCHAR(20) NOT NULL, SERVICE_NAME VARCHAR(60), MSG_VERSION CHARACTER(1), OPERATION_TYPE VARCHAR(20),
        LAST_UPDATED_STAMP TIMESTAMP, LAST_UPDATED_TX_STAMP TIMESTAMP, CREATED_STAMP TIMESTAMP, CREATED_TX_STAMP
        TIMESTAMP, PRIMARY KEY (MESSAGE_TYPE)
    );
    
    create table  batch_process_group(
	   batch_group_id varchar(50),
	   trans_num int,
	   trans_amt decimal(20,2),
	   drawee_acct_no varchar(20),
	   drawee_acct_name varchar(200),
	   biz_type varchar(10),
	   batch_type varchar(20),
	   trans_id varchar(50),
	   party_id varchar(20),
	   teller_id varchar(20),
	   PRIMARY KEY (batch_group_id)
  );
  
  
    create table  batch_process(
       batch_id varchar(50),
	   batch_group_id varchar(50),
	   batch_type varchar(50),
	   process_status char(2),
	   trans_num int,
	   trans_amt decimal(20,2),
	   succ_num int,
	   succ_amt decimal(20,2),
	   fail_num int,
	   fail_amt decimal(20,2),
	   chanl_serial_no varchar(50),
	   biz_type varchar(20),
	   snd_Clear_Bank_Code varchar(20),
	   rcv_Clear_Bank_Code varchar(20),
	   PRIMARY KEY (batch_id)
  );
  
    create table  batch_process_detail(
       batch_id varchar(50),
	   batch_seq_id varchar(50),
	   payee_acct_no varchar(50),
	   payee_acct_name varchar(20),
	   payee_acct_type varchar(20),
	   payee_address varchar(20),
	   payee_bank_code varchar(20),
	   payee_bank_name varchar(50),
	   payee_clear_bank_code varchar(20),
	   payee_clear_bank_name varchar(50),
	   drawee_acct_no varchar(50),
	   drawee_acct_name varchar(50),
	   drawee_acct_type varchar(50),
	   drawee_address varchar(50),
	   drawee_bank_code varchar(50),
	   drawee_bank_name varchar(50),
	   drawee_clear_bank_code varchar(50),
	   drawee_clear_bank_name varchar(50),
	   currency_type varchar(50),
	   trans_amt decimal(20,2),
	   sys_code varchar(20),
	   biz_type varchar(20),
	   biz_class varchar(20),
	   order_id varchar(20),
	   opp_id varchar(20),
	   rsp_code varchar(20),
	   rsp_msg varchar(20),
	   cust_postscript varchar(20),
	   primary key (batch_id,batch_seq_id)
  );
  
  
  create table payment_prefer_info ( 
    payment_prefer_id BIGINT(20) , 
    order_id BIGINT(20) , 
    payment_method_type_id VARCHAR(50) , 
    payment_method_id VARCHAR(50) , 
    trans_amt decimal (20,2)   ,
    payment_status VARCHAR(50) , 
    acct_no VARCHAR(50) , 
    acct_name VARCHAR(50) , 
    acct_type VARCHAR(50) , 
    sys_code VARCHAR(50) , 
    bank_code VARCHAR(50) , 
    bank_name VARCHAR(50) , 
    trade_org_id VARCHAR(50) , 
    dc_flag VARCHAR(50) , 
    route_id VARCHAR(50) , 
    item_seq_id VARCHAR(50) , 
    cust_postscript VARCHAR(50) , 
    bank_postscript VARCHAR(50) ,
    del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (payment_prefer_id) 
 );
  
  
  create table msg_register_info(
   pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
   pay_serial_no varchar(50) NOT NULL COMMENT '支付流水号',
   sys_code varchar(10) NOT NULL COMMENT '系统编号',
   msg_type_code varchar(50) NOT NULL COMMENT '报文类型',
   msg_id varchar(20) NOT NULL COMMENT '报文标识号',
   msg_direction char(1) NOT NULL COMMENT '报文方向,I来/O往', 
   sys_date char(10) NOT NULL  COMMENT '人行日期',
   netting_date char(10) COMMENT '轧差日期',
   netting_round char(2) COMMENT '轧差场次',
   process_status varchar(10) COMMENT '处理状态',
   trans_amt decimal(20,2),
   trans_num int,
   succ_amt decimal(20,2),
   succ_num int,
   snd_bank_code  varchar(20) COMMENT '发起行号',
   rcv_bank_code varchar(20) COMMENT '接收行号',
   msg_content_id BIGINT(20) NOT NULL COMMENT '报文内容编码',
   del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
   PRIMARY KEY (pk_id)
);


create table acct_trans_register(
   pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
   chanl_serial_no varchar(50) NOT NULL COMMENT '渠道流水号',
   dr_acct_no varchar(50) NOT NULL,
   dr_acct_name varchar(200) NOT NULL,
   cr_acct_no  varchar(50) NOT NULL,
   cr_acct_name  varchar(200) NOT NULL,
   currency_code varchar(4) NOT NULL,
   tran_amt decimal(21,2) NOT NULL ,
   order_id BIGINT(20) NOT NULL,
   op_id BIGINT(20),
   sys_code varchar(10) NOT NULL COMMENT '系统编号',
   trans_org_code varchar(20),
   billing_date varchar(10),
   core_serial_no varchar(20),
   acct_reg_type varchar(20) NOT NULL COMMENT '账户状态[POSTING:过账; CLEARING:清算，记备付金 ; SUSPEND:挂账转滞留 SueBAPIn：定期来贷清算中心与落地机构账;FEE_POSTING:手续费]',
   status_id varchar(20) NOT NULL COMMENT '记账状态[RECEIVED:已成功  ROLLBACK:已冲正  REFUND：已蓝冲',
   remark varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
   del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
   create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
   create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
   update_dt TIMESTAMP(0) COMMENT '更新时间',
   PRIMARY KEY (pk_id) 
);


CREATE TABLE cnaps_system_status (
  pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
  SYS_CODE varchar(20)  NOT NULL,
  CLEAR_BANK_CODE varchar(60)  NOT NULL,
  orig_SYS_DATE varchar(10)  DEFAULT NULL,
  orig_SYS_STATUS varchar(10)  DEFAULT NULL,
  CURRENT_SYS_DATE varchar(10)  DEFAULT NULL,
  CURRENT_SYS_STATUS varchar(10)  DEFAULT NULL,
  HOLIDAY_FLAG varchar(10)  DEFAULT NULL,
  special_Work_Day_Flag varchar(10)  DEFAULT NULL,
  NEXT_SYS_DATE varchar(10)  DEFAULT NULL,
  LOGIN_OPER_TYPE varchar(10)  DEFAULT NULL,
  PROCESS_CODE varchar(10)  DEFAULT NULL,
  REJECT_INFO varchar(400)  DEFAULT NULL,
  BANK_CHANGE_NUMBER varchar(10)  DEFAULT NULL,
  BASE_DATA_CHANGE_NUMBER varchar(10)  DEFAULT NULL,
  CIS_CHANGE_NUMBER varchar(10)  DEFAULT NULL,
  PARAMETER_CHANGE_NUMBER varchar(10)  DEFAULT NULL,
  del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
   create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
   create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
   update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
  PRIMARY KEY (pk_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='订单支付项';