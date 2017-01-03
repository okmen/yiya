package com.bbyiya.pic.vo.order;

import java.io.Serializable;
import java.util.List;

public class SubmitOrderParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String remark;
	private Long salesUserId;
	private List<SubmitOrderProductParam> products;
	
	
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<SubmitOrderProductParam> getProducts() {
		return products;
	}
	public void setProducts(List<SubmitOrderProductParam> products) {
		this.products = products;
	}
	public Long getSalesUserId() {
		return salesUserId;
	}
	public void setSalesUserId(Long salesUserId) {
		this.salesUserId = salesUserId;
	}
	
	
}
