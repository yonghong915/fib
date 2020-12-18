DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user(
   pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
   user_code VARCHAR(10) NOT NULL COMMENT '用户编号',
   real_name VARCHAR(100) NOT NULL COMMENT '用户真实姓名',
   user_desc VARCHAR(200) COMMENT '用户描述',
   user_type SMALLINT NOT NULL COMMENT '用户类型',
   user_status SMALLINT NOT NULL DEFAULT 1 COMMENT '用户状态',
   del_flag SMALLINT NOT NULL DEFAULT 0,
   create_by BIGINT(20) NOT NULL DEFAULT 0,
   create_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   update_by BIGINT(20)  NOT NULL DEFAULT 0,
   update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (pk_id)
 ) ;
 
 CREATE UNIQUE INDEX uk_user_name ON sys_user(user_code);
 
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
 
 