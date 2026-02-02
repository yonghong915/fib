package com.fib.midbiz.service;

import java.util.UUID;

import com.fib.commons.xml.dom4j.XmlAttribute;
import com.fib.commons.xml.dom4j.XmlElement;

public class Department {
	@XmlAttribute
	private String id = UUID.randomUUID().toString();

	@XmlElement(isCData = true)
	private String name;

	@XmlElement(name = "manager")
	private Employee manager;

	public Department(String name) {
		this.name = name;
		this.manager = new Employee("张总", 40);
	}
}
