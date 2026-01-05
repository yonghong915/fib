package com.fib.uqcp.entity.model;

import java.io.Serializable;

import org.dom4j.Element;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;

public class ModelField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1935367152335615691L;

	private String name;
	private String type;
	private String colName;
	private boolean isPk = false;
	private boolean encrypt = false;
	private boolean isNotNull = false;

	public ModelField(Element fieldElement) {
		Assert.notNull(fieldElement.attributeValue("type"), () -> new RuntimeException(""));
		Assert.notNull(fieldElement.attributeValue("name"), () -> new RuntimeException(""));
		this.type = fieldElement.attributeValue("type");
		this.name = fieldElement.attributeValue("name");
		this.setColName(fieldElement.attributeValue("colName"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		if (StrUtil.isEmptyIfStr(colName)) {
			this.colName = StrUtil.toUnderlineCase(this.name);
		} else {
			this.colName = colName;
		}
	}

	public boolean isPk() {
		return isPk;
	}

	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}

	public boolean isEncrypt() {
		return encrypt;
	}

	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}

	public boolean isNotNull() {
		return isNotNull;
	}

	public void setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
}