package com.bbyiya.vo.product;

import java.io.Serializable;
import java.util.List;

public class ProductResult implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long productId;
	private String title;
	private Long branchUserId;
	
	private List<ProductStandardResult> propertyList;
	
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getBranchUserId() {
		return branchUserId;
	}
	public void setBranchUserId(Long branchUserId) {
		this.branchUserId = branchUserId;
	}
	public List<ProductStandardResult> getPropertyList() {
		return propertyList;
	}
	public void setPropertyList(List<ProductStandardResult> propertyList) {
		this.propertyList = propertyList;
	}
	
	
}
