package com.fib.upp.util.configuration.jconfig;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Variable {
	private String name;
	private String text;
	private List<Property> properties = new ArrayList<>();

	public void addProperty(Property property) {
		properties.add(property);
	}
}
