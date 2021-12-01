package com.fib.commons.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Category {
	private List<Property> properties = new ArrayList<>();
	private String name;

	public void add(Property prop) {
		properties.add(prop);
	}
}
