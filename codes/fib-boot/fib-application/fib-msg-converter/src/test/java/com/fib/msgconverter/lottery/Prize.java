package com.fib.msgconverter.lottery;

public class Prize {
	private Integer index;
	private String id;
	private String name;
	private Integer quantity;

	private Double probability;// 抽奖概率

	public Prize() {
	}

	public Prize(Integer index, String id, String name, Double probability) {
		this.index = index;
		this.id = id;
		this.name = name;
		this.probability = probability;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

}
