package com.fib.gateway.message.metadata;

/**
 * 
 * @author fangyh
 * @version 1.0
 * @since 1.0
 * @date 2020-12-31
 */
public class Variable {
	private String id;
	private String name;
	private int dataType;
	private String value;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDataType() {
		return this.dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean equalsTo(Variable variable) {
		if (null == variable) {
			return false;
		} else if (!this.name.equalsIgnoreCase(variable.getName())) {
			return false;
		} else if (this.dataType != variable.getDataType()) {
			return false;
		} else {
			return this.value.equalsIgnoreCase(variable.getValue());
		}
	}

	public Variable copy() {
		Variable variable = new Variable();
		variable.setName(this.name);
		variable.setValue(this.value);
		variable.setDataType(this.getDataType());
		return variable;
	}
}
