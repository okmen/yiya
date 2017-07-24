package com.bbyiya.pic.vo.product;

import java.io.Serializable;

public class PublicFinacingMyPro implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String headImg;
	private Double amountLimit;
	private Double price;
	private String title;
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public Double getAmountLimit() {
		return amountLimit;
	}
	public void setAmountLimit(Double amountLimit) {
		this.amountLimit = amountLimit;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
