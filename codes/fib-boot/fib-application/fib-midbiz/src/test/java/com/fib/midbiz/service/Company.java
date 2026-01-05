package com.fib.midbiz.service;

import java.util.ArrayList;
import java.util.List;

import com.fib.commons.xml.dom4j.XmlAttribute;
import com.fib.commons.xml.dom4j.XmlElement;
import com.fib.commons.xml.dom4j.XmlElementWrapper;
import com.fib.commons.xml.dom4j.XmlRootElement;

@XmlRootElement(name = "company")
public class Company {
	@XmlAttribute
	private String id = "C001";

	@XmlElement(order = 1)
	private String name = "Tech Corp";

	@XmlElementWrapper(name = "departments")
	@XmlElement(name = "department", order = 2)
	private List<Department> departments = new ArrayList<>();

	// 构造方法
	public Company() {
		departments.add(new Department("研发部"));
		departments.add(new Department("销售部"));
	}
}