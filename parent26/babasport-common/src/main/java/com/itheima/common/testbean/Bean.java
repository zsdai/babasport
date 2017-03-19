package com.itheima.common.testbean;

import java.io.Serializable;

public class Bean implements Serializable{

	/**  
	* @Fields serialVersionUID:
	*/ 
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private double price;
	
	public Bean() {
		super();
	}
	public Bean(int id, String name, double price) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
}
