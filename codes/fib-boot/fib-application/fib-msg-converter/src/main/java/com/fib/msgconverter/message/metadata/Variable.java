package com.fib.msgconverter.message.metadata;

public class Variable {

	private String id;
	private String name;
	private int dataType;
	private String value;

	public Variable() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean equals(Variable variable) {
		if (null == variable) {
			return false;
		}
		if (!this.name.equalsIgnoreCase(variable.getName())) {
			return false;
		}
		if (this.dataType != variable.getDataType()) {
			return false;
		}
		return this.value.equalsIgnoreCase(variable.getValue());
	}

	public Variable B() {
		Variable a = new Variable();
		a.setName(name);
		a.setValue(value);
		a.setDataType(dataType);
		return a;
	}
}
