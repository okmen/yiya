package com.bbyiya.pic.vo.product;

import java.util.List;

import com.bbyiya.model.PMyproducts;

public class MyProductsResult extends PMyproducts{

	private static final long serialVersionUID = 1L;
	//��Ʒ����
	private int count;
	private String headImg;
	//�Ƿ��µ�
	private int isOrder;
	private String description;
	private List<MyProductsDetailsResult> detailslist;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public int getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(int isOrder) {
		this.isOrder = isOrder;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<MyProductsDetailsResult> getDetailslist() {
		return detailslist;
	}
	public void setDetailslist(List<MyProductsDetailsResult> detailslist) {
		this.detailslist = detailslist;
	}
	
	
}
