DROP TABLE IF EXISTS order_header;
CREATE TABLE communication_event  (
  order_id BIGINT(20) NOT NULL COMMENT '订单号',
  del_flag SMALLINT DEFAULT 0 COMMENT '删除标志',
  create_by BIGINT(20)  DEFAULT 0 COMMENT '创建人',
  create_dt TIMESTAMP  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by BIGINT(0)  DEFAULT 0 COMMENT '更新人',
  update_dt TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (order_id) 
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '订单';