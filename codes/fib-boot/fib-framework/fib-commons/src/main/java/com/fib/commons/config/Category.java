package com.fib.commons.config;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private List<Property> properties = new ArrayList<>();
	private String name;

	public void add(Property prop) {
		properties.add(prop);
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
