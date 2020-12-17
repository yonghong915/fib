package com.fib.core.base.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fib.commons.util.serialize.DateJsonDeserializer;
import com.fib.commons.util.serialize.DateJsonSerializer;

import lombok.Data;

/**
 * 实体基类
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-15
 */
@Data
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@TableId
	private Long pkId;

	/** 创建人 */
	private Integer createBy;

	/** 创建时间 */
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	private Timestamp createDt;

	/** 更新人 */
	private Integer updateBy;

	/** 更新时间 */
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonSerialize(using = DateJsonSerializer.class)
	private Timestamp updateDt;

	/**
	 * 删除标识:1删除,0没删除
	 */
	private Integer delFlag = 0;
}
