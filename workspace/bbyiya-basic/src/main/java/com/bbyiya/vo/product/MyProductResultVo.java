package com.bbyiya.vo.product;

import java.util.List;

import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;

public class MyProductResultVo extends PMyproducts{
	private static final long serialVersionUID = 1L;
	//产品数量
	private int count;
	private String headImg;
	//是否下单
	private int isOrder;
	private String description;
	private List<PMyproductdetails> detailslist;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public List<PMyproductdetails> getDetailslist() {
		return detailslist;
	}
	public void setDetailslist(List<PMyproductdetails> detailslist) {
		this.detailslist = detailslist;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	

}
