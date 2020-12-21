DROP TABLE IF EXISTS sys_error_code;
 CREATE TABLE sys_error_code(
     pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
     language varchar(10) NOT NULL DEFAULT 'zh_CN' COMMENT '语言',
     system_code varchar(10) NOT NULL DEFAULT 'uias' COMMENT '系统编码',
     error_code varchar(10) NOT NULL COMMENT '错误码',
     error_desc varchar(255) NOT NULL COMMENT '错误描述',
     del_flag SMALLINT NOT NULL DEFAULT 0,
     create_by BIGINT(20) NOT NULL DEFAULT 0,
     create_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     update_by BIGINT(20)  NOT NULL DEFAULT 0,
     update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (pk_id)
 );
CREATE UNIQUE INDEX uk_error_code ON sys_error_code(error_code);
 