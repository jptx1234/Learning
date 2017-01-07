package com.github.jptx1234.productManSysConVer.domain;

public class Product {
	private String UUID;
	private String name;
	private String price;
	private String num;
	private String description;
	
	
	
	public Product(String uUID, String name, String price, String num, String description) {
		UUID = uUID;
		this.name = name;
		this.price = price;
		this.num = num;
		this.description = description;
	}
	public Product(){
		
	}

	public void setUUID(String UUID){
		this.UUID=UUID;
	}
	
	public String getUUID() {
		return UUID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	

}
