create table if not exists t_order (
    pk_id BIGINT(20) NOT NULL COMMENT '主键编码',
    create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
    create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
    update_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  COMMENT '更新时间',
    PRIMARY KEY (pk_id)
 ) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '订单支付项';