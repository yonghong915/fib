package com.fib.midbiz.service;

import com.fib.commons.xml.dom4j.XmlElement;

public class Employee {
	@XmlElement(order = 2)
	private String name;

	@XmlElement(order = 1)
	private int age;

	public Employee(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
