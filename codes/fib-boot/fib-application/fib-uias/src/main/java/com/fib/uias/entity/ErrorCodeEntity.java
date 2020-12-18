package com.fib.uias.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fib.core.base.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("SYS_ERROR_CODE")
public class ErrorCodeEntity extends BaseEntity {

	private static final long serialVersionUID = 5565692809127703716L;
	/***/
	private String language;
	/***/
	private String systemCode;
	/***/
	private String errorCode;
	/***/
	private String errorDesc;
}
